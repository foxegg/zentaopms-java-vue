package com.zentao.controller;

import com.zentao.entity.ProjectStory;
import com.zentao.service.ProjectStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目需求模块 - 对应 module/projectstory
 */
@RestController
@RequestMapping("/api/projectstory")
@RequiredArgsConstructor
public class ProjectStoryController {

    private final ProjectStoryService projectStoryService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false, defaultValue = "0") int projectID) {
        if (projectID <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<ProjectStory>of()));
        List<ProjectStory> list = projectStoryService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 与 PHP getExecutionStories 一致：批量移除前查询已关联到执行的需求，用于提示。storyIds 逗号分隔；projectID≤0 时返回空 */
    @GetMapping("/executionStories")
    public ResponseEntity<Map<String, Object>> executionStories(
            @RequestParam(required = false, defaultValue = "0") int projectID,
            @RequestParam(required = false) String storyIds) {
        if (projectID <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Map<String, Object>>of()));
        List<Integer> ids = parseIdList(storyIds);
        List<Map<String, Object>> list = projectStoryService.getExecutionStories(projectID, ids);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    private static List<Integer> parseIdList(String s) {
        if (s == null || s.isBlank()) return List.of();
        List<Integer> out = new java.util.ArrayList<>();
        for (String part : s.split(",")) {
            try { out.add(Integer.parseInt(part.trim())); } catch (NumberFormatException ignored) { }
        }
        return out;
    }

    @PostMapping("/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        if (projectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid projectID"));
        int storyID = ((Number) body.getOrDefault("storyID", 0)).intValue();
        int productID = body.containsKey("productID") ? ((Number) body.get("productID")).intValue() : 0;
        int branch = body.containsKey("branch") ? ((Number) body.get("branch")).intValue() : 0;
        ProjectStory ps = projectStoryService.linkStory(projectID, storyID, productID, branch);
        return ResponseEntity.ok(Map.of("result", "success", "id", ps.getId()));
    }

    /** 批量关联需求，与 PHP projectstory linkStory 委托 execution linkStory(POST stories) 一致；projectID≤0 返回 400 */
    @PostMapping("/batchLinkStory")
    public ResponseEntity<Map<String, Object>> batchLinkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        if (projectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid projectID"));
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        int productID = body.containsKey("productID") ? ((Number) body.get("productID")).intValue() : 0;
        int branch = body.containsKey("branch") ? ((Number) body.get("branch")).intValue() : 0;
        projectStoryService.batchLinkStory(projectID, storyIds, productID, branch);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        if (projectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid projectID"));
        int storyID = ((Number) body.getOrDefault("storyID", 0)).intValue();
        projectStoryService.unlinkStory(projectID, storyID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        if (projectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid projectID"));
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        projectStoryService.batchUnlinkStory(projectID, storyIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private static List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream()
                    .filter(o -> o instanceof Number)
                    .map(o -> ((Number) o).intValue())
                    .toList();
        }
        return List.of();
    }
}
