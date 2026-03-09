package com.zentao.controller;

import com.zentao.entity.Notify;
import com.zentao.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知队列 - 对应 module/notify
 */
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String createdBy,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        List<Notify> list;
        if (createdBy != null && !createdBy.isEmpty()) {
            list = notifyService.getByCreatedBy(createdBy, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        } else {
            list = notifyService.getByStatus(status);
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable int id) {
        return notifyService.getById(id)
                .map(n -> ResponseEntity.ok(Map.of("result", "success", "data", n)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        notifyService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/batchDelete")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestBody Map<String, Object> body) {
        List<Integer> ids = body.get("ids") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        notifyService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
