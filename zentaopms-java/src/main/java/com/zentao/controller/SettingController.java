package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设置模块 - 对应 module/setting，代理到 config
 */
@RestController
@RequestMapping("/api/setting")
@RequiredArgsConstructor
public class SettingController {

    private final ConfigRepository configRepository;

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig(
            @RequestParam(defaultValue = "system") String owner,
            @RequestParam(defaultValue = "common") String module,
            @RequestParam(defaultValue = "global") String section) {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection(owner, module, section);
        Map<String, String> map = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        return ResponseEntity.ok(Map.of("result", "success", "data", map));
    }

    @PutMapping("/config")
    public ResponseEntity<Map<String, Object>> saveConfig(
            @RequestParam(defaultValue = "system") String owner,
            @RequestParam(defaultValue = "common") String module,
            @RequestParam(defaultValue = "global") String section,
            @RequestBody Map<String, String> configs) {
        for (Map.Entry<String, String> e : configs.entrySet()) {
            configRepository.findByOwnerAndModuleAndSectionAndKey_(owner, module, section, e.getKey())
                    .ifPresentOrElse(
                            c -> {
                                c.setValue(e.getValue());
                                configRepository.save(c);
                            },
                            () -> {
                                Config c = new Config();
                                c.setKey_(e.getKey());
                                c.setValue(e.getValue());
                                c.setOwner(owner);
                                c.setModule(module);
                                c.setSection(section);
                                c.setVision("");
                                configRepository.save(c);
                            }
                    );
        }
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
