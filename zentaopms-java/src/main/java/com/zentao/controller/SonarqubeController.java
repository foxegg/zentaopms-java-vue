package com.zentao.controller;

import com.zentao.entity.Pipeline;
import com.zentao.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP sonarqube 一致：使用 zt_pipeline 表 type=sonarqube */
@RestController
@RequestMapping("/api/sonarqube")
@RequiredArgsConstructor
public class SonarqubeController {
    private static final String TYPE_SONARQUBE = "sonarqube";
    private final PipelineService pipelineService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        List<Pipeline> list = pipelineService.getListByType(TYPE_SONARQUBE);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return pipelineService.getById(id)
                .filter(p -> TYPE_SONARQUBE.equals(p.getType()))
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Pipeline p) {
        p.setType(TYPE_SONARQUBE);
        p.setId(null);
        Pipeline created = pipelineService.create(p);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Pipeline p) {
        p.setId(id);
        p.setType(TYPE_SONARQUBE);
        pipelineService.update(p);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        pipelineService.getById(id).filter(p -> TYPE_SONARQUBE.equals(p.getType())).ifPresent(x -> pipelineService.delete(id));
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
