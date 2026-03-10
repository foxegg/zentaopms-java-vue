package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP datatable 一致：列表显示/列配置存 zt_config（account.module.method.showModule 等） */
@RestController
@RequestMapping("/api/datatable")
@RequiredArgsConstructor
public class DatatableController {

    private final ConfigRepository configRepository;

    @PostMapping("/ajaxSave")
    public ResponseEntity<Map<String, Object>> ajaxSave(@RequestBody Map<String, Object> body) {
        UserPrincipal user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "guest."));
        String module = body != null && body.get("currentModule") != null ? body.get("currentModule").toString() : null;
        String method = body != null && body.get("currentMethod") != null ? body.get("currentMethod").toString() : null;
        Object value = body != null ? body.get("value") : null;
        if (module == null || method == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "missing params"));
        String account = user.getUsername();
        String key = "showModule";
        configRepository.findByOwnerAndModuleAndSectionAndKey_(account, module, method, key)
                .ifPresentOrElse(
                        c -> { c.setValue(value != null ? value.toString() : ""); configRepository.save(c); },
                        () -> {
                            Config c = new Config();
                            c.setOwner(account);
                            c.setModule(module);
                            c.setSection(method);
                            c.setKey_(key);
                            c.setValue(value != null ? value.toString() : "");
                            c.setVision("");
                            configRepository.save(c);
                        }
                );
        if (body != null && body.get("allModule") != null) {
            String allModuleVal = body.get("allModule").toString();
            String acc = user.getUsername();
            configRepository.findByOwnerAndModuleAndSectionAndKey_(acc, "execution", "task", "allModule")
                    .ifPresentOrElse(
                            cx -> { cx.setValue(allModuleVal); configRepository.save(cx); },
                            () -> {
                                Config cx = new Config();
                                cx.setOwner(acc);
                                cx.setModule("execution");
                                cx.setSection("task");
                                cx.setKey_("allModule");
                                cx.setValue(allModuleVal);
                                cx.setVision("");
                                configRepository.save(cx);
                            }
                    );
        }
        return ResponseEntity.ok(Map.of("result", "success", "closeModal", true, "load", true));
    }

    private UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up;
        return null;
    }

    @PostMapping("/ajaxSaveFields")
    public ResponseEntity<Map<String, Object>> ajaxSaveFields(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String extra,
            @RequestBody(required = false) Map<String, Object> body) {
        UserPrincipal user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "guest."));
        return ResponseEntity.ok(Map.of("result", "success", "load", true));
    }

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() { return PlaceholderResponses.emptyList(); }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
