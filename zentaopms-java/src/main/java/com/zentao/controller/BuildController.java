package com.zentao.controller;

import com.zentao.entity.Build;
import com.zentao.service.BuildService;
import com.zentao.service.ExportService;
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
    private final ExportService exportService;

    /** 导出构建版本，按产品；product 必传 */
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer product,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        byte[] data = exportService.exportBuilds(product, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=builds.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
    }

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
            builds = buildService.getList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", builds));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getPairs(product)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getPairsByList(idList)));
    }

    /** 与 PHP 一致；ID≤0 时返回空列表 */
    @GetMapping("/productBuilds")
    public ResponseEntity<Map<String, Object>> productBuilds(@RequestParam(required = false, defaultValue = "0") int productID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", productID <= 0 ? List.<Build>of() : buildService.getByProduct(productID)));
    }

    @GetMapping("/projectBuilds")
    public ResponseEntity<Map<String, Object>> projectBuilds(@RequestParam(required = false, defaultValue = "0") int projectID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", projectID <= 0 ? List.<Build>of() : buildService.getByProject(projectID)));
    }

    @GetMapping("/executionBuilds")
    public ResponseEntity<Map<String, Object>> executionBuilds(@RequestParam(required = false, defaultValue = "0") int executionID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", executionID <= 0 ? List.<Build>of() : buildService.getByExecution(executionID)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return buildService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/stories")
    public ResponseEntity<Map<String, Object>> stories(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getStoriesByBuild(id)));
    }

    @GetMapping("/{id}/bugs")
    public ResponseEntity<Map<String, Object>> bugs(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", buildService.getBugsByBuild(id)));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Build build) {
        Build created = buildService.create(build);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Build build) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        build.setId(id);
        buildService.update(build);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        buildService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时拒绝操作，返回 400 */
    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = buildService.linkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = buildService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = buildService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/linkBug")
    public ResponseEntity<Map<String, Object>> linkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        boolean ok = buildService.linkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int bugID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid bugID"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = buildService.unlinkBug(id, bugID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkBug")
    public ResponseEntity<Map<String, Object>> batchUnlinkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (buildService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
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
