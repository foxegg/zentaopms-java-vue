package com.zentao.controller;

import com.zentao.entity.Task;
import com.zentao.service.ExportService;
import com.zentao.service.RepoService;
import com.zentao.service.TaskEstimateService;
import com.zentao.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务模块 - 对应 module/task
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskEstimateService taskEstimateService;
    private final ExportService exportService;
    private final RepoService repoService;

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(required = false) String ids) {
        List<Integer> idList = java.util.Collections.emptyList();
        if (ids != null && !ids.isBlank()) {
            idList = java.util.Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; } })
                    .filter(i -> i != null && i > 0)
                    .distinct()
                    .toList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", taskService.getPairsByIdList(idList)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer execution,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        org.springframework.data.jpa.domain.Specification<com.zentao.entity.Task> spec = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
            if (project != null && project > 0) preds.add(cb.equal(root.get("project"), project));
            if (execution != null && execution > 0) preds.add(cb.equal(root.get("execution"), execution));
            if (assignedTo != null && !assignedTo.isEmpty()) preds.add(cb.equal(root.get("assignedTo"), assignedTo));
            if (status != null && !status.isEmpty()) preds.add(cb.equal(root.get("status"), status));
            if (type != null && !type.isEmpty()) preds.add(cb.equal(root.get("type"), type));
            return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        var page = taskService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
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

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/efforts")
    public ResponseEntity<Map<String, Object>> efforts(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var efforts = taskEstimateService.getByTask(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", efforts));
    }

    @PostMapping("/{id}/recordWorkhour")
    public ResponseEntity<Map<String, Object>> recordWorkhour(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        java.time.LocalDate date = body.get("date") != null ? java.time.LocalDate.parse(body.get("date").toString()) : java.time.LocalDate.now();
        java.math.BigDecimal consumed = body.get("consumed") != null ? new java.math.BigDecimal(body.get("consumed").toString()) : java.math.BigDecimal.ZERO;
        java.math.BigDecimal left = body.get("left") != null ? new java.math.BigDecimal(body.get("left").toString()) : java.math.BigDecimal.ZERO;
        String work = (String) body.getOrDefault("work", "");
        var e = taskEstimateService.recordWorkhour(id, date, consumed, left, work);
        return ResponseEntity.ok(Map.of("result", "success", "data", e));
    }

    @PutMapping("/effort/{estimateId}")
    public ResponseEntity<Map<String, Object>> editEffort(@PathVariable int estimateId, @RequestBody Map<String, Object> body) {
        if (estimateId <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        var opt = taskEstimateService.getById(estimateId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var e = opt.get();
        if (body.get("consumed") != null) e.setConsumed(new java.math.BigDecimal(body.get("consumed").toString()));
        if (body.get("left") != null) e.setLeft_(new java.math.BigDecimal(body.get("left").toString()));
        if (body.get("work") != null) e.setWork(body.get("work").toString());
        if (body.get("date") != null) e.setDate(java.time.LocalDate.parse(body.get("date").toString()));
        taskEstimateService.update(e);
        return ResponseEntity.ok(Map.of("result", "success", "data", e));
    }

    @DeleteMapping("/effort/{estimateId}")
    public ResponseEntity<Map<String, Object>> deleteWorkhour(@PathVariable int estimateId) {
        if (estimateId <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        taskEstimateService.delete(estimateId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；projectId≤0 时返回 data: [] */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Map<String, Object>> byProject(@PathVariable int projectId) {
        if (projectId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Task>of()));
        List<Task> tasks = taskService.getByProject(projectId);
        return ResponseEntity.ok(Map.of("result", "success", "data", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return taskService.getById(id)
                .map(t -> ResponseEntity.ok(Map.of("result", "success", "data", t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Task task) {
        Task created = taskService.create(task);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Task task) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        task.setId(id);
        taskService.update(task);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/assignTo")
    public ResponseEntity<Map<String, Object>> assignTo(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.assignTo(id, body.getOrDefault("assignedTo", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Map<String, Object>> finish(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        java.math.BigDecimal consumed = body.get("consumed") != null ? new java.math.BigDecimal(body.get("consumed").toString()) : null;
        java.math.BigDecimal left = body.get("left") != null ? new java.math.BigDecimal(body.get("left").toString()) : null;
        Task task = taskService.finish(id, consumed, left);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<Map<String, Object>> pause(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.pause(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/restart")
    public ResponseEntity<Map<String, Object>> restart(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.restart(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.close(id, body != null ? body.get("closedReason") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancel(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.cancel(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        taskService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchAssignTo")
    public ResponseEntity<Map<String, Object>> batchAssignTo(@RequestBody Map<String, Object> body) {
        List<Integer> taskIds = toIntList(body.get("taskIds"));
        String assignedTo = (String) body.getOrDefault("assignedTo", "");
        taskService.batchAssignTo(taskIds, assignedTo);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchClose")
    public ResponseEntity<Map<String, Object>> batchClose(@RequestBody Map<String, Object> body) {
        List<Integer> taskIds = toIntList(body.get("taskIds"));
        taskService.batchClose(taskIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> taskIds = toIntList(body.get("taskIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        taskService.batchEdit(taskIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchCreate")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<Task> tasks) {
        List<Integer> ids = taskService.batchCreate(tasks);
        return ResponseEntity.ok(Map.of("result", "success", "data", ids));
    }

    @PutMapping("/{id}/confirmStoryChange")
    public ResponseEntity<Map<String, Object>> confirmStoryChange(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Task task = taskService.confirmStoryChange(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "data", task));
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> report(
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer execution) {
        return ResponseEntity.ok(Map.of("result", "success", "data", taskService.getReport(project, execution)));
    }

    @PostMapping("/batchChangeModule")
    public ResponseEntity<Map<String, Object>> batchChangeModule(@RequestBody Map<String, Object> body) {
        List<Integer> taskIds = toIntList(body.get("taskIds"));
        int moduleID = body.get("moduleID") instanceof Number n ? n.intValue() : 0;
        taskService.batchChangeModule(taskIds, moduleID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchCancel")
    public ResponseEntity<Map<String, Object>> batchCancel(@RequestBody Map<String, Object> body) {
        List<Integer> taskIds = toIntList(body.get("taskIds"));
        taskService.batchCancel(taskIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/manageTeam")
    public ResponseEntity<Map<String, Object>> manageTeam(@RequestParam(required = false, defaultValue = "0") int executionID, @RequestParam(required = false, defaultValue = "0") int taskID, @RequestBody Map<String, Object> body) {
        if (executionID <= 0 || taskID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid executionID or taskID"));
        if (taskService.getById(taskID).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/createBranch")
    public ResponseEntity<Map<String, Object>> createBranch(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        int repoID = body != null && body.get("repoID") != null ? ((Number) body.get("repoID")).intValue() : 0;
        if (repoID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid repoID"));
        repoService.createBranch(id, repoID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/unlinkBranch")
    public ResponseEntity<Map<String, Object>> unlinkBranch(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (taskService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        repoService.unlinkBranch(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer execution,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        org.springframework.data.jpa.domain.Specification<Task> spec = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
            if (project != null && project > 0) preds.add(cb.equal(root.get("project"), project));
            if (execution != null && execution > 0) preds.add(cb.equal(root.get("execution"), execution));
            if (assignedTo != null && !assignedTo.isEmpty()) preds.add(cb.equal(root.get("assignedTo"), assignedTo));
            if (status != null && !status.isEmpty()) preds.add(cb.equal(root.get("status"), status));
            return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        byte[] data = exportService.exportTasks(spec, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tasks.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
    }

    private List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().map(o -> o instanceof Number n ? n.intValue() : Integer.parseInt(o.toString())).toList();
        }
        return List.of();
    }
}
