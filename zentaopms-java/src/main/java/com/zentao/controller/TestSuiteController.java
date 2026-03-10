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

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", testSuiteService.getPairs(product)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", testSuiteService.getPairsByList(idList)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false, defaultValue = "all") String type) {
        List<TestSuite> suites;
        if (project != null && project > 0) {
            suites = testSuiteService.getByProject(project);
        } else if (product != null && product > 0) {
            suites = testSuiteService.getByProduct(product);
        } else {
            suites = testSuiteService.getList();
        }
        if (type != null && !type.isEmpty() && !"all".equalsIgnoreCase(type)) {
            suites = suites.stream().filter(s -> type.equalsIgnoreCase(s.getType())).toList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", suites));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return testSuiteService.getById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/cases")
    public ResponseEntity<Map<String, Object>> cases(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<SuiteCase>of()));
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
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        suite.setId(id);
        testSuiteService.update(suite);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 或 caseId≤0 时返回 400 */
    @PostMapping("/{id}/addCase")
    public ResponseEntity<Map<String, Object>> addCase(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        int caseId = body.get("caseId") instanceof Number n ? n.intValue() : 0;
        if (caseId <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid caseId"));
        int productId = body.get("productId") instanceof Number n ? n.intValue() : 0;
        testSuiteService.addCase(id, caseId, productId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量关联用例到套件，与 PHP testsuite linkCase 一致；请求体 caseIds、productId，可选 versions（caseId->version）；id≤0 返回 400 */
    @PostMapping("/{id}/linkCases")
    public ResponseEntity<Map<String, Object>> linkCases(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        int productId = body != null && body.get("productId") instanceof Number n ? n.intValue() : 0;
        List<Integer> caseIds = body != null && body.get("caseIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        Map<Integer, Integer> versions = null;
        if (body != null && body.get("versions") instanceof Map<?, ?> vm) {
            java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
            vm.forEach((k, v) -> {
                if (k instanceof Number nk && v instanceof Number nv) map.put(nk.intValue(), nv.intValue());
            });
            versions = map;
        }
        testSuiteService.linkCases(id, productId, caseIds, versions);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}/removeCase/{caseId}")
    public ResponseEntity<Map<String, Object>> removeCase(@PathVariable int id, @PathVariable int caseId) {
        if (id <= 0 || caseId <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testSuiteService.removeCase(id, caseId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量移除套件中的用例，与 PHP testsuite batchUnlinkCases 一致；id≤0 返回 400 */
    @PostMapping("/{id}/batchUnlinkCases")
    public ResponseEntity<Map<String, Object>> batchUnlinkCases(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> caseIds = body != null && body.get("caseIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        testSuiteService.batchUnlinkCases(id, caseIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testSuiteService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testSuiteService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
