package com.zentao.controller;

import com.zentao.entity.WeeklyReport;
import com.zentao.service.WeeklyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 周报模块 - 对应 module/weekly
 */
@RestController
@RequestMapping("/api/weekly")
@RequiredArgsConstructor
public class WeeklyController {

    private final WeeklyReportService weeklyReportService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int projectID) {
        List<WeeklyReport> list = weeklyReportService.getByProject(projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return weeklyReportService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody WeeklyReport report) {
        WeeklyReport created = weeklyReportService.create(report);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody WeeklyReport report) {
        report.setId(id);
        weeklyReportService.update(report);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        weeklyReportService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
