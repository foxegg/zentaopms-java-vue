package com.zentao.controller;

import com.zentao.entity.Metric;
import com.zentao.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计指标模块 - 对应 module/metric，对接 zt_metric
 */
@RestController
@RequestMapping("/api/metric")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        List<Metric> list = metricService.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return metricService.getById(id)
                .map(m -> ResponseEntity.ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Metric metric) {
        Metric created = metricService.create(metric);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Metric metric) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        metric.setId(id);
        metricService.update(metric);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        metricService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
