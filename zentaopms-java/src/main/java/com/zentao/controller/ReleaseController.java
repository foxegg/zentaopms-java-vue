package com.zentao.controller;

import com.zentao.entity.Release;
import com.zentao.service.ExportService;
import com.zentao.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 发布版本模块 - 对应 module/release
 */
@RestController
@RequestMapping("/api/release")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;
    private final ExportService exportService;

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", releaseService.getPairs(product)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", releaseService.getPairsByList(idList)));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer product,
            @RequestParam(required = false) Integer project,
            @RequestParam(required = false, defaultValue = "all") String branch,
            @RequestParam(required = false, defaultValue = "all") String type) {
        List<Release> releases;
        if (project != null && project > 0) {
            releases = releaseService.getByProject(project);
        } else if (product != null && product > 0) {
            releases = releaseService.getByProduct(product, branch, type);
        } else {
            releases = releaseService.getList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", releases));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return releaseService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/stories")
    public ResponseEntity<Map<String, Object>> stories(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", releaseService.getStoriesByRelease(id)));
    }

    @GetMapping("/{id}/bugs")
    public ResponseEntity<Map<String, Object>> bugs(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", releaseService.getBugsByRelease(id)));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Release release) {
        Release created = releaseService.create(release);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Release release) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        release.setId(id);
        releaseService.update(release);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        releaseService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时拒绝操作，返回 400 */
    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = releaseService.linkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = releaseService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/linkBug")
    public ResponseEntity<Map<String, Object>> linkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        boolean ok = releaseService.linkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int bugID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid bugID"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = releaseService.unlinkBug(id, bugID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = toIntList(body != null ? body.get("storyIds") : null);
        boolean ok = releaseService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkBug")
    public ResponseEntity<Map<String, Object>> batchUnlinkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = toIntList(body != null ? body.get("bugIds") : null);
        boolean ok = releaseService.batchUnlinkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PutMapping("/{id}/changeStatus")
    public ResponseEntity<Map<String, Object>> changeStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = releaseService.changeStatus(id, body != null ? body.get("status") : null);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Map<String, Object>> publish(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        boolean ok = releaseService.publish(id);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Map<String, Object>> notify(@PathVariable int id, @RequestBody(required = false) Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (releaseService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("result", "success", "message", "notify placeholder"));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) Integer product,
            @RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        byte[] data = exportService.exportReleases(product, maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=releases.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
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
