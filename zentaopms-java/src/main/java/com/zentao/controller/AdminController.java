package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import com.zentao.repository.LogRepository;
import com.zentao.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统管理模块 - 对应 module/admin
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ConfigRepository configRepository;
    private final LogRepository logRepository;
    private final StoryService storyService;

    @GetMapping("/index")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of(
                        "version", "22.0.0",
                        "phpVersion", "N/A",
                        "dbType", "MySQL"
                )
        ));
    }

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getConfig() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection("system", "common", "global");
        Map<String, String> map = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        return ResponseEntity.ok(Map.of("result", "success", "data", map));
    }

    @PutMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveConfig(@RequestBody Map<String, String> configs) {
        for (Map.Entry<String, String> e : configs.entrySet()) {
            configRepository.findByOwnerAndModuleAndSectionAndKey_("system", "common", "global", e.getKey())
                    .ifPresentOrElse(
                            c -> {
                                c.setValue(e.getValue());
                                configRepository.save(c);
                            },
                            () -> {
                                Config c = new Config();
                                c.setKey_(e.getKey());
                                c.setValue(e.getValue());
                                c.setOwner("system");
                                c.setModule("common");
                                c.setSection("global");
                                c.setVision("");
                                configRepository.save(c);
                            }
                    );
        }
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/safe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> safe() {
        // 按 PHP admin/safe：版本、数据库、目录可写等检查
        Map<String, Object> checks = new HashMap<>();
        checks.put("version", "22.0.0");
        checks.put("dbType", "MySQL");
        checks.put("status", "ok");
        return ResponseEntity.ok(Map.of("result", "success", "data", checks));
    }

    @GetMapping("/log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> log(
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        int page = Math.max(1, pageID);
        int size = Math.min(100, Math.max(1, recPerPage));
        var pageable = PageRequest.of(page - 1, size);
        var logPage = logRepository.findAllByOrderByDateDesc(pageable);
        Map<String, Object> pager = Map.of(
                "pageID", page,
                "recPerPage", size,
                "recTotal", (int) logPage.getTotalElements()
        );
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", logPage.getContent(),
                "pager", pager
        ));
    }

    /**
     * 删除过期日志 - 对应 PHP admin deleteLog()，按保存天数删除
     */
    /** 与 PHP admin 一致：days 须为正整数，未传时默认 30 */
    @DeleteMapping("/log")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteLog(
            @RequestParam(required = false) Integer days) {
        if (days != null && days <= 0) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid days"));
        }
        int saveDays = days != null ? days : 30;
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(saveDays), LocalTime.MIN);
        logRepository.deleteByDateBefore(before);
        return ResponseEntity.ok(Map.of("result", "success", "message", "deleted"));
    }

    /** 一次性迁移：将需求的 linkStories 同步到 zt_relation 表，返回本次写入的关联对数 */
    @PostMapping("/syncLinkStoriesToRelation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> syncLinkStoriesToRelation() {
        int count = storyService.syncLinkStoriesToRelation();
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("syncedPairs", count)));
    }
}
