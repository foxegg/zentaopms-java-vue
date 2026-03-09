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
 * 消息配置模块 - 对应 module/message
 */
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final ConfigRepository configRepository;

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getConfig() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection("system", "message", "browser");
        Map<String, String> map = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        return ResponseEntity.ok(Map.of("result", "success", "data", map));
    }

    @PutMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveConfig(@RequestBody Map<String, String> configs) {
        for (Map.Entry<String, String> e : configs.entrySet()) {
            configRepository.findByOwnerAndModuleAndSectionAndKey_("system", "message", "browser", e.getKey())
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
                                c.setModule("message");
                                c.setSection("browser");
                                c.setVision("");
                                configRepository.save(c);
                            }
                    );
        }
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
