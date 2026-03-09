package com.zentao.controller;

import com.zentao.entity.TestRun;
import com.zentao.entity.TestTask;
import com.zentao.service.TestTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 测试单模块 - 对应 module/testtask
 */
@RestController
@RequestMapping("/api/testtask")
@RequiredArgsConstructor
public class TestTaskController {

    private final TestTaskService testTaskService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer execution) {
        List<TestTask> tasks;
        if (execution != null && execution > 0) {
            tasks = testTaskService.getByExecution(execution);
        } else if (project != null && project > 0) {
            tasks = testTaskService.getByProject(project);
        } else if (product != null && product > 0) {
            tasks = testTaskService.getByProduct(product);
        } else {
            tasks = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return testTaskService.getById(id)
                .map(t -> ResponseEntity.ok(Map.of("result", "success", "data", t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody TestTask task) {
        TestTask created = testTaskService.create(task);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody TestTask task) {
        task.setId(id);
        testTaskService.update(task);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        TestTask t = testTaskService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        TestTask t = testTaskService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Map<String, Object>> block(@PathVariable int id) {
        TestTask t = testTaskService.block(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        TestTask t = testTaskService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        testTaskService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<Map<String, Object>> results(@PathVariable int id) {
        java.util.List<TestRun> list = testTaskService.getResults(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/linkCase")
    public ResponseEntity<Map<String, Object>> linkCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        int caseID = body != null && body.get("caseID") instanceof Number n ? n.intValue() : 0;
        String assignedTo = body != null && body.get("assignedTo") != null ? body.get("assignedTo").toString() : null;
        TestRun run = testTaskService.linkCase(id, caseID, assignedTo);
        return ResponseEntity.ok(Map.of("result", "success", "data", run));
    }

    @PostMapping("/{id}/unlinkCase")
    public ResponseEntity<Map<String, Object>> unlinkCase(@PathVariable int id, @RequestParam int caseID) {
        testTaskService.unlinkCase(id, caseID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/batchUnlinkCases")
    public ResponseEntity<Map<String, Object>> batchUnlinkCases(@PathVariable int id, @RequestBody Map<String, Object> body) {
        java.util.List<Integer> caseIds = body != null && body.get("caseIds") instanceof java.util.List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : java.util.List.of();
        testTaskService.batchUnlinkCases(id, caseIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/runCase")
    public ResponseEntity<Map<String, Object>> runCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        int caseID = body != null && body.get("caseID") instanceof Number n ? n.intValue() : 0;
        String result = body != null && body.get("result") != null ? body.get("result").toString() : "n/a";
        TestRun run = testTaskService.runCase(id, caseID, result);
        return ResponseEntity.ok(Map.of("result", "success", "data", run));
    }
}
