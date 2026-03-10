package com.zentao.controller;

import com.zentao.entity.Stakeholder;
import com.zentao.service.StakeholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 干系人模块 - 对应 module/stakeholder
 */
@RestController
@RequestMapping("/api/stakeholder")
@RequiredArgsConstructor
public class StakeholderController {

    private final StakeholderService stakeholderService;

    /** 与 PHP 一致；objectID≤0 时返回 data: [] */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "0") int objectID,
            @RequestParam(defaultValue = "project") String objectType) {
        if (objectID <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Stakeholder>of()));
        List<Stakeholder> list = stakeholderService.getByObject(objectID, objectType);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return stakeholderService.getById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Stakeholder stakeholder) {
        Stakeholder created = stakeholderService.create(stakeholder);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Stakeholder stakeholder) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (stakeholderService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        stakeholder.setId(id);
        stakeholderService.update(stakeholder);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (stakeholderService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        stakeholderService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
