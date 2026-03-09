package com.zentao.controller;

import com.zentao.entity.Screen;
import com.zentao.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/screen")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService service;

    @GetMapping({ "/list", "", "/" })
    public ResponseEntity<Map<String, Object>> list() {
        List<Screen> list = service.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return service.getById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Screen d) {
        Screen created = service.create(d);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Screen d) {
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
