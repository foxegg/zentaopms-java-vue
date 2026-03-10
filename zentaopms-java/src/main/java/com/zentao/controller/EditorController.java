package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP editor 一致：模块文件编辑入口，index 返回 tab 与 moduleTree 形状 */
@RestController
@RequestMapping("/api/editor")
public class EditorController {

    @GetMapping({ "/index", "/list", "" })
    public ResponseEntity<Map<String, Object>> index(@RequestParam(required = false, defaultValue = "editor") String type) {
        Map<String, Object> data = Map.of(
                "tab", type,
                "moduleTree", java.util.List.of()
        );
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
