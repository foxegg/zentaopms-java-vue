package com.zentao.service;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 备份模块服务 - 对应 module/backup，列表与配置与 PHP 对齐；支持 MySQL mysqldump 备份与 mysql 还原
 */
@Service
@RequiredArgsConstructor
public class BackupService {

    private static final String CONFIG_OWNER = "system";
    private static final String CONFIG_MODULE = "backup";
    private static final String CONFIG_SECTION = "setting";
    private static final String KEY_SETTING_DIR = "settingDir";
    private static final Pattern JDBC_MYSQL = Pattern.compile("jdbc:mysql://([^:/]+):?(\\d*)/?([^/?]*).*");

    private final ConfigRepository configRepository;

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    public String getBackupPath() {
        String dir = getSettingValue(KEY_SETTING_DIR);
        if (dir == null || dir.isBlank()) {
            dir = System.getProperty("java.io.tmpdir", "/tmp") + File.separator + "backup";
        }
        dir = dir.replace('\\', '/').replaceAll("/+$", "") + "/";
        return dir;
    }

    /** 与 PHP backupZen getBackupList 一致：扫描备份目录 *.sql*（含 .sql、.sql.gz、.sql.php 等）返回列表 */
    public List<Map<String, Object>> getBackupList() {
        String backupPath = getBackupPath();
        Path path = Path.of(backupPath);
        if (!Files.isDirectory(path)) {
            return List.of();
        }
        Map<String, Path> baseToPath = new HashMap<>();
        try (Stream<Path> stream = Files.list(path)) {
            stream.filter(p -> {
                String name = p.getFileName().toString();
                return name.contains(".sql");
            }).forEach(p -> {
                String name = p.getFileName().toString();
                int idx = name.indexOf(".sql");
                String base = idx >= 0 ? name.substring(0, idx) : name;
                baseToPath.putIfAbsent(base, p);
            });
        } catch (Exception e) {
            return List.of();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Path> e : baseToPath.entrySet()) {
            String base = e.getKey();
            Path sqlPath = e.getValue();
            Map<String, Object> item = new HashMap<>();
            item.put("id", base.replace("-", "_"));
            item.put("name", base);
            try {
                item.put("time", Files.getLastModifiedTime(sqlPath).toMillis());
            } catch (Exception ignored) {
                item.put("time", 0L);
            }
            list.add(item);
        }
        list.sort((a, b) -> Long.compare((Long) b.get("time"), (Long) a.get("time")));
        return list;
    }

