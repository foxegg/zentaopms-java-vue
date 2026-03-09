package com.zentao.controller;

import com.zentao.entity.Extension;
import com.zentao.service.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 扩展/插件模块 - 对应 module/extension
 */
@RestController
@RequestMapping("/api/extension")
@RequiredArgsConstructor
public class ExtensionController {

    private final ExtensionService extensionService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "installed") String status) {
        List<Extension> list = "all".equals(status)
                ? extensionService.getAll()
                : extensionService.getByStatus(status);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return extensionService.getById(id)
                .map(e -> ResponseEntity.ok(Map.of("result", "success", "data", e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Extension extension) {
        Extension created = extensionService.create(extension);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Extension extension) {
        extension.setId(id);
        extensionService.update(extension);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        extensionService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
