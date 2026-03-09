package com.zentao.service;

import com.zentao.entity.Todo;
import com.zentao.repository.TodoRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        var user = getCurrentUser();
        if (user == null) return List.of();
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
