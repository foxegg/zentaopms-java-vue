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
    public ResponseEntity<Map<String, Object>> list(@RequestParam int projectID) {
        List<ProjectStory> list = projectStoryService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        int storyID = ((Number) body.getOrDefault("storyID", 0)).intValue();
        int productID = body.containsKey("productID") ? ((Number) body.get("productID")).intValue() : 0;
        int branch = body.containsKey("branch") ? ((Number) body.get("branch")).intValue() : 0;
        ProjectStory ps = projectStoryService.linkStory(projectID, storyID, productID, branch);
        return ResponseEntity.ok(Map.of("result", "success", "id", ps.getId()));
    }

    @PostMapping("/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
        int storyID = ((Number) body.getOrDefault("storyID", 0)).intValue();
        projectStoryService.unlinkStory(projectID, storyID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@RequestBody Map<String, Object> body) {
        int projectID = ((Number) body.getOrDefault("projectID", 0)).intValue();
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
