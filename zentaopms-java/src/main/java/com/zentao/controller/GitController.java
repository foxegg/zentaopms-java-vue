package com.zentao.controller;

import com.zentao.entity.Repo;
import com.zentao.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP git 一致：使用 zt_repo，scm=git；list/view 已接 Repo，create/edit/delete 对接 RepoService */
@RestController
@RequestMapping("/api/git")
@RequiredArgsConstructor
public class GitController {

    private static final String SCM_GIT = "git";

    private final RepoService repoService;

    @GetMapping({ "/list", "" })
    public ResponseEntity<Map<String, Object>> list() {
        return ResponseEntity.ok(Map.of("result", "success", "data", repoService.getAll()));
    }

    @GetMapping("/repos")
    public ResponseEntity<Map<String, Object>> getRepos() {
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
        Repo repo = mapToRepo(body);
        repo.setId(null);
        repo.setScm(SCM_GIT);
        Repo created = repoService.create(repo);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Map<String, Object> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        Repo existing = repoService.getById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        applyBodyToRepo(existing, body);
        existing.setScm(SCM_GIT);
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

    public static Repo mapToRepo(Map<String, Object> body) {
        Repo r = new Repo();
        if (body != null) applyBodyToRepo(r, body);
        return r;
    }

    public static void applyBodyToRepo(Repo r, Map<String, Object> body) {
        if (body == null) return;
        if (body.containsKey("name")) r.setName(body.get("name") != null ? String.valueOf(body.get("name")) : null);
        if (body.containsKey("path")) r.setPath(body.get("path") != null ? String.valueOf(body.get("path")) : null);
        if (body.containsKey("prefix")) r.setPrefix(body.get("prefix") != null ? String.valueOf(body.get("prefix")) : null);
        if (body.containsKey("encoding")) r.setEncoding(body.get("encoding") != null ? String.valueOf(body.get("encoding")) : null);
        if (body.containsKey("client")) r.setClient(body.get("client") != null ? String.valueOf(body.get("client")) : null);
        if (body.containsKey("account")) r.setAccount(body.get("account") != null ? String.valueOf(body.get("account")) : null);
        if (body.containsKey("password")) r.setPassword(body.get("password") != null ? String.valueOf(body.get("password")) : null);
        if (body.containsKey("product")) r.setProduct(body.get("product") != null ? String.valueOf(body.get("product")) : "");
        if (body.containsKey("projects")) r.setProjects(body.get("projects") != null ? String.valueOf(body.get("projects")) : "");
        if (body.containsKey("description")) r.setDescription(body.get("description") != null ? String.valueOf(body.get("description")) : null);
        if (body.containsKey("serviceHost")) r.setServiceHost(body.get("serviceHost") != null ? String.valueOf(body.get("serviceHost")) : null);
        if (body.containsKey("serviceProject")) r.setServiceProject(body.get("serviceProject") != null ? String.valueOf(body.get("serviceProject")) : null);
    }
}
