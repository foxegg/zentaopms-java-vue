package com.zentao.controller;

import com.zentao.entity.Group;
import com.zentao.entity.UserGroup;
import com.zentao.repository.GroupRepository;
import com.zentao.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限组模块 - 对应 module/group
 */
@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;

    /** 与 PHP/GroupService 一致：projectID=0 为全局组；vision 非空时按 project+vision 过滤 */
    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "0") int projectID,
            @RequestParam(required = false) String vision) {
        return ResponseEntity.ok(Map.of("result", "success", "data", groupService.getPairs(projectID, vision)));
    }

    /** 与 PHP group getList(projectID) 一致：按 project 过滤，projectID=0 表示全局组（project=0）；vision 非空时按 project+vision 过滤 */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int projectID,
            @RequestParam(required = false) String vision) {
        List<Group> groups = groupService.getList(projectID, vision);
        return ResponseEntity.ok(Map.of("result", "success", "data", groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return groupRepository.findById(id)
                .map(g -> ResponseEntity.ok(Map.of("result", "success", "data", g)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Group group) {
        Group created = groupRepository.save(group);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Group group) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (groupRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        group.setId(id);
        groupRepository.save(group);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致：仅允许删除项目权限组（project>0）；全局组（project=0）不可删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        var opt = groupRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var g = opt.get();
        if (g.getProject() == null || g.getProject() == 0) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "不能删除全局权限组"));
        }
        groupRepository.delete(g);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/members")
    public ResponseEntity<Map<String, Object>> manageMember(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        List<UserGroup> members = groupService.getMembers(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", members));
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> addMember(@PathVariable int id, @RequestBody Map<String, String> body) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (groupRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        String account = body != null ? body.get("account") : null;
        String project = body != null ? body.get("project") : null;
        groupService.addMember(id, account != null ? account : "", project);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}/members/{account}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> removeMember(@PathVariable int id, @PathVariable String account) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (groupRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        groupService.removeMember(id, account);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；groupID≤0 时返回 400 */
    @PostMapping("/copy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> copy(@RequestBody Map<String, Object> body) {
        int groupID = body.get("groupID") instanceof Number n ? n.intValue() : 0;
        if (groupID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid groupID"));
        @SuppressWarnings("unchecked")
        Map<String, Object> options = (Map<String, Object>) body.get("options");
        return groupService.copyGroup(groupID, options != null ? options : Map.of())
                .map(g -> ResponseEntity.ok(Map.of("result", "success", "id", g.getId(), "data", g)))
                .orElse(ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "源组不存在")));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/priv")
    public ResponseEntity<Map<String, Object>> managePriv(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("result", "success", "data", groupService.getPrivs(id)));
    }

    @PutMapping("/{id}/priv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> setPriv(@PathVariable int id, @RequestBody List<Map<String, String>> privs) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (groupRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        groupService.setPrivs(id, privs != null ? privs : List.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
