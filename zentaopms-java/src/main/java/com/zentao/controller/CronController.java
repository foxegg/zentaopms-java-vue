package com.zentao.controller;

import com.zentao.entity.Cron;
import com.zentao.service.CronService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定时任务模块 - 对应 module/cron
 */
@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {

    private final CronService cronService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list() {
        List<Cron> list = cronService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return cronService.getById(id)
                .map(c -> ResponseEntity.ok(Map.of("result", "success", "data", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Cron cron) {
        Cron created = cronService.create(cron);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Cron cron) {
        cron.setId(id);
        cronService.update(cron);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        cronService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> toggle(@PathVariable int id, @RequestBody Map<String, String> body) {
        Cron c = cronService.toggle(id, body != null ? body.get("status") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", c));
    }

    @PostMapping("/{id}/run")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> run(@PathVariable int id) {
        Cron c = cronService.run(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", c));
    }
}
