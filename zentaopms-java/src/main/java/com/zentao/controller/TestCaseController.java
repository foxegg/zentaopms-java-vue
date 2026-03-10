package com.zentao.controller;

import com.zentao.entity.Bug;
import com.zentao.entity.TestCase;
import com.zentao.service.BugService;
import com.zentao.service.ExportService;
import com.zentao.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 测试用例模块 - 对应 module/testcase
 */
@RestController
@RequestMapping("/api/testcase")
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final BugService bugService;
    private final ExportService exportService;

    /** 导出测试用例，与 PHP testcase export 一致；product 必传（按产品导出） */
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer product,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        byte[] data = exportService.exportTestCases(product, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=testcases.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", testCaseService.getPairs(product)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", testCaseService.getPairsByList(idList)));
    }

    /** 与 PHP testcase list 一致：product、lib、module、branch、type 筛选，pageID、recPerPage 分页；分页时返回 pager */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer lib,
            @RequestParam(required = false) Integer module,
            @RequestParam(required = false) Integer branch,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "1") int pageID,
            @RequestParam(required = false, defaultValue = "0") int recPerPage) {
        List<TestCase> cases;
        if (lib != null && lib > 0) {
            cases = testCaseService.getByLib(lib);
        } else if (product != null && product > 0) {
            cases = testCaseService.getByProduct(product);
        } else {
            cases = testCaseService.getList();
        }
        if (module != null && module > 0) {
            cases = cases.stream().filter(c -> module.equals(c.getModule())).toList();
        }
        if (branch != null && branch >= 0) {
            cases = cases.stream().filter(c -> c.getBranch() != null && branch == c.getBranch()).toList();
        }
        if (type != null && !type.isEmpty()) {
            cases = cases.stream().filter(c -> type.equals(c.getType())).toList();
        }
        if (recPerPage > 0 && pageID > 0) {
            int total = cases.size();
            int from = (pageID - 1) * recPerPage;
            if (from >= total) {
                return ResponseEntity.ok(Map.of(
                        "result", "success",
                        "data", List.<TestCase>of(),
                        "pager", Map.of("recTotal", total, "recPerPage", recPerPage, "pageID", pageID)));
            }
            int to = Math.min(from + recPerPage, total);
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", cases.subList(from, to),
                    "pager", Map.of("recTotal", total, "recPerPage", recPerPage, "pageID", pageID)));
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", cases));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return testCaseService.getById(id)
                .map(c -> ResponseEntity.ok(Map.of("result", "success", "data", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody TestCase testCase) {
        TestCase created = testCaseService.create(testCase);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody TestCase testCase) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testCaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testCase.setId(id);
        testCaseService.update(testCase);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testCaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        testCaseService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/createBug")
    public ResponseEntity<Map<String, Object>> createBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        var opt = testCaseService.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        TestCase c = opt.get();
        Bug bug = new Bug();
        bug.setProduct(c.getProduct() != null ? c.getProduct() : 0);
        bug.setTitle((String) body.getOrDefault("title", "From Case #" + id + ": " + (c.getTitle() != null ? c.getTitle() : "")));
        bug.setCaseId(id);
        bug.setCaseVersion(c.getVersion() != null ? c.getVersion() : 1);
        bug.setOpenedBuild((String) body.getOrDefault("openedBuild", ""));
        Bug created = bugService.create(bug);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PostMapping("/batchCreate")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<TestCase> cases) {
        List<Integer> ids = testCaseService.batchCreate(cases);
        return ResponseEntity.ok(Map.of("result", "success", "data", ids));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        testCaseService.batchEdit(caseIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchDelete")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        testCaseService.batchDelete(caseIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<Map<String, Object>> review(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (testCaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String result = body != null ? body.get("result") : null;
        TestCase c = testCaseService.review(id, result);
        return ResponseEntity.ok(Map.of("result", "success", "data", c));
    }

    @PostMapping("/batchReview")
    public ResponseEntity<Map<String, Object>> batchReview(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        String result = body != null && body.get("result") != null ? body.get("result").toString() : "reviewed";
        testCaseService.batchReview(caseIds, result);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangeModule")
    public ResponseEntity<Map<String, Object>> batchChangeModule(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        int moduleId = body != null && body.get("moduleID") instanceof Number n ? n.intValue() : 0;
        testCaseService.batchChangeModule(caseIds, moduleId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangeBranch")
    public ResponseEntity<Map<String, Object>> batchChangeBranch(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        int branchId = body != null && body.get("branchID") instanceof Number n ? n.intValue() : 0;
        testCaseService.batchChangeBranch(caseIds, branchId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangeType")
    public ResponseEntity<Map<String, Object>> batchChangeType(@RequestBody Map<String, Object> body) {
        List<Integer> caseIds = toIntList(body.get("caseIds"));
        String type = body != null && body.get("type") != null ? body.get("type").toString() : "";
        testCaseService.batchChangeType(caseIds, type);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/confirmStoryChange")
    public ResponseEntity<Map<String, Object>> confirmStoryChange(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (testCaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        TestCase c = testCaseService.confirmStoryChange(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "data", c));
    }

    private static List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList();
        }
        return List.of();
    }
}
