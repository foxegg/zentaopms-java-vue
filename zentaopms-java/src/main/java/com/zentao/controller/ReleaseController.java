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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false) Integer product) {
        List<Release> releases = (product != null && product > 0)
                ? releaseService.getByProduct(product) : List.of();
        return ResponseEntity.ok(Map.of("result", "success", "data", releases));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return releaseService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Release release) {
        Release created = releaseService.create(release);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Release release) {
        release.setId(id);
        releaseService.update(release);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        releaseService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body.get("storyIds"));
        boolean ok = releaseService.linkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        boolean ok = releaseService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/linkBug")
    public ResponseEntity<Map<String, Object>> linkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body.get("bugIds"));
        boolean ok = releaseService.linkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int bugID) {
        boolean ok = releaseService.unlinkBug(id, bugID);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> storyIds = toIntList(body != null ? body.get("storyIds") : null);
        boolean ok = releaseService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/batchUnlinkBug")
    public ResponseEntity<Map<String, Object>> batchUnlinkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> bugIds = toIntList(body != null ? body.get("bugIds") : null);
        boolean ok = releaseService.batchUnlinkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PutMapping("/{id}/changeStatus")
    public ResponseEntity<Map<String, Object>> changeStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
        boolean ok = releaseService.changeStatus(id, body != null ? body.get("status") : null);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Map<String, Object>> publish(@PathVariable int id) {
        boolean ok = releaseService.publish(id);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail"));
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Map<String, Object>> notify(@PathVariable int id, @RequestBody(required = false) Map<String, Object> body) {
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
