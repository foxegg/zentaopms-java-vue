package com.zentao.controller;

import com.zentao.entity.Project;
import com.zentao.service.ActionService;
import com.zentao.service.BugService;
import com.zentao.service.BuildService;
import com.zentao.service.ExportService;
import com.zentao.service.ExecutionService;
import com.zentao.service.ProjectService;
import com.zentao.service.ProjectProductService;
import com.zentao.service.TeamService;
import com.zentao.service.TestSuiteService;
import com.zentao.service.TestTaskService;
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
    private final BugService bugService;
    private final BuildService buildService;
    private final TestTaskService testTaskService;
    private final TestSuiteService testSuiteService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "all") String status,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        org.springframework.data.jpa.domain.Specification<Project> spec = projectService.buildListSpec(status, type);
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

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> all() {
        List<Project> projects = projectService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", projects));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(required = false, defaultValue = "all") String mode,
            @RequestParam(required = false, defaultValue = "0") Integer programID) {
        Map<Integer, String> pairs = projectService.getPairs(mode, programID != null && programID > 0 ? programID : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", pairs));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", projectService.getPairsByList(idList)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
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
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        project.setId(id);
        projectService.update(project);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Project p = projectService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<Map<String, Object>> suspend(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Project p = projectService.suspend(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Project p = projectService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Project p = projectService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
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

    /** 与 PHP 一致；id≤0 时返回空 data + pager（与 execution dynamic 形状一致） */
    @GetMapping("/{id}/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", List.of(),
                "pager", Map.of("recTotal", 0L, "recPerPage", recPerPage, "pageID", pageID)));
        var actions = actionService.getByProject(id, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }

    @GetMapping("/{id}/executions")
    public ResponseEntity<Map<String, Object>> executions(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", executionService.getByProject(id)));
    }

    /** 与 PHP project 子列表一致：聚合 execution/bug/build/testtask/testsuite，供看板/概览使用；id≤0 返回 404 */
    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> summary(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Map<String, Object> data = Map.of(
                "executions", executionService.getByProject(id),
                "bugs", bugService.getByProject(id),
                "builds", buildService.getByProject(id),
                "testTasks", testTaskService.getByProject(id),
                "testSuites", testSuiteService.getByProject(id)
        );
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/{id}/team")
    public ResponseEntity<Map<String, Object>> team(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var list = teamService.getByProject(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/manageMembers")
    public ResponseEntity<Map<String, Object>> manageMembers(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
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
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var list = projectProductService.getProductsByProject(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/manageProducts")
    public ResponseEntity<Map<String, Object>> manageProducts(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (projectService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
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
