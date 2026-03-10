package com.zentao.controller;

import com.zentao.config.ZentaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP install 一致：安装向导仅提供只读检查；index 返回版本与安装状态 */
@RestController
@RequestMapping("/api/install")
@RequiredArgsConstructor
public class InstallController {

    private final ZentaoProperties zentaoProperties;

    @GetMapping({ "/index", "" })
    public ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of(
                        "version", zentaoProperties.getVersion() != null ? zentaoProperties.getVersion() : "22.0.0",
                        "installed", true
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
