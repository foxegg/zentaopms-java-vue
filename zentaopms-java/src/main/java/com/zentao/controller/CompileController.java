package com.zentao.controller;

import com.zentao.entity.Compile;
import com.zentao.service.CompileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 构建记录模块 - 对应 module/compile
 */
@RestController
@RequestMapping("/api/compile")
@RequiredArgsConstructor
public class CompileController {

    private final CompileService compileService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer jobID) {
        List<Compile> list = jobID != null && jobID > 0
                ? compileService.getByJob(jobID)
                : compileService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return compileService.getById(id)
                .map(c -> ResponseEntity.ok(Map.of("result", "success", "data", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Compile compile) {
        Compile created = compileService.create(compile);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Compile compile) {
        compile.setId(id);
        compileService.update(compile);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        compileService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
