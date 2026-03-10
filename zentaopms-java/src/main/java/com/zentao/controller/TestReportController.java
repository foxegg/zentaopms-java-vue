package com.zentao.controller;

import com.zentao.entity.TestReport;
import com.zentao.service.TestReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 测试报告模块 - 对应 module/testreport
 */
@RestController
@RequestMapping("/api/testreport")
@RequiredArgsConstructor
public class TestReportController {

    private final TestReportService testReportService;

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "0") int product,
            @RequestParam(defaultValue = "0") int appendID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", testReportService.getPairs(product, appendID)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", testReportService.getPairsByList(idList)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer execution) {
        List<TestReport> reports;
        if (execution != null && execution > 0) {
            reports = testReportService.getByExecution(execution);
        } else if (project != null && project > 0) {
            reports = testReportService.getByProject(project);
        } else if (product != null && product > 0) {
            reports = testReportService.getByProduct(product);
        } else {
            reports = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", reports));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return testReportService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody TestReport report) {
        TestReport created = testReportService.create(report);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody TestReport report) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testReportService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        report.setId(id);
        testReportService.update(report);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testReportService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testReportService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
