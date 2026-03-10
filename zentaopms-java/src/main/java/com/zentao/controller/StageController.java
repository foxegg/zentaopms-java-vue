package com.zentao.controller;

import com.zentao.entity.Stage;
import com.zentao.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 阶段模块 - 对应 module/stage
 */
@RestController
@RequestMapping("/api/stage")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "0") int groupID) {
        List<Stage> stages = groupID > 0
                ? stageService.getByWorkflowGroup(groupID)
                : stageService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", stages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return stageService.getById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Stage stage) {
        Stage created = stageService.create(stage);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Stage stage) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (stageService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        stage.setId(id);
        stageService.update(stage);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (stageService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        stageService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
