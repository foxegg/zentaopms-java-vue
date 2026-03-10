package com.zentao.controller;

import com.zentao.entity.Build;
import com.zentao.service.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目版本模块 - 对应 module/projectbuild，代理到 build 模块
 */
@RestController
@RequestMapping("/api/projectbuild")
@RequiredArgsConstructor
public class ProjectBuildController {

    private final BuildService buildService;

    /** 与 PHP 一致；projectID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false, defaultValue = "0") int projectID) {
        List<Build> list = projectID <= 0 ? List.of() : buildService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return buildService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Build build) {
        Build created = buildService.create(build);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Build build) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        build.setId(id);
        buildService.update(build);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        buildService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
