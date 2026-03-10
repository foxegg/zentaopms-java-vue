package com.zentao.controller;

import com.zentao.entity.Kanban;
import com.zentao.entity.KanbanCard;
import com.zentao.entity.KanbanSpace;
import com.zentao.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 看板模块 - 对应 module/kanban
 */
@RestController
@RequestMapping("/api/kanban")
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping("/space/list")
    public ResponseEntity<Map<String, Object>> spaceList() {
        List<KanbanSpace> list = kanbanService.getSpaceList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/space/{id}")
    public ResponseEntity<Map<String, Object>> spaceView(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return kanbanService.getSpaceById(id)
                .map(s -> ResponseEntity.ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/space")
    public ResponseEntity<Map<String, Object>> createSpace(@RequestBody KanbanSpace space) {
        KanbanSpace created = kanbanService.createSpace(space);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/space/{id}")
    public ResponseEntity<Map<String, Object>> editSpace(@PathVariable int id, @RequestBody KanbanSpace space) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getSpaceById(id).isEmpty()) return ResponseEntity.notFound().build();
        space.setId(id);
        kanbanService.updateSpace(space);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/space/{id}")
    public ResponseEntity<Map<String, Object>> deleteSpace(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getSpaceById(id).isEmpty()) return ResponseEntity.notFound().build();
        kanbanService.deleteSpace(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP kanban 列表一致；spaceID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> kanbanList(@RequestParam(required = false, defaultValue = "0") int spaceID) {
        List<Kanban> list = spaceID <= 0 ? List.of() : kanbanService.getKanbansBySpace(spaceID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> kanbanView(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return kanbanService.getKanbanById(id)
                .map(k -> ResponseEntity.ok(Map.of("result", "success", "data", k)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createKanban(@RequestBody Kanban kanban) {
        Kanban created = kanbanService.createKanban(kanban);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editKanban(@PathVariable int id, @RequestBody Kanban kanban) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getKanbanById(id).isEmpty()) return ResponseEntity.notFound().build();
        kanban.setId(id);
        kanbanService.updateKanban(kanban);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteKanban(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getKanbanById(id).isEmpty()) return ResponseEntity.notFound().build();
        kanbanService.deleteKanban(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<Map<String, Object>> cardList(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        List<KanbanCard> list = kanbanService.getCardsByKanban(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<Map<String, Object>> cardView(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return kanbanService.getCardById(id)
                .map(c -> ResponseEntity.ok(Map.of("result", "success", "data", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/card")
    public ResponseEntity<Map<String, Object>> createCard(@RequestBody KanbanCard card) {
        KanbanCard created = kanbanService.createCard(card);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/card/{id}")
    public ResponseEntity<Map<String, Object>> editCard(@PathVariable int id, @RequestBody KanbanCard card) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getCardById(id).isEmpty()) return ResponseEntity.notFound().build();
        card.setId(id);
        kanbanService.updateCard(card);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/card/{id}")
    public ResponseEntity<Map<String, Object>> deleteCard(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (kanbanService.getCardById(id).isEmpty()) return ResponseEntity.notFound().build();
        kanbanService.deleteCard(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
