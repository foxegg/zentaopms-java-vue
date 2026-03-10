package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 与 PHP sso 一致：配置存 zt_config(system/sso/setting)：turnon、addr、code、key */
@RestController
@RequestMapping("/api/sso")
@RequiredArgsConstructor
public class SsoController {

    private static final String CONFIG_OWNER = "system";
    private static final String CONFIG_MODULE = "sso";
    private static final String CONFIG_SECTION = "setting";

    private final ConfigRepository configRepository;

    @GetMapping({ "/list", "" })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list() {
        Map<String, String> data = getSettingMap();
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSetting() {
        return ResponseEntity.ok(Map.of("result", "success", "data", getSettingMap()));
    }

    @PutMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveSetting(@RequestBody Map<String, Object> body) {
        if (body != null) {
            for (String key : new String[]{"turnon", "addr", "code", "key"}) {
                if (!body.containsKey(key)) continue;
                String value = body.get(key) != null ? String.valueOf(body.get(key)) : "";
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
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("id", id)));
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) {
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private Map<String, String> getSettingMap() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection(CONFIG_OWNER, CONFIG_MODULE, CONFIG_SECTION);
        Map<String, String> data = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        if (data.isEmpty()) {
            data = new HashMap<>();
            data.put("turnon", "0");
            data.put("addr", "");
            data.put("code", "");
            data.put("key", "");
        }
        return data;
    }
}
