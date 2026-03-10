package com.zentao.controller;

import com.zentao.entity.Repo;
import com.zentao.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP gitea 一致：zt_repo scm=gitea；create/edit/delete 对接 RepoService */
@RestController
@RequestMapping("/api/gitea")
@RequiredArgsConstructor
public class GiteaController {

    private static final String SCM_GITEA = "gitea";

    private final RepoService repoService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", repoService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return repoService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        Repo repo = GitController.mapToRepo(body);
        repo.setId(null);
        repo.setScm(SCM_GITEA);
        Repo created = repoService.create(repo);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Repo existing = repoService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        GitController.applyBodyToRepo(existing, body);
        existing.setScm(SCM_GITEA);
        repoService.update(existing);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (repoService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        repoService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
