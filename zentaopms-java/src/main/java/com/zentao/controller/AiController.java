package com.zentao.controller;

import com.zentao.entity.AiAgent;
import com.zentao.entity.AiModel;
import com.zentao.service.AiAgentService;
import com.zentao.service.AiModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP ai 一致：zt_ai_model（语言模型）、zt_ai_agent（Prompt/智能体）列表与查看 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiModelService aiModelService;
    private final AiAgentService aiAgentService;

    @GetMapping({ "/list", "/index", "" })
    public ResponseEntity<Map<String, Object>> list() {
        List<AiModel> models = aiModelService.getList();
        List<AiAgent> prompts = aiAgentService.getList();
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of("models", models, "prompts", prompts)
        ));
    }

    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> models() {
        return ResponseEntity.ok(Map.of("result", "success", "data", aiModelService.getList()));
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<Map<String, Object>> modelView(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return aiModelService.getById(id)
                .map(m -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/models")
    public ResponseEntity<Map<String, Object>> modelCreate(@RequestBody AiModel body) {
        body.setId(null);
        AiModel created = aiModelService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/models/{id}")
    public ResponseEntity<Map<String, Object>> modelEdit(@PathVariable int id, @RequestBody AiModel body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (aiModelService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        body.setId(id);
        body.setDeleted(0);
        aiModelService.update(body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/models/{id}")
    public ResponseEntity<Map<String, Object>> modelDelete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (aiModelService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        aiModelService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/models/{id}/enable")
    public ResponseEntity<Map<String, Object>> modelEnable(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        return aiModelService.enable(id)
                .map(m -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/models/{id}/disable")
    public ResponseEntity<Map<String, Object>> modelDisable(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        return aiModelService.disable(id)
                .map(m -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/prompts")
    public ResponseEntity<Map<String, Object>> prompts(@RequestParam(required = false) String module) {
        List<AiAgent> list = module != null && !module.isBlank()
                ? aiAgentService.getByModule(module)
                : aiAgentService.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/prompts/{id}")
    public ResponseEntity<Map<String, Object>> promptView(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return aiAgentService.getById(id)
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/prompts")
    public ResponseEntity<Map<String, Object>> promptCreate(@RequestBody AiAgent body) {
        body.setId(null);
        AiAgent created = aiAgentService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/prompts/{id}")
    public ResponseEntity<Map<String, Object>> promptEdit(@PathVariable int id, @RequestBody AiAgent body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (aiAgentService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        body.setId(id);
        body.setDeleted(0);
        aiAgentService.update(body);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/prompts/{id}")
    public ResponseEntity<Map<String, Object>> promptDelete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (aiAgentService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        aiAgentService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/prompts/{id}/publish")
    public ResponseEntity<Map<String, Object>> promptPublish(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        return aiAgentService.publish(id)
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/prompts/{id}/unpublish")
    public ResponseEntity<Map<String, Object>> promptUnpublish(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        return aiAgentService.unpublish(id)
                .map(p -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
