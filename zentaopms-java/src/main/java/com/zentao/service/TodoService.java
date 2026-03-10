package com.zentao.service;

import com.zentao.entity.Todo;
import com.zentao.repository.TodoRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final ActionService actionService;

    public Optional<Todo> getById(int id) {
        return todoRepository.findById(id).filter(t -> t.getDeleted() == 0);
    }

    public List<Todo> getByAccount(String account) {
        return todoRepository.findByAccountAndDeletedOrderByDateDesc(account, 0);
    }

    public List<Todo> getByAssignedTo(String account) {
        return todoRepository.findByAssignedToAndDeleted(account, 0);
    }

    public List<Todo> getMyTodos() {
        return getMyTodos(null);
    }

    /** 当前用户待办列表，status 为 wait/done/closed 时按状态筛选，与 PHP todo 列表一致 */
    public List<Todo> getMyTodos(String status) {
        var user = getCurrentUser();
        if (user == null) return List.of();
        if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
            return todoRepository.findByAccountAndStatusAndDeletedOrderByDateDesc(user.getUsername(), status, 0);
        }
        return getByAccount(user.getUsername());
    }

    public Todo create(Todo todo) {
        var user = getCurrentUser();
        if (user != null && todo.getAccount() == null) {
            todo.setAccount(user.getUsername());
        }
        todo.setDeleted(0);
        Todo saved = todoRepository.save(todo);
        actionService.create("todo", saved.getId(), "opened");
        return saved;
    }

    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    @Transactional
    public Todo start(int todoId) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setStatus("doing");
        Todo saved = todoRepository.save(t);
        actionService.create("todo", todoId, "started");
        return saved;
    }

    @Transactional
    public Todo activate(int todoId) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setStatus("wait");
        Todo saved = todoRepository.save(t);
        actionService.create("todo", todoId, "activated");
        return saved;
    }

    @Transactional
    public Todo close(int todoId) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setStatus("closed");
        t.setClosedBy(getCurrentAccount());
        t.setClosedDate(LocalDateTime.now());
        Todo saved = todoRepository.save(t);
        actionService.create("todo", todoId, "closed");
        return saved;
    }

    @Transactional
    public Todo assignTo(int todoId, String assignedTo) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setAssignedTo(assignedTo);
        t.setAssignedBy(getCurrentAccount());
        t.setAssignedDate(LocalDateTime.now());
        Todo saved = todoRepository.save(t);
        actionService.create("todo", todoId, "Assigned", "", assignedTo);
        return saved;
    }

    @Transactional
    public Todo finish(int todoId) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setStatus("done");
        t.setFinishedBy(getCurrentAccount());
        t.setFinishedDate(LocalDateTime.now());
        Todo saved = todoRepository.save(t);
        actionService.create("todo", todoId, "finished", "", "done");
        return saved;
    }

    @Transactional
    public void delete(int todoId) {
        Todo t = getById(todoId).orElseThrow(() -> new RuntimeException("待办不存在"));
        t.setDeleted(1);
        todoRepository.save(t);
        actionService.create("todo", todoId, "deleted");
    }

    @Transactional
    public void batchFinish(List<Integer> todoIds) {
        for (Integer id : todoIds) finish(id);
    }

    @Transactional
    public void batchClose(List<Integer> todoIds) {
        for (Integer id : todoIds) close(id);
    }

    /** 批量添加待办，与 PHP todo batchCreate 一致 */
    @Transactional
    public List<Integer> batchCreate(List<Todo> todos) {
        if (todos == null || todos.isEmpty()) return List.of();
        List<Integer> ids = new ArrayList<>();
        for (Todo t : todos) {
            ids.add(create(t).getId());
        }
        return ids;
    }

    /** 批量编辑待办，与 PHP todo batchEdit/batchUpdate 一致 */
    @Transactional
    public void batchEdit(List<Integer> todoIds, Map<String, Object> fields) {
        if (todoIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : todoIds) {
            if (id == null || id <= 0) continue;
            getById(id).ifPresent(t -> {
                if (fields.containsKey("pri") && fields.get("pri") instanceof Number n) t.setPri(n.intValue());
                if (fields.containsKey("status")) t.setStatus(String.valueOf(fields.get("status")));
                if (fields.containsKey("type")) t.setType(String.valueOf(fields.get("type")));
                if (fields.containsKey("name")) t.setName(String.valueOf(fields.get("name")));
                if (fields.containsKey("date")) {
                    Object v = fields.get("date");
                    if (v instanceof String s) t.setDate(LocalDate.parse(s));
                    else if (v instanceof LocalDate d) t.setDate(d);
                }
                if (fields.containsKey("begin")) t.setBegin(String.valueOf(fields.get("begin")));
                if (fields.containsKey("end")) t.setEnd(String.valueOf(fields.get("end")));
                if (fields.containsKey("assignedTo")) {
                    t.setAssignedTo(String.valueOf(fields.get("assignedTo")));
                    t.setAssignedBy(getCurrentAccount());
                    t.setAssignedDate(LocalDateTime.now());
                }
                if (fields.containsKey("objectID") && fields.get("objectID") instanceof Number n) t.setObjectID(n.intValue());
                todoRepository.save(t);
                if ("done".equals(t.getStatus())) actionService.create("todo", id, "finished", "", "done");
            });
        }
    }

    private String getCurrentAccount() {
        var user = getCurrentUser();
        return user != null ? user.getUsername() : "";
    }

    private UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up;
        }
        return null;
    }
}
