package com.zentao.controller;

import com.zentao.entity.Action;
import com.zentao.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 操作记录模块 - 对应 module/action
 */
@RestController
@RequestMapping("/api/action")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    /** 与 PHP action getList 一致：按 objectType+objectID 查操作记录；可选 pageID、recPerPage 分页，分页时返回 pager */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String objectType,
            @RequestParam(required = false, defaultValue = "0") int objectID,
            @RequestParam(required = false, defaultValue = "0") int pageID,
            @RequestParam(required = false, defaultValue = "0") int recPerPage) {
        if (objectID <= 0 || objectType == null || objectType.isBlank()) {
            if (recPerPage > 0 && pageID > 0) {
                return ResponseEntity.ok(Map.of(
                        "result", "success",
                        "data", List.<Action>of(),
                        "pager", Map.of("recTotal", 0L, "recPerPage", recPerPage, "pageID", pageID)));
            }
            return ResponseEntity.ok(Map.of("result", "success", "data", List.<Action>of()));
        }
        if (recPerPage > 0 && pageID > 0) {
            var page = actionService.getListPage(objectType, objectID, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", page.getContent(),
                    "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
        }
        List<Action> actions = actionService.getList(objectType, objectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }

    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> comment(@RequestBody Map<String, Object> body) {
        String objectType = (String) body.getOrDefault("objectType", "");
        int objectID = body.get("objectID") instanceof Number n ? n.intValue() : 0;
        String comment = (String) body.getOrDefault("comment", "");
        Action a = actionService.create(objectType, objectID, "commented", comment);
        return ResponseEntity.ok(Map.of("result", "success", "id", a.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return actionService.getById(id)
                .map(a -> ResponseEntity.ok(Map.of("result", "success", "data", a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editComment(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (actionService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String comment = body != null ? body.get("comment") : null;
        Action a = actionService.updateComment(id, comment);
        return ResponseEntity.ok(Map.of("result", "success", "data", a));
    }
}
