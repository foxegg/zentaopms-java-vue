package com.zentao.controller;

import com.zentao.entity.Webhook;
import com.zentao.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Webhook 模块 - 对应 module/webhook
 */
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list() {
        List<Webhook> webhooks = webhookService.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", webhooks));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return webhookService.getById(id)
                .map(w -> ResponseEntity.ok(Map.of("result", "success", "data", w)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Webhook webhook) {
        Webhook created = webhookService.create(webhook);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Webhook webhook) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (webhookService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        webhook.setId(id);
        webhookService.update(webhook);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (webhookService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        webhookService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> log(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }
}
