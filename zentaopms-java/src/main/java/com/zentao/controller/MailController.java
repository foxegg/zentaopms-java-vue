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
 * 邮件配置模块 - 对应 module/mail
 */
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final ConfigRepository configRepository;

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getConfig() {
        List<Config> configs = configRepository.findByOwnerAndModuleAndSection("system", "mail", "smtp");
        Map<String, String> map = configs.stream()
                .filter(c -> c.getKey_() != null && c.getValue() != null)
                .collect(Collectors.toMap(Config::getKey_, Config::getValue, (a, b) -> b));
        return ResponseEntity.ok(Map.of("result", "success", "data", map));
    }

    @PutMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveConfig(@RequestBody Map<String, String> configs) {
        for (Map.Entry<String, String> e : configs.entrySet()) {
            configRepository.findByOwnerAndModuleAndSectionAndKey_("system", "mail", "smtp", e.getKey())
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
                                c.setModule("mail");
                                c.setSection("smtp");
                                c.setVision("");
                                configRepository.save(c);
                            }
                    );
        }
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> test(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(Map.of("result", "success", "message", "test mail placeholder"));
    }

    @GetMapping("/browse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> browse() {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }
}
