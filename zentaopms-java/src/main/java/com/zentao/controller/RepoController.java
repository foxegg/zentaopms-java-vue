package com.zentao.controller;

import com.zentao.entity.Repo;
import com.zentao.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 代码库模块 - 对应 module/repo
 */
@RestController
@RequestMapping("/api/repo")
@RequiredArgsConstructor
public class RepoController {

    private final RepoService repoService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        List<Repo> list = repoService.getAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return repoService.getById(id)
                .map(r -> ResponseEntity.ok(Map.of("result", "success", "data", r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Repo repo) {
        Repo created = repoService.create(repo);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Repo repo) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (repoService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        repo.setId(id);
        repoService.update(repo);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (repoService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        repoService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 创建代码分支，与 PHP repo createBranch 对应（objectID 为 taskID）；objectID≤0 或 repoID≤0 时返回 400 */
    @PostMapping("/createBranch")
    public ResponseEntity<Map<String, Object>> createBranch(@RequestParam(defaultValue = "0") int objectID,
            @RequestParam(defaultValue = "0") int repoID) {
        if (objectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid objectID"));
        if (repoID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid repoID"));
        repoService.createBranch(objectID, repoID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 解除代码分支关联，与 PHP repo unlinkBranch 对应；objectID≤0 时返回 400 */
    @PostMapping("/unlinkBranch")
    public ResponseEntity<Map<String, Object>> unlinkBranch(@RequestParam(defaultValue = "0") int objectID) {
        if (objectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid objectID"));
        repoService.unlinkBranch(objectID);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 提交日志列表，对应 PHP repo log；id≤0 时返回 404，暂无真实 git 集成时返回空列表 */
    @GetMapping("/{id}/log")
    public ResponseEntity<Map<String, Object>> log(
            @PathVariable int id,
            @RequestParam(required = false) String branchID,
            @RequestParam(defaultValue = "0") int objectID,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "50") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        if (repoService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", List.<Map<String, Object>>of(),
                "pager", Map.of("recTotal", 0, "recPerPage", recPerPage, "pageID", pageID)));
    }

    /** 代码库维护列表，对应 PHP repo maintain；返回分页的 repo 列表 */
    @GetMapping("/maintain")
    public ResponseEntity<Map<String, Object>> maintain(
            @RequestParam(defaultValue = "0") int objectID,
            @RequestParam(defaultValue = "id_desc") String orderBy,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        List<Repo> all = repoService.getAll();
        int total = all.size();
        int from = Math.max(0, (pageID - 1) * recPerPage);
        int to = Math.min(total, from + recPerPage);
        List<Repo> page = from < to ? all.subList(from, to) : List.of();
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page,
                "pager", Map.of("recTotal", total, "recPerPage", recPerPage, "pageID", pageID)));
    }
}
