package com.zentao.controller;

import com.zentao.entity.Mark;
import com.zentao.security.UserPrincipal;
import com.zentao.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP mark 一致：zt_mark 收藏/标星；list 按 account、objectType，view/create/delete 对接 MarkService */
@RestController
@RequestMapping("/api/mark")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String objectType) {
        String acc = account != null ? account : getCurrentAccount();
        List<Mark> list = markService.getByAccountAndObjectType(acc, objectType);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return markService.getById(id)
                .map(m -> ResponseEntity.<Map<String, Object>>ok(Map.of("result", "success", "data", m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        Mark m = new Mark();
        if (body != null) {
            if (body.get("objectType") != null) m.setObjectType(String.valueOf(body.get("objectType")));
            if (body.get("objectID") != null) m.setObjectID(((Number) body.get("objectID")).intValue());
            if (body.get("version") != null) m.setVersion(String.valueOf(body.get("version"))); else m.setVersion("1");
            if (body.get("account") != null) m.setAccount(String.valueOf(body.get("account"))); else m.setAccount(getCurrentAccount() != null ? getCurrentAccount() : "");
            if (body.get("mark") != null) m.setMark(String.valueOf(body.get("mark"))); else m.setMark("view");
            if (body.get("extra") != null) m.setExtra(String.valueOf(body.get("extra")));
        }
        int oid = m.getObjectID() != null ? m.getObjectID() : 0;
        if (oid <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid objectID"));
        if (m.getAccount().isBlank()) m.setAccount(getCurrentAccount() != null ? getCurrentAccount() : "");
        Mark created = markService.create(m);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Mark existing = markService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        if (body != null) {
            if (body.containsKey("mark") && body.get("mark") != null) existing.setMark(String.valueOf(body.get("mark")));
            if (body.containsKey("extra")) existing.setExtra(body.get("extra") != null ? String.valueOf(body.get("extra")) : "");
        }
        markService.update(existing);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (markService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        markService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return null;
    }
}
