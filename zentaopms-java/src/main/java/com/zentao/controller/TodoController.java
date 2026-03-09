package com.zentao.controller;

import com.zentao.entity.Todo;
import com.zentao.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 待办模块 - 对应 module/todo
 */
@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        List<Todo> todos = todoService.getMyTodos();
        return ResponseEntity.ok(Map.of("result", "success", "data", todos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return todoService.getById(id)
                .map(t -> ResponseEntity.ok(Map.of("result", "success", "data", t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Todo todo) {
        Todo created = todoService.create(todo);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Todo todo) {
        todo.setId(id);
        todoService.update(todo);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> start(@PathVariable int id) {
        Todo t = todoService.start(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        Todo t = todoService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        Todo t = todoService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/assignTo")
    public ResponseEntity<Map<String, Object>> assignTo(@PathVariable int id, @RequestBody Map<String, String> body) {
        Todo t = todoService.assignTo(id, body.getOrDefault("assignedTo", ""));
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Map<String, Object>> finish(@PathVariable int id) {
        Todo t = todoService.finish(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        todoService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchFinish")
    public ResponseEntity<Map<String, Object>> batchFinish(@RequestBody Map<String, Object> body) {
        List<Integer> ids = toIntList(body.get("todoIds"));
        todoService.batchFinish(ids);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchClose")
    public ResponseEntity<Map<String, Object>> batchClose(@RequestBody Map<String, Object> body) {
        List<Integer> ids = toIntList(body.get("todoIds"));
        todoService.batchClose(ids);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().map(o -> o instanceof Number n ? n.intValue() : Integer.parseInt(o.toString())).toList();
        }
        return List.of();
    }
}
