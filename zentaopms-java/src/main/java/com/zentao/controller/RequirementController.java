package com.zentao.controller;

import com.zentao.entity.Story;
import com.zentao.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Requirement 模块 - 基于 Story type=requirement
 */
@RestController
@RequestMapping("/api/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final StoryService storyService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var page = storyService.getListByType("requirement", null, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
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

    /** 与 PHP 一致；productId≤0 时返回 data: [] */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> byProduct(@PathVariable int productId) {
        if (productId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Story>of()));
        List<Story> list = storyService.getByProductAndType(productId, "requirement");
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return storyService.getById(id)
                .filter(s -> "requirement".equals(s.getType()))
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Story story) {
        story.setType("requirement");
        Story created = storyService.create(story);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Story story) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        story.setId(id);
        story.setType("requirement");
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        storyService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
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

    private List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream()
                    .filter(o -> o instanceof Number)
                    .map(o -> ((Number) o).intValue())
                    .toList();
        }
        return List.of();
    }
}
