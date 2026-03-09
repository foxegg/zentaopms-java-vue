package com.zentao.controller;

import com.zentao.entity.Release;
import com.zentao.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目发布模块 - 对应 module/projectrelease，基于 zt_release 的 project 字段
 */
@RestController
@RequestMapping("/api/projectrelease")
@RequiredArgsConstructor
public class ProjectReleaseController {

    private final ReleaseService releaseService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int projectID) {
        List<Release> list = releaseService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return releaseService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Release release) {
        Release created = releaseService.create(release);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Release release) {
        release.setId(id);
        releaseService.update(release);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        releaseService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
