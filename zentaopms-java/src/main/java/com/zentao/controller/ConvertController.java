package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP convert 一致：数据导入入口，index 返回当前步骤状态（API 无 session 时为空状态） */
@RestController
@RequestMapping("/api/convert")
public class ConvertController {

    @GetMapping({ "/index", "/list", "" })
    public ResponseEntity<Map<String, Object>> index(@RequestParam(required = false) String mode) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("mode", mode != null ? mode : "");
        data.put("stepStatus", Map.of());
        data.put("jiraRelation", (Object) null);
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/selectSource")
    public ResponseEntity<Map<String, Object>> selectSource() {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("sources", java.util.List.of())));
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
