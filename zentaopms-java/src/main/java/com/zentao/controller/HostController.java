package com.zentao.controller;

import com.zentao.entity.Host;
import com.zentao.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/host")
@RequiredArgsConstructor
public class HostController {
    private final HostService service;

    @GetMapping({ "/list", "", "/" })
    public ResponseEntity<Map<String, Object>> list() {
        List<Host> list = service.getList();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return service.getById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Host d) {
        Host created = service.create(d);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Host d) {
        d.setId(id);
        service.update(d);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP changeStatus 一致：online|offline */
    @PutMapping("/{id}/changeStatus")
    public ResponseEntity<Map<String, Object>> changeStatus(@PathVariable int id,
            @RequestParam(defaultValue = "online") String status,
            @RequestParam(required = false) String reason) {
        service.updateStatus(id, status, reason);
        return ResponseEntity.ok(Map.of("result", "success", "message", "saved"));
    }

    /** 与 PHP treemap 一致：type=serverroom|group */
    @GetMapping("/treemap")
    public ResponseEntity<Map<String, Object>> treemap(@RequestParam(defaultValue = "serverroom") String type) {
        return ResponseEntity.ok(Map.of("result", "success", "data", service.getTreemap(type)));
    }

    /** 与 PHP ajaxGetOS 一致：返回 [{text, value}] 供下拉 */
    @GetMapping("/os")
    public ResponseEntity<List<Map<String, String>>> ajaxGetOS(@RequestParam(defaultValue = "os") String type) {
        List<Map<String, String>> items = service.getOSOptions(type);
        return ResponseEntity.ok(items);
    }
}
