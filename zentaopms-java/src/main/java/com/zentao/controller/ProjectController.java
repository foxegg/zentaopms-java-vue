package com.zentao.controller;

import com.zentao.entity.Project;
import com.zentao.service.ActionService;
import com.zentao.service.ExportService;
import com.zentao.service.ExecutionService;
import com.zentao.service.ProjectService;
import com.zentao.service.ProjectProductService;
import com.zentao.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目模块 - 对应 module/project
 */
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ExecutionService executionService;
    private final ActionService actionService;
    private final ExportService exportService;
    private final TeamService teamService;
    private final ProjectProductService projectProductService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var page = projectService.getList(null, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
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

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> all() {
        List<Project> projects = projectService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return projectService.getById(id)
                .map(p -> ResponseEntity.ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Project project) {
        Project created = projectService.create(project);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Project project) {
        project.setId(id);
        projectService.update(project);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        Project p = projectService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<Map<String, Object>> suspend(@PathVariable int id) {
        Project p = projectService.suspend(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        Project p = projectService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        Project p = projectService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        projectService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> projectIds = toIntList(body.get("projectIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        projectService.batchEdit(projectIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var actions = actionService.getByProject(id, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }

    @GetMapping("/{id}/executions")
    public ResponseEntity<Map<String, Object>> executions(@PathVariable int id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", executionService.getByProject(id)));
    }

    @GetMapping("/{id}/team")
    public ResponseEntity<Map<String, Object>> team(@PathVariable int id) {
        var list = teamService.getByProject(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/manageMembers")
    public ResponseEntity<Map<String, Object>> manageMembers(@PathVariable int id, @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> members = body.get("members") instanceof List<?> list
                ? list.stream()
                    .filter(o -> o instanceof Map)
                    .map(o -> (Map<String, Object>) o)
                    .toList()
                : List.of();
        teamService.manageMembersProject(id, members);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Map<String, Object>> products(@PathVariable int id) {
        var list = projectProductService.getProductsByProject(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/manageProducts")
    public ResponseEntity<Map<String, Object>> manageProducts(@PathVariable int id, @RequestBody Map<String, Object> body) {
        projectProductService.manageProductsFromBody(id, body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false, defaultValue = "all") String status,
            @RequestParam(required = false) String orderBy,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        byte[] data = exportService.exportProjects(status, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=projects.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
    }

    private static List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList();
        }
        return List.of();
    }
}
