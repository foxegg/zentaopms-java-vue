package com.zentao.controller;

import com.zentao.entity.Project;
import com.zentao.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目集模块 - 对应 module/program，使用 zt_project type='program'
 */
@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProjectService projectService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "unclosed") String status,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "100") int recPerPage) {
        Specification<Project> spec = (root, q, cb) -> {
            if ("unclosed".equals(status)) {
                return cb.notEqual(root.get("status"), "closed");
            }
            if ("closed".equals(status)) {
                return cb.equal(root.get("status"), "closed");
            }
            return null;
        };
        spec = Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("type"), "program"));
        var page = projectService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of(
                        "recTotal", page.getTotalElements(),
                        "recPerPage", recPerPage,
                        "pageID", pageID
                )
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return projectService.getById(id)
                .filter(p -> "program".equals(p.getType()))
                .map(p -> ResponseEntity.ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Project program) {
        program.setType("program");
        Project created = projectService.create(program);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Project program) {
        program.setId(id);
        program.setType("program");
        projectService.update(program);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        projectService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
