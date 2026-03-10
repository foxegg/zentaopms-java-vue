package com.zentao.controller;

import com.zentao.entity.Branch;
import com.zentao.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分支模块 - 对应 module/branch
 */
@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    /** 与 PHP branch getList 一致；productID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false, defaultValue = "0") int productID) {
        List<Branch> list = productID <= 0 ? List.of() : branchService.getByProduct(productID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "0") int productID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", branchService.getPairs(productID)));
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
        return ResponseEntity.ok(Map.of("result", "success", "data", branchService.getPairsByList(idList)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return branchService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Branch branch) {
        Branch created = branchService.create(branch);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Branch branch) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (branchService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        branch.setId(id);
        branchService.update(branch);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (branchService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        branchService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
