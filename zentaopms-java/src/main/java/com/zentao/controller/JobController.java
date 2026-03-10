package com.zentao.controller;

import com.zentao.entity.Job;
import com.zentao.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 构建任务模块 - 对应 module/jenkins, module/ci 的 job
 */
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer repoID,
            @RequestParam(required = false) Integer productID) {
        List<Job> list;
        if (repoID != null && repoID > 0) {
            list = jobService.getByRepo(repoID);
        } else if (productID != null && productID > 0) {
            list = jobService.getByProduct(productID);
        } else {
            list = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return jobService.getById(id)
                .map(j -> ResponseEntity.ok(Map.of("result", "success", "data", j)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Job job) {
        Job created = jobService.create(job);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Job job) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        job.setId(id);
        jobService.update(job);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        jobService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
