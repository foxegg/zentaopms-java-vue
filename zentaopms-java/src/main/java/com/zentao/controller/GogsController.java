package com.zentao.controller;

import com.zentao.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/gogs")
@RequiredArgsConstructor
public class GogsController {

    private final RepoService repoService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", repoService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return repoService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        return PlaceholderResponses.success();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) {
        return PlaceholderResponses.success();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) {
        return PlaceholderResponses.success();
    }
}
