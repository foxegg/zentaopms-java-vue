package com.zentao.controller;

import com.zentao.entity.Project;
import com.zentao.service.ExecutionService;
import com.zentao.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目计划/阶段模块 - 对应 module/programplan，基于 project/execution
 */
@RestController
@RequestMapping("/api/programplan")
@RequiredArgsConstructor
public class ProgramPlanController {

    private final ProjectService projectService;
    private final ExecutionService executionService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int projectID,
            @RequestParam(defaultValue = "0") int executionID) {
        if (executionID > 0) {
            var exec = executionService.getById(executionID);
            return exec.map(e -> ResponseEntity.ok(Map.of("result", "success", "data", List.of(e))))
                    .orElse(ResponseEntity.ok(Map.of("result", "success", "data", List.of())));
        }
        if (projectID > 0) {
            List<Project> executions = executionService.getByProject(projectID);
            return ResponseEntity.ok(Map.of("result", "success", "data", executions));
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        var project = projectService.getById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(Map.of("result", "success", "data", project.get()));
        }
        var exec = executionService.getById(id);
        return exec.map(e -> ResponseEntity.ok(Map.of("result", "success", "data", e)))
                .orElse(ResponseEntity.notFound().build());
    }
}
