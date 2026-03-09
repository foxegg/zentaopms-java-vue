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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int productID) {
        List<Branch> list = branchService.getByProduct(productID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        branch.setId(id);
        branchService.update(branch);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        branchService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
