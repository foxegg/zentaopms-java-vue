package com.zentao.controller;

import com.zentao.entity.Dataview;
import com.zentao.service.DataviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dataview")
@RequiredArgsConstructor
public class DataviewController {
    private final DataviewService service;

    @GetMapping({ "/list", "", "/" })
    public ResponseEntity<Map<String, Object>> list() {
        List<Dataview> list = service.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return service.getById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Dataview d) {
        Dataview created = service.create(d);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Dataview d) {
        d.setId(id);
        service.update(d);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
