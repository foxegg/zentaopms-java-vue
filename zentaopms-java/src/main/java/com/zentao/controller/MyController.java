package com.zentao.controller;

import com.zentao.entity.User;
import com.zentao.security.UserPrincipal;
import com.zentao.service.ActionService;
import com.zentao.service.TaskService;
import com.zentao.service.TodoService;
import com.zentao.service.BugService;
import com.zentao.service.StoryService;
import com.zentao.service.UserService;
import com.zentao.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的地盘 - 对应 module/my
 */
@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {

    private final TodoService todoService;
    private final TaskService taskService;
    private final BugService bugService;
    private final StoryService storyService;
    private final ActionService actionService;
    private final UserService userService;
    private final ScoreService scoreService;

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> index() {
        var user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("user", user.getUser());
        data.put("todos", todoService.getMyTodos());
        data.put("tasks", taskService.getByAssignedTo(user.getUsername()));
        data.put("bugs", bugService.getByAssignedTo(user.getUsername()));
        data.put("stories", storyService.getByAssignedTo(user.getUsername()));

        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/todo")
    public ResponseEntity<Map<String, Object>> todo() {
        return ResponseEntity.ok(Map.of("result", "success", "data", todoService.getMyTodos()));
    }

    @GetMapping("/task")
    public ResponseEntity<Map<String, Object>> task() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", java.util.List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", taskService.getByAssignedTo(user.getUsername())));
    }

    @GetMapping("/bug")
    public ResponseEntity<Map<String, Object>> bug() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", java.util.List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", bugService.getByAssignedTo(user.getUsername())));
    }

    @GetMapping("/story")
    public ResponseEntity<Map<String, Object>> story() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        return ResponseEntity.ok(Map.of("result", "success", "data", storyService.getByAssignedTo(user.getUsername())));
    }

    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(
            @RequestParam(defaultValue = "today") String type,
            @RequestParam(defaultValue = "0") int recTotal,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        var actions = actionService.getByActor(user.getUsername(), PageRequest.of(0, recPerPage > 0 ? recPerPage : 20));
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        User profile = userService.getByAccount(user.getUsername()).orElse(null);
        return ResponseEntity.ok(Map.of("result", "success", "data", profile != null ? profile : user.getUser()));
    }

    @GetMapping("/preference")
    public ResponseEntity<Map<String, Object>> preference() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("message", "preference placeholder");
        return ResponseEntity.ok(Map.of("result", "success", "data", prefs));
    }

    @PutMapping("/preference")
    public ResponseEntity<Map<String, Object>> updatePreference(@RequestBody Map<String, Object> body) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> body) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        String oldPassword = body != null ? body.get("oldPassword") : null;
        String newPassword = body != null ? body.get("newPassword") : null;
        boolean ok = userService.changePassword(user.getUsername(), oldPassword, newPassword);
        return ResponseEntity.ok(Map.of("result", ok ? "success" : "fail", "message", ok ? "" : "原密码错误或新密码无效"));
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> editProfile(@RequestBody Map<String, Object> body) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        User u = userService.updateProfile(user.getUsername(), body != null ? body : Map.of());
        return ResponseEntity.ok(Map.of("result", "success", "data", u));
    }

    @GetMapping("/score")
    public ResponseEntity<Map<String, Object>> score(
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "fail", "message", "未登录"));
        var list = scoreService.getList(user.getUsername(), pageID, recPerPage);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/work")
    public ResponseEntity<Map<String, Object>> work() {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("tasks", List.of(), "bugs", List.of(), "todos", List.of())));
        Map<String, Object> data = new HashMap<>();
        data.put("tasks", taskService.getByAssignedTo(user.getUsername()));
        data.put("bugs", bugService.getByAssignedTo(user.getUsername()));
        data.put("todos", todoService.getMyTodos());
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendar(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        var user = getCurrentUser();
        if (user == null) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        Map<String, Object> data = new HashMap<>();
        data.put("tasks", taskService.getByAssignedTo(user.getUsername()));
        data.put("todos", todoService.getMyTodos());
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    private UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up;
        }
        return null;
    }
}
