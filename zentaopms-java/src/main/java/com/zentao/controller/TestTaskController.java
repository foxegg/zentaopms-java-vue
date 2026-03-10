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

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "0") int product,
            @RequestParam(defaultValue = "0") int execution) {
        return ResponseEntity.ok(Map.of("result", "success", "data", testTaskService.getPairs(product, execution)));
    }

    @GetMapping("/pairsByList")
    public ResponseEntity<Map<String, Object>> pairsByList(@RequestParam(required = false) String ids) {
        List<Integer> idList = ids != null && !ids.isBlank()
                ? java.util.Arrays.stream(ids.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(s -> { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; } })
                        .filter(i -> i != null && i > 0)
                        .distinct()
                        .toList()
                : List.of();
        return ResponseEntity.ok(Map.of("result", "success", "data", testTaskService.getPairsByList(idList)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer execution,
            @RequestParam(required = false, defaultValue = "all") String status) {
        List<TestTask> tasks;
        if (execution != null && execution > 0) {
            tasks = testTaskService.getByExecution(execution);
        } else if (project != null && project > 0) {
            tasks = testTaskService.getByProject(project);
        } else if (product != null && product > 0) {
            tasks = testTaskService.getByProduct(product);
        } else {
            tasks = testTaskService.getList();
        }
        if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
            tasks = tasks.stream().filter(t -> status.equalsIgnoreCase(t.getStatus())).toList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
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
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        task.setId(id);
        testTaskService.update(task);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        TestTask t = testTaskService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        TestTask t = testTaskService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Map<String, Object>> block(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        TestTask t = testTaskService.block(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        TestTask t = testTaskService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testTaskService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/results")
    public ResponseEntity<Map<String, Object>> results(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", java.util.List.<TestRun>of()));
        java.util.List<TestRun> list = testTaskService.getResults(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 测试单关联的用例（执行记录），与 PHP testtask cases 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/cases")
    public ResponseEntity<Map<String, Object>> cases(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", java.util.List.<TestRun>of()));
        java.util.List<TestRun> list = testTaskService.getResults(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/linkCase")
    public ResponseEntity<Map<String, Object>> linkCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int caseID = body != null && body.get("caseID") instanceof Number n ? n.intValue() : 0;
        if (caseID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid caseID"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String assignedTo = body != null && body.get("assignedTo") != null ? body.get("assignedTo").toString() : null;
        TestRun run = testTaskService.linkCase(id, caseID, assignedTo);
        return ResponseEntity.ok(Map.of("result", "success", "data", run));
    }

    @PostMapping("/{id}/unlinkCase")
    public ResponseEntity<Map<String, Object>> unlinkCase(@PathVariable int id, @RequestParam int caseID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (caseID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid caseID"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testTaskService.unlinkCase(id, caseID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/batchUnlinkCases")
    public ResponseEntity<Map<String, Object>> batchUnlinkCases(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        java.util.List<Integer> caseIds = body != null && body.get("caseIds") instanceof java.util.List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : java.util.List.of();
        testTaskService.batchUnlinkCases(id, caseIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量指派测试单中的用例，与 PHP testtask batchAssign 一致；id≤0 返回 400 */
    @PostMapping("/{id}/batchAssign")
    public ResponseEntity<Map<String, Object>> batchAssign(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String assignedTo = body != null && body.get("assignedTo") != null ? body.get("assignedTo").toString() : "";
        java.util.List<Integer> caseIds = body != null && body.get("caseIds") instanceof java.util.List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : java.util.List.of();
        testTaskService.batchAssign(id, assignedTo, caseIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/runCase")
    public ResponseEntity<Map<String, Object>> runCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int caseID = body != null && body.get("caseID") instanceof Number n ? n.intValue() : 0;
        if (caseID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid caseID"));
        if (testTaskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String result = body != null && body.get("result") != null ? body.get("result").toString() : "n/a";
        TestRun run = testTaskService.runCase(id, caseID, result);
        return ResponseEntity.ok(Map.of("result", "success", "data", run));
    }
}
