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

    /** 与 PHP notify list 一致：status、createdBy 筛选，pageID、recPerPage 分页；按 createdBy 时分页并返回 pager */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String createdBy,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (createdBy != null && !createdBy.isEmpty()) {
            var page = notifyService.getPageByCreatedBy(createdBy, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", page.getContent(),
                    "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
        }
        List<Notify> list = notifyService.getByStatus(status);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return notifyService.getById(id)
                .map(n -> ResponseEntity.ok(Map.of("result", "success", "data", n)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (notifyService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
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
