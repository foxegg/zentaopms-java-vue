package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", java.util.List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("id", id)));
    }

    /** 与 PHP cache setting 一致：GET 返回配置，POST 保存 */
    @GetMapping("/setting")
    public ResponseEntity<Map<String, Object>> getSetting() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("enable", false)));
    }

    @PostMapping("/setting")
    public ResponseEntity<Map<String, Object>> saveSetting(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("result", "success", "message", "saved"));
    }

    /** 与 PHP cache flush 一致：清除缓存 */
    @PostMapping("/flush")
    public ResponseEntity<Map<String, Object>> flush() {
        return ResponseEntity.ok(Map.of("result", "success", "message", "cleared"));
    }
}
