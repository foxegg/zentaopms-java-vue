package com.zentao.controller;

import com.zentao.entity.User;
import com.zentao.entity.Task;
import com.zentao.entity.Bug;
import com.zentao.entity.Story;
import com.zentao.service.ActionService;
import com.zentao.service.BugService;
import com.zentao.service.ExecutionService;
import com.zentao.service.StoryService;
import com.zentao.service.TaskService;
import com.zentao.service.TestCaseService;
import com.zentao.service.TestTaskService;
import com.zentao.service.TodoService;
import com.zentao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户模块 - 对应 module/user
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ActionService actionService;
    private final TaskService taskService;
    private final BugService bugService;
    private final StoryService storyService;
    private final TodoService todoService;
    private final TestTaskService testTaskService;
    private final TestCaseService testCaseService;
    private final ExecutionService executionService;

    /** 与 PHP user list 一致：分页；可选 account、dept 筛选 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String account,
            @RequestParam(required = false, defaultValue = "0") int dept,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        Specification<User> spec = null;
        if ((account != null && !account.isBlank()) || dept > 0) {
            spec = (root, q, cb) -> {
                java.util.List<jakarta.persistence.criteria.Predicate> preds = new java.util.ArrayList<>();
                if (account != null && !account.isBlank()) {
                    preds.add(cb.like(root.get("account"), "%" + account.replace("%", "\\%") + "%"));
                }
                if (dept > 0) {
                    preds.add(cb.equal(root.get("dept"), dept));
                }
                return preds.isEmpty() ? null : cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
            };
        }
        var page = userService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of(
                        "recTotal", page.getTotalElements(),
                        "recPerPage", recPerPage,
                        "pageID", pageID
                )
        ));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(@RequestParam(defaultValue = "nodeleted") String mode) {
        List<User> users = userService.getPairs(mode);
        var pairs = users.stream().collect(java.util.stream.Collectors.toMap(User::getId, User::getRealname, (a, b) -> a));
        return ResponseEntity.ok(Map.of("result", "success", "data", pairs));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return userService.getById(id)
                .map(user -> ResponseEntity.ok(Map.of("result", "success", "data", user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> viewByAccount(@RequestParam String account) {
        return userService.getByAccount(account != null ? account : "")
                .map(user -> ResponseEntity.ok(Map.of("result", "success", "data", user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(
            @RequestParam String account,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var actions = actionService.getByActor(account != null ? account : "", org.springframework.data.domain.PageRequest.of(0, recPerPage));
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }

    @GetMapping("/{id}/todo")
    public ResponseEntity<Map<String, Object>> todo(@PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        org.springframework.data.jpa.domain.Specification<Task> taskSpec = (root, q, cb) -> cb.equal(root.get("assignedTo"), account);
        var taskPage = taskService.getList(taskSpec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        org.springframework.data.jpa.domain.Specification<Bug> bugSpec = (root, q, cb) -> cb.equal(root.get("assignedTo"), account);
        var bugPage = bugService.getList(bugSpec, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        List<Story> stories = storyService.getByAssignedTo(account);
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of(
                        "tasks", taskPage.getContent(),
                        "bugs", bugPage.getContent(),
                        "stories", stories
                ),
                "pager", Map.of(
                        "recTotal", taskPage.getTotalElements() + bugPage.getTotalElements() + stories.size(),
                        "recPerPage", recPerPage,
                        "pageID", pageID
                )
        ));
    }

    @GetMapping("/{id}/task")
    public ResponseEntity<Map<String, Object>> taskList(@PathVariable int id,
            @RequestParam(defaultValue = "assignedTo") String type,
            @RequestParam(defaultValue = "id_desc") String orderBy,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        String field = "assignedTo";
        if ("openedBy".equals(type)) field = "openedBy";
        else if ("closedBy".equals(type)) field = "closedBy";
        else if ("finishedBy".equals(type)) field = "finishedBy";
        final String f = field;
        org.springframework.data.jpa.domain.Specification<Task> spec = (root, q, cb) -> cb.equal(root.get(f), account);
        Sort sort = toSort(orderBy, "id");
        var page = taskService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage, sort));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @GetMapping("/{id}/bug")
    public ResponseEntity<Map<String, Object>> bugList(@PathVariable int id,
            @RequestParam(defaultValue = "assignedTo") String type,
            @RequestParam(defaultValue = "id_desc") String orderBy,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        String field = "assignedTo";
        if ("openedBy".equals(type)) field = "openedBy";
        else if ("resolvedBy".equals(type)) field = "resolvedBy";
        else if ("closedBy".equals(type)) field = "closedBy";
        final String f = field;
        org.springframework.data.jpa.domain.Specification<Bug> spec = (root, q, cb) -> cb.equal(root.get(f), account);
        Sort sort = toSort(orderBy, "id");
        var page = bugService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage, sort));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @GetMapping("/{id}/story")
    public ResponseEntity<Map<String, Object>> storyList(@PathVariable int id,
            @RequestParam(defaultValue = "story") String storyType,
            @RequestParam(defaultValue = "assignedTo") String type,
            @RequestParam(defaultValue = "id_desc") String orderBy,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        String field = "assignedTo";
        if ("openedBy".equals(type)) field = "openedBy";
        else if ("closedBy".equals(type)) field = "closedBy";
        else if ("reviewedBy".equals(type)) field = "reviewedBy";
        final String f = field;
        org.springframework.data.jpa.domain.Specification<Story> spec = (root, q, cb) -> cb.equal(root.get(f), account);
        if (storyType != null && !storyType.isEmpty() && !"story".equals(storyType)) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("type"), storyType));
        }
        Sort sort = toSort(orderBy, "id");
        var page = storyService.getList(spec, PageRequest.of(Math.max(0, pageID - 1), recPerPage > 0 ? recPerPage : 20, sort));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<Map<String, Object>> profile(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return userService.getById(id)
                .map(user -> ResponseEntity.ok(Map.of("result", "success", "data", user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/testtask")
    public ResponseEntity<Map<String, Object>> testtaskList(@PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        var page = testTaskService.getByOwner(account, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @GetMapping("/{id}/testcase")
    public ResponseEntity<Map<String, Object>> testcaseList(@PathVariable int id,
            @RequestParam(defaultValue = "case2Him") String type,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        PageRequest pageRequest = PageRequest.of(Math.max(0, pageID - 1), recPerPage);
        if ("caseByHim".equals(type)) {
            var page = testCaseService.getByOpenedBy(account, pageRequest);
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", page.getContent(),
                    "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
            ));
        }
        var page = testCaseService.getRunsByAssignedTo(account, pageRequest);
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @GetMapping("/{id}/execution")
    public ResponseEntity<Map<String, Object>> executionList(@PathVariable int id,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        String account = userService.getById(id).map(User::getAccount).orElse(null);
        if (account == null || account.isEmpty()) return ResponseEntity.notFound().build();
        var page = executionService.getByAccount(account, status, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody User user) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        user.setId(id);
        userService.update(user);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        userService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchCreate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<User> users) {
        List<Integer> ids = userService.batchCreate(users);
        return ResponseEntity.ok(Map.of("result", "success", "data", ids));
    }

    @PostMapping("/batchEdit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> userIds = toIntList(body.get("userIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        userService.batchEdit(userIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> unlock(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        userService.unlock(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/resetPassword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        String newPassword = body != null ? body.get("password") : null;
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/unbind")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> unbind(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (userService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        userService.unbind(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private static Sort toSort(String orderBy, String defaultField) {
        if (orderBy == null || orderBy.isEmpty()) return Sort.by(Sort.Direction.DESC, defaultField);
        boolean desc = orderBy.endsWith("_desc");
        String prop = orderBy.replace("_desc", "").replace("_asc", "");
        if (prop.isEmpty()) prop = defaultField;
        return Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, prop);
    }

    private static List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList();
        }
        return List.of();
    }
}
