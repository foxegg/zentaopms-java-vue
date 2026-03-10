package com.zentao.controller;

import com.zentao.entity.ProductPlan;
import com.zentao.service.ProductPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品计划模块 - 对应 module/productplan
 */
@RestController
@RequestMapping("/api/productplan")
@RequiredArgsConstructor
public class ProductPlanController {

    private final ProductPlanService productPlanService;

    /** 与 PHP productplan getList 一致；productID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "0") int productID,
            @RequestParam(required = false, defaultValue = "all") String branch) {
        List<ProductPlan> plans = productPlanService.getByProductAndBranch(productID, branch);
        return ResponseEntity.ok(Map.of("result", "success", "data", plans));
    }

    /** 与 PHP productplan getPairs 一致；productID≤0 时返回空 */
    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(required = false, defaultValue = "0") int productID,
            @RequestParam(required = false, defaultValue = "all") String branch) {
        return ResponseEntity.ok(Map.of("result", "success", "data", productPlanService.getPairs(productID, branch)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", productPlanService.getPairsByList(idList)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return productPlanService.getById(id)
                .map(p -> ResponseEntity.ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody ProductPlan plan) {
        ProductPlan created = productPlanService.create(plan);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody ProductPlan plan) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        plan.setId(id);
        productPlanService.update(plan);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        ProductPlan p = productPlanService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Map<String, Object>> finish(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        ProductPlan p = productPlanService.finish(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        ProductPlan p = productPlanService.close(id, body != null ? body.get("closedReason") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        ProductPlan p = productPlanService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        productPlanService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/stories")
    public ResponseEntity<Map<String, Object>> stories(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var list = productPlanService.getStoriesByPlan(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}/bugs")
    public ResponseEntity<Map<String, Object>> bugs(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var list = productPlanService.getBugsByPlan(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 与 PHP 一致；id≤0 时拒绝操作，返回 400 */
    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        var ps = productPlanService.linkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "id", ps.getId()));
    }

    /** 批量关联需求，与 PHP productplan linkStory(POST stories 数组) 一致 */
    @PostMapping("/{id}/batchLinkStory")
    public ResponseEntity<Map<String, Object>> batchLinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = body != null && body.get("storyIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        productPlanService.batchLinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (storyID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid storyID"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        productPlanService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> storyIds = body != null && body.get("storyIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        productPlanService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/linkBug")
    public ResponseEntity<Map<String, Object>> linkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = body != null && body.get("bugIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        productPlanService.linkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/unlinkBug")
    public ResponseEntity<Map<String, Object>> unlinkBug(@PathVariable int id, @RequestParam int bugID) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (bugID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid bugID"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        productPlanService.unlinkBug(id, bugID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/batchUnlinkBug")
    public ResponseEntity<Map<String, Object>> batchUnlinkBug(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productPlanService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        List<Integer> bugIds = body != null && body.get("bugIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        productPlanService.batchUnlinkBug(id, bugIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
