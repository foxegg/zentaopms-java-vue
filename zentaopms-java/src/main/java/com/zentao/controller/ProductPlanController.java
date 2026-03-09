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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int productID) {
        List<ProductPlan> plans = productPlanService.getByProduct(productID);
        return ResponseEntity.ok(Map.of("result", "success", "data", plans));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        plan.setId(id);
        productPlanService.update(plan);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        ProductPlan p = productPlanService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Map<String, Object>> finish(@PathVariable int id) {
        ProductPlan p = productPlanService.finish(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id, @RequestBody Map<String, String> body) {
        ProductPlan p = productPlanService.close(id, body != null ? body.get("closedReason") : null);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        ProductPlan p = productPlanService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        productPlanService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/stories")
    public ResponseEntity<Map<String, Object>> stories(@PathVariable int id) {
        var list = productPlanService.getStoriesByPlan(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/{id}/linkStory")
    public ResponseEntity<Map<String, Object>> linkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        int storyID = body != null && body.get("storyID") instanceof Number n ? n.intValue() : 0;
        var ps = productPlanService.linkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success", "id", ps.getId()));
    }

    @PostMapping("/{id}/unlinkStory")
    public ResponseEntity<Map<String, Object>> unlinkStory(@PathVariable int id, @RequestParam int storyID) {
        productPlanService.unlinkStory(id, storyID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/{id}/batchUnlinkStory")
    public ResponseEntity<Map<String, Object>> batchUnlinkStory(@PathVariable int id, @RequestBody Map<String, Object> body) {
        List<Integer> storyIds = body != null && body.get("storyIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        productPlanService.batchUnlinkStory(id, storyIds);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
