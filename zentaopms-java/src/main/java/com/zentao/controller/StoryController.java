package com.zentao.controller;

import com.zentao.entity.Story;
import com.zentao.service.ExportService;
import com.zentao.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 需求/用户故事模块 - 对应 module/story
 */
@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final ExportService exportService;

    /** 与 PHP story list 一致：product、status、branch、moduleID 筛选，pageID、recPerPage 分页 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer branch,
            @RequestParam(required = false) Integer moduleID,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        org.springframework.data.jpa.domain.Specification<Story> spec = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
            if (product != null && product > 0) preds.add(cb.equal(root.get("product"), product));
            if (status != null && !status.isEmpty()) preds.add(cb.equal(root.get("status"), status));
            if (branch != null && branch >= 0) preds.add(cb.equal(root.get("branch"), branch));
            if (moduleID != null && moduleID > 0) preds.add(cb.equal(root.get("module"), moduleID));
            return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        var page = storyService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
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

    /** 与 PHP/StoryService 一致：product≤0 返回空；planID>0 按计划过滤，hasParent=false 仅非父需求 */
    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "0") int product,
            @RequestParam(required = false, defaultValue = "0") int planID,
            @RequestParam(required = false) Boolean hasParent) {
        boolean hasParentVal = hasParent == null || hasParent;
        return ResponseEntity.ok(Map.of("result", "success", "data", storyService.getPairs(product, planID, hasParentVal)));
    }

    /** 与 PHP/StoryService 一致：withEmptyOption=true 时结果含 0→""（请选择） */
    @GetMapping("/pairsByList")
    public ResponseEntity<Map<String, Object>> pairsByList(
            @RequestParam(required = false) String ids,
            @RequestParam(required = false, defaultValue = "false") boolean withEmptyOption) {
        List<Integer> idList = ids != null && !ids.isBlank()
                ? java.util.Arrays.stream(ids.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(s -> { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; } })
                        .filter(i -> i != null && i > 0)
                        .distinct()
                        .toList()
                : List.of();
        return ResponseEntity.ok(Map.of("result", "success", "data", storyService.getPairsByList(idList, withEmptyOption)));
    }

    /** 与 PHP 一致；productId≤0 时返回 data: [] */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> byProduct(@PathVariable int productId) {
        if (productId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Story>of()));
        List<Story> stories = storyService.getByProduct(productId);
        return ResponseEntity.ok(Map.of("result", "success", "data", stories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return storyService.getById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Story story) {
        Story created = storyService.create(story);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Story story) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        story.setId(id);
        storyService.update(story);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Story story = storyService.close(id, body != null ? body.get("closedReason") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", story));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Story story = storyService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", story));
    }

    @PutMapping("/{id}/assignTo")
    public ResponseEntity<Map<String, Object>> assignTo(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Story story = storyService.assignTo(id, body != null ? body.get("assignedTo") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", story));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        storyService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int targetStoryID = body != null && body.get("targetStoryID") instanceof Number n ? n.intValue() : 0;
        if (targetStoryID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid targetStoryID"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Story story = storyService.linkStory(id, targetStoryID);
        return ResponseEntity.ok(Map.of("result", "success", "data", story));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int targetStoryID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (targetStoryID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid targetStoryID"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Story story = storyService.unlinkStory(id, targetStoryID);
        return ResponseEntity.ok(Map.of("result", "success", "data", story));
    }

    @PostMapping("/batchAssignTo")
    public ResponseEntity<Map<String, Object>> batchAssignTo(@RequestBody Map<String, Object> body) {
        List<Integer> ids = toIntList(body.get("storyIds"));
        String assignedTo = (String) body.getOrDefault("assignedTo", "");
        storyService.batchAssignTo(ids, assignedTo);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchClose")
    public ResponseEntity<Map<String, Object>> batchClose(@RequestBody Map<String, Object> body) {
        List<Integer> ids = toIntList(body.get("storyIds"));
        String closedReason = (String) body.getOrDefault("closedReason", "");
        storyService.batchClose(ids, closedReason);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchCreate")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<Story> stories) {
        List<Integer> ids = storyService.batchCreate(stories);
        return ResponseEntity.ok(Map.of("result", "success", "data", ids));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        storyService.batchEdit(storyIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchChangePlan")
    public ResponseEntity<Map<String, Object>> batchChangePlan(@RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        int planID = body.get("planID") instanceof Number n ? n.intValue() : 0;
        storyService.batchChangePlan(storyIds, planID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量修改需求所属模块，与 PHP story batchChangeModule 一致 */
    @PostMapping("/batchChangeModule")
    public ResponseEntity<Map<String, Object>> batchChangeModule(@RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        int moduleID = body.get("moduleID") instanceof Number n ? n.intValue() : 0;
        storyService.batchChangeModule(storyIds, moduleID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量修改需求分支，与 PHP story batchChangeBranch 一致 */
    @PostMapping("/batchChangeBranch")
    public ResponseEntity<Map<String, Object>> batchChangeBranch(@RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        int branchID = body.get("branchID") instanceof Number n ? n.intValue() : 0;
        storyService.batchChangeBranch(storyIds, branchID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> report(@RequestParam(required = false) Integer product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", storyService.getReport(product)));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer product,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        org.springframework.data.jpa.domain.Specification<Story> spec = (root, q, cb) -> {
            if (product != null && product > 0) {
                return cb.equal(root.get("product"), product);
            }
            return null;
        };
        byte[] data = exportService.exportStories(spec, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stories.xlsx");
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
