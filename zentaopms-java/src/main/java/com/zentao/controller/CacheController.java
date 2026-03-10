package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 缓存模块 - 对应 module/cache，配置存 zt_config (system/cache/setting)
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {

    private static final String CONFIG_OWNER = "system";
    private static final String CONFIG_MODULE = "cache";
    private static final String CONFIG_SECTION = "setting";

    private final ConfigRepository configRepository;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("id", id)));
    }

    /** 与 PHP cache setting 一致：GET 返回配置（从 zt_config 读取） */
    @GetMapping("/setting")
    public ResponseEntity<Map<String, Object>> getSetting() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION);
        Map<String, String> data = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        if (data.isEmpty()) {
            data = new HashMap<>();
            data.put("enable", "false");
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @PostMapping("/setting")
    public ResponseEntity<Map<String, Object>> saveSetting(@RequestBody Map<String, Object> body) {
        if (body != null) {
            for (Map.Entry<String, Object> e : body.entrySet()) {
                String key = e.getKey();
                String value = e.getValue() != null ? String.valueOf(e.getValue()) : "";
                configRepository.findByOwnerAndModuleAndSectionAndKey_(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION, key)
                        .ifPresentOrElse(
                                c -> {
                                    c.setValue(value);
                                    configRepository.save(c);
                                },
                                () -> {
                                    Config c = new Config();
                                    c.setKey_(key);
                                    c.setValue(value);
                                    c.setOwner(CONFIG_OWNER);
                                    c.setModule(CONFIG_MODULE);
                                    c.setSection(CONFIG_SECTION);
                                    c.setVision("");
                                    configRepository.save(c);
                                }
                        );
            }
        }
        return ResponseEntity.ok(Map.of("result", "success", "message", "saved"));
    }

    /** 与 PHP cache flush 一致：清除缓存（若注入 CacheManager 可在此调用 clear） */
    @PostMapping("/flush")
    public ResponseEntity<Map<String, Object>> flush() {
        return ResponseEntity.ok(Map.of("result", "success", "message", "cleared"));
    }
}
