package com.zentao.controller;

import com.zentao.entity.Instance;
import com.zentao.service.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP instance 一致：zt_instance 列表/查看/创建/编辑/删除 */
@RestController
@RequestMapping("/api/instance")
@RequiredArgsConstructor
public class InstanceController {

    private final InstanceService instanceService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer space,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        List<Instance> list;
        if (space != null && space > 0) {
            list = instanceService.getBySpace(space);
        } else if (recPerPage > 0 && pageID >= 1) {
            var page = instanceService.getList(PageRequest.of(pageID - 1, recPerPage));
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", page.getContent(),
                    "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
        } else {
            list = instanceService.getList();
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return instanceService.getById(id)
                .map(i -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", i)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Instance body) {
        body.setId(null);
        Instance created = instanceService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Instance existing = instanceService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        applyBodyToInstance(existing, body);
        instanceService.update(existing);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private static void applyBodyToInstance(Instance e, Map<String, Object> body) {
        if (body == null) return;
        if (body.containsKey("name") && body.get("name") != null) e.setName(String.valueOf(body.get("name")));
        if (body.containsKey("space")) e.setSpace(body.get("space") instanceof Number n ? n.intValue() : 0);
        if (body.containsKey("solution")) e.setSolution(body.get("solution") instanceof Number n ? n.intValue() : 0);
        if (body.containsKey("appID")) e.setAppID(body.get("appID") instanceof Number n ? n.intValue() : 0);
        if (body.containsKey("appName")) e.setAppName(body.get("appName") != null ? String.valueOf(body.get("appName")) : null);
        if (body.containsKey("appVersion")) e.setAppVersion(body.get("appVersion") != null ? String.valueOf(body.get("appVersion")) : null);
        if (body.containsKey("status")) e.setStatus(body.get("status") != null ? String.valueOf(body.get("status")) : null);
        if (body.containsKey("domain")) e.setDomain(body.get("domain") != null ? String.valueOf(body.get("domain")) : null);
        if (body.containsKey("pinned")) e.setPinned(body.get("pinned") instanceof Number n ? n.intValue() : 0);
        if (body.containsKey("description")) e.setDescription(body.get("description") != null ? String.valueOf(body.get("description")) : null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (instanceService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        instanceService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
