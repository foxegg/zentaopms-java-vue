package com.zentao.controller;

import com.zentao.entity.Bug;
import com.zentao.entity.TestCase;
import com.zentao.service.BugService;
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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer lib) {
        List<TestCase> cases;
        if (lib != null && lib > 0) {
            cases = testCaseService.getByLib(lib);
        } else if (product != null && product > 0) {
            cases = testCaseService.getByProduct(product);
        } else {
            cases = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", cases));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        testCase.setId(id);
        testCaseService.update(testCase);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        testCaseService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/createBug")
    public ResponseEntity<Map<String, Object>> createBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        TestCase c = testCaseService.getById(id).orElseThrow(() -> new RuntimeException("用例不存在"));
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

    @PutMapping("/{id}/confirmStoryChange")
    public ResponseEntity<Map<String, Object>> confirmStoryChange(@PathVariable int id, @RequestBody Map<String, Object> body) {
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
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
