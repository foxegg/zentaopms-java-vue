package com.zentao.controller;

import com.zentao.entity.Pivot;
import com.zentao.service.PivotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP pivot 一致：zt_pivot 列表/查看/创建/编辑/删除；list 支持 dimension 筛选 */
@RestController
@RequestMapping("/api/pivot")
@RequiredArgsConstructor
public class PivotController {

    private final PivotService pivotService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false) Integer dimension) {
        List<Pivot> list = (dimension != null && dimension > 0)
                ? pivotService.getByDimension(dimension)
                : pivotService.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return pivotService.getById(id)
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Pivot body) {
        body.setId(null);
        Pivot created = pivotService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Pivot body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Pivot existing = pivotService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        body.setId(id);
        body.setCreatedBy(existing.getCreatedBy());
        body.setCreatedDate(existing.getCreatedDate());
        body.setDeleted(0);
        pivotService.update(body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (pivotService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        pivotService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
