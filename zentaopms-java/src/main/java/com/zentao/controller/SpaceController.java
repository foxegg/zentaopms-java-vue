package com.zentao.controller;

import com.zentao.entity.Space;
import com.zentao.security.UserPrincipal;
import com.zentao.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP space 一致：zt_space，按 owner 列表/CRUD */
@RestController
@RequestMapping("/api/space")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false) String owner) {
        String account = owner != null ? owner : getCurrentAccount();
        List<Space> list = spaceService.getByOwner(account);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = getCurrentAccount();
        return (account != null ? spaceService.getByIdAndOwner(id, account) : spaceService.getById(id))
                .map(s -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Space body) {
        String account = getCurrentAccount();
        if (account != null && (body.getOwner() == null || body.getOwner().isBlank())) body.setOwner(account);
        body.setId(null);
        Space created = spaceService.create(body);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Space existing = spaceService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        String account = getCurrentAccount();
        if (account != null && !account.equals(existing.getOwner())) return ResponseEntity.notFound().build();
        if (body.containsKey("name") && body.get("name") != null) existing.setName(String.valueOf(body.get("name")));
        if (body.containsKey("k8space") && body.get("k8space") != null) existing.setK8space(String.valueOf(body.get("k8space")));
        if (body.containsKey("default") && body.get("default") != null) existing.setDefault_(((Number) body.get("default")).intValue());
        spaceService.update(existing);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (spaceService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        spaceService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return null;
    }
}
