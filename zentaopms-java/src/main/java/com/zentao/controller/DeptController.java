package com.zentao.controller;

import com.zentao.entity.Dept;
import com.zentao.entity.User;
import com.zentao.repository.DeptRepository;
import com.zentao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门模块 - 对应 module/dept
 */
@RestController
@RequestMapping("/api/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptRepository deptRepository;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "0") int parentID) {
        List<Dept> depts = deptRepository.findByParentOrderByOrderNumAsc(parentID);
        return ResponseEntity.ok(Map.of("result", "success", "data", depts));
    }

    @GetMapping("/browse")
    public ResponseEntity<Map<String, Object>> browse(@RequestParam(defaultValue = "0") int parentID) {
        return list(parentID);
    }

    @GetMapping("/tree")
    public ResponseEntity<Map<String, Object>> tree() {
        List<Dept> depts = deptRepository.findAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", depts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return deptRepository.findById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Dept dept) {
        Dept created = deptRepository.save(dept);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Dept dept) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        dept.setId(id);
        deptRepository.save(dept);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP dept delete 一致：有子部门或部门下有人时禁止删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (!deptRepository.findByParentOrderByOrderNumAsc(id).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "This Department has child departments. You cannot delete it!"));
        }
        if (!userRepository.findByDeptAndDeleted(id, 0).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "This Department has users. You cannot delete it!"));
        }
        deptRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/updateOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateOrder(@RequestBody List<Map<String, Object>> orders) {
        if (orders != null) {
            for (Map<String, Object> o : orders) {
                Object idObj = o.get("id");
                Object orderObj = o.get("order");
                if (idObj instanceof Number idNum && orderObj instanceof Number orderNum) {
                    deptRepository.findById(idNum.intValue()).ifPresent(d -> {
                        d.setOrderNum(orderNum.intValue());
                        deptRepository.save(d);
                    });
                }
            }
        }
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致；id≤0 时返回 data: [] */
    @GetMapping("/{id}/users")
    public ResponseEntity<Map<String, Object>> users(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        List<User> users = userRepository.findByDeptAndDeleted(id, 0);
        return ResponseEntity.ok(Map.of("result", "success", "data", users));
    }
}
