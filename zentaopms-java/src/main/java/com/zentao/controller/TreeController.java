package com.zentao.controller;

import com.zentao.entity.Module;
import com.zentao.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 模块树模块 - 对应 module/tree
 */
@RestController
@RequestMapping("/api/tree")
@RequiredArgsConstructor
public class TreeController {

    private final ModuleService moduleService;

    @GetMapping("/browse")
    public ResponseEntity<Map<String, Object>> browse(
            @RequestParam int rootID,
            @RequestParam(required = false) String viewType) {
        List<Module> modules = moduleService.getByRoot(rootID, viewType != null ? viewType : "");
        return ResponseEntity.ok(Map.of("result", "success", "data", modules));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return moduleService.getById(id)
                .map(m -> ResponseEntity.ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Module module) {
        Module created = moduleService.create(module);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Module module) {
        module.setId(id);
        moduleService.update(module);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        moduleService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<Map<String, Object>> updateOrder(@RequestBody List<Map<String, Object>> orders) {
        moduleService.updateOrder(orders != null ? orders : List.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/fix")
    public ResponseEntity<Map<String, Object>> fix(@RequestParam(required = false) Integer rootID) {
        return ResponseEntity.ok(Map.of("result", "success", "message", "fix placeholder"));
    }
}
