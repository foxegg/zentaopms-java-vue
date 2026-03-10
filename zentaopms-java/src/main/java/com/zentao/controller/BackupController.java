package com.zentao.controller;

import com.zentao.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 备份模块 - 对应 module/backup；list/setting/diskSpace 与 PHP 对齐
 */
@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    /** 与 PHP backup index 一致：从备份目录扫描 *.sql* 返回列表 */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", backupService.getBackupList()));
    }

    /** 与 PHP backup 一致：执行 mysqldump 生成备份文件（仅 MySQL），返回 result + message（成功时为备份 base 名） */
    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> backup() {
        var result = backupService.doBackup();
        return ResponseEntity.ok(Map.of("result", result.success ? "success" : "fail", "message", result.message));
    }

    /** 与 PHP backup restore 一致：body 传 fileName/backupFile，校验存在后执行 mysql 导入还原（仅 MySQL） */
    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> restore(@RequestBody Map<String, String> body) {
        String fileName = body != null ? body.get("fileName") : null;
        if (fileName == null && body != null) fileName = body.get("backupFile");
        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.ok(Map.of("result", "fail", "message", "请传 fileName 或 backupFile"));
        }
        if (!backupService.restore(fileName)) {
            return ResponseEntity.ok(Map.of("result", "fail", "message", "备份不存在或文件名无效"));
        }
        var result = backupService.executeRestore(fileName);
        return ResponseEntity.ok(Map.of("result", result.success ? "success" : "fail", "message", result.message));
    }

    /** 与 PHP backup delete 一致：按 fileName（base 名或 id，如 2025_01_01）删除备份目录下对应 *.sql* 文件 */
    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String fileName) {
        boolean ok = backupService.deleteBackup(fileName);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail", "message", ok ? "" : "文件不存在或已删除"));
    }

    @GetMapping("/diskSpace")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> diskSpace() {
        return ResponseEntity.ok(Map.of("result", "success", "data", backupService.getDiskSpace()));
    }

    @GetMapping("/progress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> progress() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("progress", 0)));
    }

    @GetMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSetting() {
        return ResponseEntity.ok(Map.of("result", "success", "data", backupService.getSetting()));
    }

    @PutMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveSetting(@RequestBody Map<String, Object> body) {
        backupService.saveSetting(body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
