package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import com.zentao.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final NotifyService notifyService;

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

    /** 与 PHP mail browse 一致：zt_notify objectType=mail 分页列表；status=all/wait/sent/fail */
    @GetMapping("/browse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> browse(
            @RequestParam(defaultValue = "id_desc") String orderBy,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "0") int recTotal,
            @RequestParam(defaultValue = "100") int recPerPage,
            @RequestParam(defaultValue = "1") int pageID) {
        var page = notifyService.getMailQueue(status, PageRequest.of(Math.max(0, pageID - 1), Math.min(100, Math.max(1, recPerPage))));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of(
                        "recTotal", page.getTotalElements(),
                        "recPerPage", page.getSize(),
                        "pageID", pageID
                )
        ));
    }
}
