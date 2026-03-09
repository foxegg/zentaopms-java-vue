package com.zentao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 备份模块 - 对应 module/backup，基础 API 占位
 */
@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class BackupController {

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }

    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> backup() {
        return ResponseEntity.ok(Map.of("result", "success", "message", "backup placeholder"));
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> restore(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(Map.of("result", "success", "message", "restore placeholder"));
    }

    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String fileName) {
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/diskSpace")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> diskSpace() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("free", 0L, "total", 0L)));
    }

    @GetMapping("/progress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> progress() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("progress", 0)));
    }

    @GetMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSetting() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of()));
    }

    @PutMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> saveSetting(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
