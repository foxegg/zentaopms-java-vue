package com.zentao.controller;

import com.zentao.entity.SuiteCase;
import com.zentao.entity.TestSuite;
import com.zentao.service.TestSuiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 测试套件模块 - 对应 module/testsuite
 */
@RestController
@RequestMapping("/api/testsuite")
@RequiredArgsConstructor
public class TestSuiteController {

    private final TestSuiteService testSuiteService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project) {
        List<TestSuite> suites;
        if (project != null && project > 0) {
            suites = testSuiteService.getByProject(project);
        } else if (product != null && product > 0) {
            suites = testSuiteService.getByProduct(product);
        } else {
            suites = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", suites));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return testSuiteService.getById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/cases")
    public ResponseEntity<Map<String, Object>> cases(@PathVariable int id) {
        List<SuiteCase> cases = testSuiteService.getCasesBySuite(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", cases));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody TestSuite suite) {
        TestSuite created = testSuiteService.create(suite);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody TestSuite suite) {
        suite.setId(id);
        testSuiteService.update(suite);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/addCase")
    public ResponseEntity<Map<String, Object>> addCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        int caseId = body.get("caseId") instanceof Number n ? n.intValue() : 0;
        int productId = body.get("productId") instanceof Number n ? n.intValue() : 0;
        testSuiteService.addCase(id, caseId, productId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}/removeCase/{caseId}")
    public ResponseEntity<Map<String, Object>> removeCase(@PathVariable int id, @PathVariable int caseId) {
        testSuiteService.removeCase(id, caseId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        testSuiteService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
