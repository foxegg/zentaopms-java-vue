package com.zentao.controller;

import com.zentao.entity.Action;
import com.zentao.service.ActionService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam String objectType,
            @RequestParam int objectID) {
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
        return actionService.getById(id)
                .map(a -> ResponseEntity.ok(Map.of("result", "success", "data", a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editComment(@PathVariable int id, @RequestBody Map<String, String> body) {
        String comment = body != null ? body.get("comment") : null;
        Action a = actionService.updateComment(id, comment);
        return ResponseEntity.ok(Map.of("result", "success", "data", a));
    }
}
