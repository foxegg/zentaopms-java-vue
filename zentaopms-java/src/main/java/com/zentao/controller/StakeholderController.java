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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam int objectID,
            @RequestParam(defaultValue = "project") String objectType) {
        List<Stakeholder> list = stakeholderService.getByObject(objectID, objectType);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        stakeholder.setId(id);
        stakeholderService.update(stakeholder);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        stakeholderService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
