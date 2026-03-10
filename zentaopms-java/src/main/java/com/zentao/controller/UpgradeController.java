package com.zentao.controller;

import com.zentao.config.ZentaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP upgrade 一致：升级模块提供当前版本等只读信息 */
@RestController
@RequestMapping("/api/upgrade")
@RequiredArgsConstructor
public class UpgradeController {

    private final ZentaoProperties zentaoProperties;

    @GetMapping({ "/index", "" })
    public ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of(
                        "version", zentaoProperties.getVersion() != null ? zentaoProperties.getVersion() : "22.0.0"
                )
        ));
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
