package com.zentao.controller;

import com.zentao.entity.TestSuite;
import com.zentao.repository.TestSuiteRepository;
import com.zentao.service.TestSuiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用例库模块 - 基于 TestSuite type=library，对应 module/caselib
 */
@RestController
@RequestMapping("/api/caselib")
@RequiredArgsConstructor
public class CaselibController {

    private final TestSuiteService testSuiteService;
    private final TestSuiteRepository testSuiteRepository;

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "all") String type) {
        return ResponseEntity.ok(Map.of("result", "success", "data", testSuiteService.getCaselibPairs(type)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project) {
        List<TestSuite> list;
        if (product != null && product > 0) {
            list = testSuiteRepository.findByTypeAndProductAndDeletedOrderByOrderNumAsc("library", product, 0);
        } else if (project != null && project > 0) {
            list = testSuiteRepository.findByTypeAndProjectAndDeletedOrderByOrderNumAsc("library", project, 0);
        } else {
            list = testSuiteRepository.findByTypeAndDeletedOrderByOrderNumAsc("library", 0);
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return testSuiteService.getById(id)
                .filter(s -> "library".equals(s.getType()))
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody TestSuite lib) {
        lib.setType("library");
        TestSuite created = testSuiteService.create(lib);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody TestSuite lib) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        lib.setId(id);
        lib.setType("library");
        testSuiteService.update(lib);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        testSuiteService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/browse")
    public ResponseEntity<Map<String, Object>> browse(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return testSuiteService.getById(id)
                .filter(s -> "library".equals(s.getType()))
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }
}
