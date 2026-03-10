package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP transfer 一致：数据导入导出入口，index 返回配置形状 */
@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @GetMapping({ "/index", "/list", "" })
    public ResponseEntity<Map<String, Object>> index() {
        Map<String, Object> data = Map.of(
                "maxImport", 0,
                "templateFields", java.util.List.of(),
                "exportFields", java.util.List.of(),
                "moduleConfig", (Object) null
        );
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) {
        return PlaceholderResponses.emptyView(id);
    }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
