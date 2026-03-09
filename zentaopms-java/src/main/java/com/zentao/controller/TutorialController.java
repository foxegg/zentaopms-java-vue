package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tutorial")
public class TutorialController {
    @GetMapping({ "/index", "" })
    public ResponseEntity<Map<String, Object>> index() { return PlaceholderResponses.emptyList(); }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
