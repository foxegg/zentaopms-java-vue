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

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "0") int projectID) {
        List<Group> groups = projectID > 0
                ? groupRepository.findByProject(projectID)
                : groupRepository.findAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        group.setId(id);
        groupRepository.save(group);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        groupRepository.findById(id).ifPresent(g -> {
            if (g.getProject() != null && g.getProject() > 0) {
                groupRepository.delete(g);
            }
        });
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Map<String, Object>> manageMember(@PathVariable int id) {
        List<UserGroup> members = groupService.getMembers(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", members));
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> addMember(@PathVariable int id, @RequestBody Map<String, String> body) {
        String account = body != null ? body.get("account") : null;
        String project = body != null ? body.get("project") : null;
        groupService.addMember(id, account != null ? account : "", project);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}/members/{account}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> removeMember(@PathVariable int id, @PathVariable String account) {
        groupService.removeMember(id, account);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/copy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> copy(@RequestBody Map<String, Object> body) {
        int groupID = body.get("groupID") instanceof Number n ? n.intValue() : 0;
        @SuppressWarnings("unchecked")
        Map<String, Object> options = (Map<String, Object>) body.get("options");
        return groupService.copyGroup(groupID, options != null ? options : Map.of())
                .map(g -> ResponseEntity.ok(Map.of("result", "success", "id", g.getId(), "data", g)))
                .orElse(ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "源组不存在")));
    }

    @GetMapping("/{id}/priv")
    public ResponseEntity<Map<String, Object>> managePriv(@PathVariable int id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", groupService.getPrivs(id)));
    }

    @PutMapping("/{id}/priv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> setPriv(@PathVariable int id, @RequestBody List<Map<String, String>> privs) {
        groupService.setPrivs(id, privs != null ? privs : List.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
