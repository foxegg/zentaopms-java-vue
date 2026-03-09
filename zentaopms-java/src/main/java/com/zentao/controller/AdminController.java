package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("message", "safe check placeholder")));
    }

    @GetMapping("/log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> log(
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of(), "pager", Map.of("pageID", pageID, "recPerPage", recPerPage)));
    }
}
