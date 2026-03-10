package com.zentao.controller;

import com.zentao.entity.Bug;
import com.zentao.service.BugService;
import com.zentao.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Bug 模块 - 对应 module/bug
 */
@RestController
@RequestMapping("/api/bug")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;
    private final ExportService exportService;

    /** 与 PHP bug list 一致：product、project、status、moduleID 等筛选，pageID、recPerPage 分页 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer moduleID,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) Integer severity,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        org.springframework.data.jpa.domain.Specification<Bug> spec = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
            if (product != null && product > 0) preds.add(cb.equal(root.get("product"), product));
            if (project != null && project > 0) preds.add(cb.equal(root.get("project"), project));
            if (status != null && !status.isEmpty()) preds.add(cb.equal(root.get("status"), status));
            if (moduleID != null && moduleID > 0) preds.add(cb.equal(root.get("module"), moduleID));
            if (assignedTo != null && !assignedTo.isEmpty()) preds.add(cb.equal(root.get("assignedTo"), assignedTo));
            if (severity != null && severity > 0) preds.add(cb.equal(root.get("severity"), severity));
            return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        var page = bugService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
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

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", bugService.getPairs(product)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", bugService.getPairsByList(idList)));
    }

    /** 与 PHP 一致；productId≤0 时返回 data: [] */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> byProduct(@PathVariable int productId) {
        if (productId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Bug>of()));
        List<Bug> bugs = bugService.getByProduct(productId);
        return ResponseEntity.ok(Map.of("result", "success", "data", bugs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return bugService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Bug bug) {
        Bug created = bugService.create(bug);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Bug bug) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        bug.setId(id);
        bugService.update(bug);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/assignTo")
    public ResponseEntity<Map<String, Object>> assignTo(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.assignTo(id, body.getOrDefault("assignedTo", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Map<String, Object>> confirm(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.confirm(id, body.getOrDefault("comment", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<Map<String, Object>> resolve(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.resolve(id,
                body.getOrDefault("resolution", "fixed"),
                body.getOrDefault("resolvedBuild", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.activate(id, body.getOrDefault("openedBuild", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.close(id, body.getOrDefault("comment", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        bugService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchAssignTo")
    public ResponseEntity<Map<String, Object>> batchAssignTo(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        String assignedTo = (String) body.getOrDefault("assignedTo", "");
        bugService.batchAssignTo(bugIds, assignedTo);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchResolve")
    public ResponseEntity<Map<String, Object>> batchResolve(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        String resolution = (String) body.getOrDefault("resolution", "fixed");
        String resolvedBuild = (String) body.getOrDefault("resolvedBuild", "");
        bugService.batchResolve(bugIds, resolution, resolvedBuild);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchClose")
    public ResponseEntity<Map<String, Object>> batchClose(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        bugService.batchClose(bugIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangeModule")
    public ResponseEntity<Map<String, Object>> batchChangeModule(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        int moduleId = body.get("moduleID") instanceof Number n ? n.intValue() : 0;
        bugService.batchChangeModule(bugIds, moduleId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangePlan")
    public ResponseEntity<Map<String, Object>> batchChangePlan(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        int planId = body.get("planID") instanceof Number n ? n.intValue() : 0;
        bugService.batchChangePlan(bugIds, planId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchConfirm")
    public ResponseEntity<Map<String, Object>> batchConfirm(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        bugService.batchConfirm(bugIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchActivate")
    public ResponseEntity<Map<String, Object>> batchActivate(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        String openedBuild = body != null && body.get("openedBuild") != null ? body.get("openedBuild").toString() : "";
        bugService.batchActivate(bugIds, openedBuild);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchCreate")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<Bug> bugs) {
        List<Integer> ids = bugService.batchCreate(bugs);
        return ResponseEntity.ok(Map.of("result", "success", "data", ids));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        bugService.batchEdit(bugIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/setStory")
    public ResponseEntity<Map<String, Object>> setStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID < 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.setStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PutMapping("/{id}/confirmStoryChange")
    public ResponseEntity<Map<String, Object>> confirmStoryChange(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.confirmStoryChange(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PostMapping("/{id}/linkBugs")
    public ResponseEntity<Map<String, Object>> linkBugs(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = toIntList(body != null ? body.get("bugIds") : null);
        Bug bug = bugService.linkBugs(id, bugIds != null ? bugIds : List.of());
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int relatedBugID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (relatedBugID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid relatedBugID"));
        if (bugService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Bug bug = bugService.unlinkBug(id, relatedBugID);
        return ResponseEntity.ok(Map.of("result", "success", "data", bug));
    }

    @PostMapping("/batchChangeBranch")
    public ResponseEntity<Map<String, Object>> batchChangeBranch(@RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        int branchID = body.get("branchID") instanceof Number n ? n.intValue() : 0;
        bugService.batchChangeBranch(bugIds, branchID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> report(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project) {
        return ResponseEntity.ok(Map.of("result", "success", "data", bugService.getReport(product, project)));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        org.springframework.data.jpa.domain.Specification<Bug> spec = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
            if (product != null && product > 0) preds.add(cb.equal(root.get("product"), product));
            if (project != null && project > 0) preds.add(cb.equal(root.get("project"), project));
            if (status != null && !status.isEmpty()) preds.add(cb.equal(root.get("status"), status));
            if (assignedTo != null && !assignedTo.isEmpty()) preds.add(cb.equal(root.get("assignedTo"), assignedTo));
            return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        byte[] data = exportService.exportBugs(spec, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bugs.xlsx");
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
