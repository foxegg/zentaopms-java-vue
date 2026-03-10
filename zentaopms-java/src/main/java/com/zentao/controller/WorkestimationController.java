package com.zentao.controller;

import com.zentao.entity.WorkEstimation;
import com.zentao.service.WorkEstimationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP workestimation 一致：zt_workestimation 列表/按 project 预算/查看/创建/编辑/删除 */
@RestController
@RequestMapping("/api/workestimation")
@RequiredArgsConstructor
public class WorkestimationController {

    private final WorkEstimationService workEstimationService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false) Integer project) {
        List<WorkEstimation> list = (project != null && project > 0)
                ? workEstimationService.getByProject(project)
                : workEstimationService.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 按项目取预算，与 PHP getBudget(projectID) 一致；project≤0 时直接返回 data:null */
    @GetMapping("/budget")
    public ResponseEntity<Map<String, Object>> budget(@RequestParam(required = false, defaultValue = "0") int project) {
        if (project <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", (Object) null));
        return workEstimationService.getBudget(project)
                .map(b -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.ok(Map.of("result", "success", "data", (Object) null)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return workEstimationService.getById(id)
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody WorkEstimation body) {
        body.setId(null);
        WorkEstimation created = workEstimationService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody WorkEstimation body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (workEstimationService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        body.setId(id);
        body.setDeleted(0);
        workEstimationService.update(body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (workEstimationService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        workEstimationService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
