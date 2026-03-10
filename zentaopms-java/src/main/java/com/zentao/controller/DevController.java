package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP dev 一致：开发工具 API/数据库结构查询，返回模块树/表树形状 */
@RestController
@RequestMapping("/api/dev")
public class DevController {

    @GetMapping({ "/api", "/list", "" })
    public ResponseEntity<Map<String, Object>> api(@RequestParam(required = false) String module) {
        Map<String, Object> data = new java.util.HashMap<>(Map.of(
                "tab", "api",
                "selectedModule", module != null ? module : "",
                "apis", java.util.List.of(),
                "moduleTree", java.util.List.of()
        ));
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/restAPI")
    public ResponseEntity<Map<String, Object>> restAPI(@RequestParam(required = false, defaultValue = "1") int apiID) {
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of("apiID", apiID, "api", (Object) null, "typeList", java.util.List.of(), "moduleTree", java.util.List.of())
        ));
    }

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> db(@RequestParam(required = false) String table) {
        Map<String, Object> data = new java.util.HashMap<>(Map.of(
                "tab", "db",
                "selectedTable", table != null ? table : "",
                "tableTree", java.util.List.of(),
                "fields", java.util.List.of()
        ));
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
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
