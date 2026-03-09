package com.zentao.controller;

import com.zentao.entity.Build;
import com.zentao.service.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 构建模块 - 对应 module/build
 */
@RestController
@RequestMapping("/api/build")
@RequiredArgsConstructor
public class BuildController {

    private final BuildService buildService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer execution) {
        List<Build> builds;
        if (execution != null && execution > 0) {
            builds = buildService.getByExecution(execution);
        } else if (project != null && project > 0) {
            builds = buildService.getByProject(project);
        } else if (product != null && product > 0) {
            builds = buildService.getByProduct(product);
        } else {
            builds = List.of();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", builds));
    }

    @GetMapping("/productBuilds")
    public ResponseEntity<Map<String, Object>> productBuilds(@RequestParam int productID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getByProduct(productID)));
    }

    @GetMapping("/projectBuilds")
    public ResponseEntity<Map<String, Object>> projectBuilds(@RequestParam int projectID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getByProject(projectID)));
    }

    @GetMapping("/executionBuilds")
    public ResponseEntity<Map<String, Object>> executionBuilds(@RequestParam int executionID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getByExecution(executionID)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return buildService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Build build) {
        Build created = buildService.create(build);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Build build) {
        build.setId(id);
        buildService.update(build);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        buildService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = buildService.linkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        boolean ok = buildService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = buildService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/linkBug")
    public ResponseEntity<Map<String, Object>> linkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        boolean ok = buildService.linkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int bugID) {
        boolean ok = buildService.unlinkBug(id, bugID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkBug")
    public ResponseEntity<Map<String, Object>> batchUnlinkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        boolean ok = buildService.batchUnlinkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
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
