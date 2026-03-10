package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP custom 一致：自定义字段 set、restore 接口形状 */
@RestController
@RequestMapping("/api/custom")
public class CustomController {

    @GetMapping({ "/set", "/list", "" })
    public ResponseEntity<Map<String, Object>> set(
            @RequestParam(required = false, defaultValue = "story") String module,
            @RequestParam(required = false, defaultValue = "priList") String field,
            @RequestParam(required = false) String lang) {
        String currentLang = lang != null && !lang.isBlank() ? lang : "zh-cn";
        Map<String, Object> data = Map.of(
                "module", module, "field", field, "currentLang", currentLang, "canAdd", true);
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @PostMapping("/restore")
    public ResponseEntity<Map<String, Object>> restore(
            @RequestParam String module, @RequestParam String field) {
        return ResponseEntity.ok(Map.of("result", "success", "load", true));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
