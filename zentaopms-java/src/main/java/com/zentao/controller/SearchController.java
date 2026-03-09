package com.zentao.controller;

import com.zentao.entity.UserQuery;
import com.zentao.repository.BugRepository;
import com.zentao.repository.StoryRepository;
import com.zentao.repository.TaskRepository;
import com.zentao.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 搜索模块 - 对应 module/search
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final BugRepository bugRepository;
    private final TaskRepository taskRepository;
    private final StoryRepository storyRepository;
    private final UserQueryService userQueryService;

    @GetMapping("/query")
    public ResponseEntity<Map<String, Object>> query(
            @RequestParam(required = false, defaultValue = "") String words,
            @RequestParam(required = false) String module,
            @RequestParam(defaultValue = "20") int limit) {
        List<Map<String, Object>> results = new ArrayList<>();
        String q = words != null ? words.trim() : "";
        if (q.isEmpty()) {
            return ResponseEntity.ok(Map.of("result", "success", "data", results));
        }

        var pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        if (module == null || module.isEmpty() || "bug".equals(module)) {
            bugRepository.findByTitleContainingAndDeleted(q, 0, pageable)
                    .forEach(b -> results.add(Map.of(
                            "objectType", "bug",
                            "id", b.getId(),
                            "title", b.getTitle() != null ? b.getTitle() : "",
                            "status", b.getStatus() != null ? b.getStatus() : ""
                    )));
        }
        if (module == null || module.isEmpty() || "task".equals(module)) {
            taskRepository.findByNameContainingAndDeleted(q, 0, pageable)
                    .forEach(t -> results.add(Map.of(
                            "objectType", "task",
                            "id", t.getId(),
                            "title", t.getName() != null ? t.getName() : "",
                            "status", t.getStatus() != null ? t.getStatus() : ""
                    )));
        }
        if (module == null || module.isEmpty() || "story".equals(module)) {
            storyRepository.findByTitleContainingAndDeleted(q, 0, pageable)
                    .forEach(s -> results.add(Map.of(
                            "objectType", "story",
                            "id", s.getId(),
                            "title", s.getTitle() != null ? s.getTitle() : "",
                            "status", s.getStatus() != null ? s.getStatus() : ""
                    )));
        }

        return ResponseEntity.ok(Map.of("result", "success", "data", results));
    }

    @GetMapping("/getQuery")
    public ResponseEntity<Map<String, Object>> getQuery(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String account) {
        String acc = account != null ? account : "";
        List<UserQuery> list = userQueryService.getByAccountAndModule(acc, module != null ? module : "");
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @PostMapping("/saveQuery")
    public ResponseEntity<Map<String, Object>> saveQuery(@RequestBody UserQuery query) {
        UserQuery saved = userQueryService.save(query);
        return ResponseEntity.ok(Map.of("result", "success", "id", saved.getId()));
    }

    @DeleteMapping("/deleteQuery/{id}")
    public ResponseEntity<Map<String, Object>> deleteQuery(@PathVariable int id) {
        userQueryService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
