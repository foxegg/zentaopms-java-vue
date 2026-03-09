package com.zentao.controller;

import com.zentao.entity.Project;
import com.zentao.entity.ProjectStory;
import com.zentao.entity.Task;
import com.zentao.service.ActionService;
import com.zentao.service.ExecutionService;
import com.zentao.service.ProjectStoryService;
import com.zentao.service.TaskService;
import com.zentao.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 执行/迭代模块 - 对应 module/execution
 */
@RestController
@RequestMapping("/api/execution")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;
    private final TaskService taskService;
    private final TeamService teamService;
    private final ActionService actionService;
    private final ProjectStoryService projectStoryService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int projectID) {
        List<Project> executions = executionService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", executions));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "0") int projectID,
            @RequestParam(defaultValue = "all") String mode) {
        Map<Integer, String> pairs = executionService.getPairs(projectID, mode);
        return ResponseEntity.ok(Map.of("result", "success", "data", pairs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return executionService.getById(id)
                .map(e -> ResponseEntity.ok(Map.of("result", "success", "data", e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Project execution) {
        Project created = executionService.create(execution);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Project execution) {
        execution.setId(id);
        executionService.update(execution);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        Project p = executionService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        Project p = executionService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        executionService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<Map<String, Object>> suspend(@PathVariable int id) {
        Project p = executionService.suspend(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        Project p = executionService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @GetMapping("/{id}/task")
    public ResponseEntity<Map<String, Object>> task(@PathVariable int id) {
        List<Task> tasks = taskService.getByExecution(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", tasks));
    }

    @GetMapping("/{id}/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (executionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        var page = actionService.getByExecution(id, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
    }

    @GetMapping("/{id}/team")
    public ResponseEntity<Map<String, Object>> team(@PathVariable int id) {
        var list = teamService.getByExecution(id);
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
        teamService.manageMembersExecution(id, members);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 执行关联需求，对应 PHP execution linkStory（执行即 project，委托 projectstory） */
    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (executionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        int storyID = body.get("storyID") instanceof Number n ? n.intValue() : 0;
        int productID = body.get("productID") instanceof Number n ? n.intValue() : 0;
        int branch = body.get("branch") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().build();
        ProjectStory ps = projectStoryService.linkStory(id, storyID, productID, branch);
        return ResponseEntity.ok(Map.of("result", "success", "id", ps.getId()));
    }

    /** 执行取消关联需求，对应 PHP execution unlinkStory */
    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (executionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        int storyID = body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().build();
        projectStoryService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 执行批量取消关联需求，对应 PHP execution batchUnlinkStory */
    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (executionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = body.get("storyIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        projectStoryService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 执行已关联需求列表，对应 PHP execution story（project 即 execution） */
    @GetMapping("/{id}/stories")
    public ResponseEntity<Map<String, Object>> stories(@PathVariable int id) {
        if (executionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        var list = projectStoryService.getByProject(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> executionIds = body.get("executionIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        executionService.batchEdit(executionIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
