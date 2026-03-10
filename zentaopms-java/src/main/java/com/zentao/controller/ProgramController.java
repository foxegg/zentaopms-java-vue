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

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs() {
        return ResponseEntity.ok(Map.of("result", "success", "data", projectService.getProgramPairs()));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", projectService.getProgramPairsByList(idList)));
    }

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
        if (id <= 0) return ResponseEntity.notFound().build();
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
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        program.setId(id);
        program.setType("program");
        projectService.update(program);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        projectService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
