package com.zentao.controller;

import com.zentao.entity.Holiday;
import com.zentao.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 节假日模块 - 对应 PHP module/holiday。
 * 实现细节：browse 按 year 查列表并返回 yearList；create/edit 校验 begin≤end；year 由 begin 推导。
 */
@RestController
@RequestMapping("/api/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String year) {
        List<Holiday> holidays = holidayService.getList(year);
        List<String> yearList = holidayService.getYearPairs();
        String currentYear = (year != null && !year.isBlank()) ? year : String.valueOf(java.time.Year.now().getValue());
        if (!yearList.contains(currentYear)) {
            yearList.add(0, currentYear);
        }
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", holidays,
                "yearList", yearList,
                "currentYear", currentYear
        ));
    }

    @GetMapping("/yearPairs")
    public ResponseEntity<Map<String, Object>> yearPairs() {
        return ResponseEntity.ok(Map.of("result", "success", "data", holidayService.getYearPairs()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return holidayService.getById(id)
                .map(h -> ResponseEntity.ok(Map.of("result", "success", "data", h)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Holiday holiday) {
        if (holiday.getEnd() != null && holiday.getBegin() != null && holiday.getBegin().isAfter(holiday.getEnd())) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "开始日期不能大于结束日期"));
        }
        Holiday created = holidayService.create(holiday);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Holiday holiday) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (holidayService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        if (holiday.getEnd() != null && holiday.getBegin() != null && holiday.getBegin().isAfter(holiday.getEnd())) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "开始日期不能大于结束日期"));
        }
        holiday.setId(id);
        holidayService.update(holiday);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (holidayService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        holidayService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