    public Map<String, String> getSetting() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION);
        return configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
    }

    public void saveSetting(Map<String, Object> body) {
        if (body == null) return;
        for (Map.Entry<String, Object> e : body.entrySet()) {
            String key = e.getKey();
            String value = e.getValue() != null ? String.valueOf(e.getValue()) : "";
            configRepository.findByOwnerAndModuleAndSectionAndKey_(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION, key)
                    .ifPresentOrElse(
                            c -> { c.setValue(value); configRepository.save(c); },
                            () -> {
                                Config c = new Config();
                                c.setOwner(CONFIG_OWNER);
                                c.setModule(CONFIG_MODULE);
                                c.setSection(CONFIG_SECTION);
                                c.setKey_(key);
                                c.setValue(value);
                                c.setVision("");
                                configRepository.save(c);
                            }
                    );
        }
    }

    /** 磁盘空间 - 与 PHP ajaxGetDiskSpace 一致 */
    public Map<String, Long> getDiskSpace() {
        File dir = new File(getBackupPath());
        try {
            if (!dir.exists()) dir.mkdirs();
            return Map.of("free", dir.getUsableSpace(), "total", dir.getTotalSpace());
        } catch (Exception e) {
            return Map.of("free", 0L, "total", 0L);
        }
    }

    /** 与 PHP backup restore 一致：按文件名还原；仅校验 fileName 在 list 的 name 中存在 */
    public boolean restore(String fileName) {
        if (fileName == null || fileName.isBlank()) return false;
        String safe = fileName.replace("..", "").replace("/", "").replace("\\", "");
        List<Map<String, Object>> list = getBackupList();
        boolean found = list.stream().anyMatch(m -> safe.equals(m.get("name")) || safe.equals(String.valueOf(m.get("id")).replace("_", "-")));
        return found;
    }

    /** 解析 JDBC MySQL URL 得到 host, port, database；非 MySQL 返回 null */
    public MysqlConnectionInfo parseMysqlConnection() {
        if (datasourceUrl == null || !datasourceUrl.startsWith("jdbc:mysql://")) return null;
        Matcher m = JDBC_MYSQL.matcher(datasourceUrl);
        if (!m.find()) return null;
        String host = m.group(1);
        int port = m.group(2).isEmpty() ? 3306 : Integer.parseInt(m.group(2));
        String database = m.group(3).trim();
        if (database.isEmpty()) return null;
        return new MysqlConnectionInfo(host, port, database, datasourceUsername != null ? datasourceUsername : "", datasourcePassword != null ? datasourcePassword : "");
    }

    /** 与 PHP backup 一致：执行 mysqldump 生成备份文件，仅支持 MySQL；返回 [成功, 消息或文件名] */
    public BackupResult doBackup() {
        MysqlConnectionInfo info = parseMysqlConnection();
        if (info == null) return new BackupResult(false, "仅支持 MySQL 数据源备份");
        String dir = getBackupPath();
        try {
            Files.createDirectories(Path.of(dir));
        } catch (IOException e) {
            return new BackupResult(false, "备份目录创建失败: " + e.getMessage());
        }
        String baseName = "backup-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String sqlPath = dir + baseName + ".sql";
        ProcessBuilder pb = new ProcessBuilder("mysqldump", "-h", info.host, "-P", String.valueOf(info.port), "-u", info.username, "--single-transaction", "--routines", "--triggers", info.database);
        pb.redirectErrorStream(true);
        if (info.password != null && !info.password.isEmpty()) {
            pb.environment().put("MYSQL_PWD", info.password);
        }
        try {
            Process p = pb.start();
            try (OutputStream out = Files.newOutputStream(Path.of(sqlPath))) {
                byte[] buf = new byte[8192];
                try (InputStream in = p.getInputStream()) {
                    int n;
                    while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                }
            }
            boolean ok = p.waitFor() == 0;
            if (!ok) Files.deleteIfExists(Path.of(sqlPath));
            return ok ? new BackupResult(true, baseName) : new BackupResult(false, "mysqldump 执行失败，请检查环境与权限");
        } catch (IOException e) {
            return new BackupResult(false, "mysqldump 未找到或执行失败: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new BackupResult(false, "备份被中断");
        }
    }

    /** 与 PHP backup restore 一致：执行 mysql 导入还原；仅支持 MySQL；fileName 为 list 中的 base 名 */
    public RestoreResult executeRestore(String fileName) {
        if (fileName == null || fileName.isBlank()) return new RestoreResult(false, "文件名无效");
        MysqlConnectionInfo info = parseMysqlConnection();
        if (info == null) return new RestoreResult(false, "仅支持 MySQL 数据源还原");
        String base = fileName.replace("..", "").replace("/", "").replace("\\", "").replace("_", "-");
        Path dir = Path.of(getBackupPath());
        if (!Files.isDirectory(dir)) return new RestoreResult(false, "备份目录不存在");
        Path sqlFile = null;
        try (Stream<Path> stream = Files.list(dir)) {
            for (Path p : stream.toList()) {
                String name = p.getFileName().toString();
                if (name.startsWith(base) && name.contains(".sql")) {
                    if (name.endsWith(".sql") && !name.endsWith(".sql.gz")) {
                        sqlFile = p;
                        break;
                    }
                    if (sqlFile == null) sqlFile = p;
                }
            }
        } catch (IOException e) {
            return new RestoreResult(false, "无法读取备份目录");
        }
        if (sqlFile == null || !Files.exists(sqlFile)) return new RestoreResult(false, "备份文件不存在");
        boolean isGz = sqlFile.toString().endsWith(".gz");
        try {
            Process p;
            if (isGz) {
                ProcessBuilder pgzip = new ProcessBuilder("gzip", "-dc", sqlFile.toAbsolutePath().toString());
                ProcessBuilder pmysql = new ProcessBuilder("mysql", "-h", info.host, "-P", String.valueOf(info.port), "-u", info.username, info.database);
                if (info.password != null && !info.password.isEmpty()) pmysql.environment().put("MYSQL_PWD", info.password);
                pmysql.redirectErrorStream(true);
                Process p1 = pgzip.start();
                pmysql.redirectInput(ProcessBuilder.Redirect.PIPE);
                p = pmysql.start();
                try (OutputStream mysqlIn = p.getOutputStream(); InputStream gzipOut = p1.getInputStream()) {
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = gzipOut.read(buf)) > 0) mysqlIn.write(buf, 0, n);
                }
                p1.destroy();
            } else {
                ProcessBuilder pb = new ProcessBuilder("mysql", "-h", info.host, "-P", String.valueOf(info.port), "-u", info.username, info.database);
                pb.redirectInput(ProcessBuilder.Redirect.from(sqlFile.toFile()));
                pb.redirectErrorStream(true);
                if (info.password != null && !info.password.isEmpty()) pb.environment().put("MYSQL_PWD", info.password);
                p = pb.start();
            }
            StringBuilder err = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) err.append(line).append("\n");
            }
            boolean ok = p.waitFor() == 0;
            return ok ? new RestoreResult(true, "") : new RestoreResult(false, err.length() > 0 ? err.toString().trim() : "mysql 执行失败");
        } catch (IOException e) {
            return new RestoreResult(false, (isGz ? "gzip/mysql" : "mysql") + " 未找到或执行失败: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RestoreResult(false, "还原被中断");
        }
    }

    public static final class MysqlConnectionInfo {
        public final String host, username, password;
        public final int port;
        public final String database;
        MysqlConnectionInfo(String host, int port, String database, String username, String password) {
            this.host = host; this.port = port; this.database = database; this.username = username; this.password = password;
        }
    }

    public static final class BackupResult {
        public final boolean success;
        public final String message;
        BackupResult(boolean success, String message) { this.success = success; this.message = message != null ? message : ""; }
    }

    public static final class RestoreResult {
        public final boolean success;
        public final String message;
        RestoreResult(boolean success, String message) { this.success = success; this.message = message != null ? message : ""; }
    }

    /** 与 PHP backup delete 一致：从备份目录删除指定备份文件（base 名，可匹配 .sql/.sql.gz 等） */
    public boolean deleteBackup(String fileName) {
        if (fileName == null || fileName.isBlank()) return false;
        String base = fileName.replace("..", "").replace("/", "").replace("\\", "").replace("_", "-");
        Path path = Path.of(getBackupPath());
        if (!Files.isDirectory(path)) return false;
        boolean deleted = false;
        try (Stream<Path> stream = Files.list(path)) {
            for (Path p : stream.toList()) {
                String name = p.getFileName().toString();
                if (name.startsWith(base) && name.contains(".sql")) {
                    Files.deleteIfExists(p);
                    deleted = true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return deleted;
    }

    private String getSettingValue(String key) {
        return configRepository.findByOwnerAndModuleAndSectionAndKey_(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION, key)
                .map(Config::getValue)
                .orElse(null);
    }
}
