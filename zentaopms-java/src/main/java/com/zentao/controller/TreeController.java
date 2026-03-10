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

    /** 与 PHP tree browse 一致；rootID≤0 时返回空列表 */
    @GetMapping("/browse")
    public ResponseEntity<Map<String, Object>> browse(
            @RequestParam(required = false, defaultValue = "0") int rootID,
            @RequestParam(required = false) String viewType) {
        List<Module> modules = rootID <= 0 ? List.of() : moduleService.getByRoot(rootID, viewType != null ? viewType : "");
        return ResponseEntity.ok(Map.of("result", "success", "data", modules));
    }

    /** 与 PHP tree getOptionMenu 一致；rootID≤0 时返回 {0: "/"} */
    @GetMapping("/optionMenu")
    public ResponseEntity<Map<String, Object>> optionMenu(
            @RequestParam(required = false, defaultValue = "0") int rootID,
            @RequestParam(required = false, defaultValue = "story") String type) {
        return ResponseEntity.ok(Map.of("result", "success", "data", moduleService.getOptionMenu(rootID, type)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
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
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        module.setId(id);
        moduleService.update(module);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        moduleService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<Map<String, Object>> updateOrder(@RequestBody List<Map<String, Object>> orders) {
        moduleService.updateOrder(orders != null ? orders : List.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 修复模块 path/grade，与 PHP tree fix 一致；rootID≤0 时跳过 */
    @PostMapping("/fix")
    public ResponseEntity<Map<String, Object>> fix(
            @RequestParam(required = false, defaultValue = "0") int rootID,
            @RequestParam(defaultValue = "story") String type) {
        if (rootID > 0) moduleService.fix(rootID, type != null ? type : "story");
        return ResponseEntity.ok(Map.of("result", "success", "message", "fixed"));
    }
}
