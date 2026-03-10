package com.zentao.controller;

import com.zentao.entity.Config;
import com.zentao.repository.ConfigRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP tutorial 一致：新手教程配置从 zt_config 读写（account.common.global.novice、account.tutorial.tasks.setting） */
@RestController
@RequestMapping("/api/tutorial")
@RequiredArgsConstructor
public class TutorialController {

    private final ConfigRepository configRepository;

    @GetMapping({ "/index", "" })
    public ResponseEntity<Map<String, Object>> index() {
        var user = getCurrentUser();
        String account = user != null ? user.getUsername() : "";
        Map<String, Object> data = new java.util.HashMap<>(Map.of(
                "currentGuide", "",
                "currentTask", "",
                "guides", List.of(),
                "setting", "",
                "novice", "1"
        ));
        if (!account.isEmpty()) {
            configRepository.findByOwnerAndModuleAndSectionAndKey_(account, "common", "global", "novice")
                    .ifPresent(c -> data.put("novice", c.getValue() != null ? c.getValue() : "1"));
            configRepository.findByOwnerAndModuleAndSectionAndKey_(account, "tutorial", "tasks", "setting")
                    .ifPresent(c -> data.put("setting", c.getValue() != null ? c.getValue() : ""));
        }
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    /** 与 PHP ajaxSetTasks 一致：保存教程任务设置 */
    @PostMapping("/setTasks")
    public ResponseEntity<Map<String, Object>> setTasks(@RequestBody Map<String, String> body) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        String finish = body != null && body.containsKey("finish") ? body.get("finish") : "keepAll";
        if ("keepAll".equals(finish)) return ResponseEntity.ok(Map.of("result", "fail", "message", "ajaxSetError"));
        String account = user.getUsername();
        configRepository.findByOwnerAndModuleAndSectionAndKey_(account, "tutorial", "tasks", "setting")
                .ifPresentOrElse(
                        c -> { c.setValue(finish); configRepository.save(c); },
                        () -> {
                            Config c = new Config();
                            c.setOwner(account);
                            c.setModule("tutorial");
                            c.setSection("tasks");
                            c.setKey_("setting");
                            c.setValue(finish);
                            c.setVision("");
                            configRepository.save(c);
                        }
                );
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP quit/ajaxQuit 一致：退出教程模式，设置 novice=0 */
    @PostMapping("/quit")
    public ResponseEntity<Map<String, Object>> quit() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success"));
        String account = user.getUsername();
        configRepository.findByOwnerAndModuleAndSectionAndKey_(account, "common", "global", "novice")
                .ifPresentOrElse(
                        c -> { c.setValue("0"); configRepository.save(c); },
                        () -> {
                            Config c = new Config();
                            c.setOwner(account);
                            c.setModule("common");
                            c.setSection("global");
                            c.setKey_("novice");
                            c.setValue("0");
                            c.setVision("");
                            configRepository.save(c);
                        }
                );
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }

    private UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up;
        return null;
    }
}
