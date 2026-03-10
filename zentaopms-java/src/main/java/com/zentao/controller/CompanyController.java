package com.zentao.controller;

import com.zentao.entity.Action;
import com.zentao.entity.Company;
import com.zentao.service.ActionService;
import com.zentao.repository.CompanyRepository;
import com.zentao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 组织/公司模块 - 对应 module/company
 */
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final ActionService actionService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        List<Company> companies = companyRepository.findAll();
        return ResponseEntity.ok(Map.of("result", "success", "data", companies));
    }

    @GetMapping("/browse")
    public ResponseEntity<Map<String, Object>> browse() {
        return list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return companyRepository.findById(id)
                .map(c -> ResponseEntity.ok(Map.of("result", "success", "data", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Company company) {
        Company created = companyRepository.save(company);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Company company) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (companyRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        company.setId(id);
        companyRepository.save(company);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 与 PHP 一致：该公司下若有用户则禁止删除 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (companyRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        if (!userRepository.findByCompanyAndDeleted(id, 0).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "This company has users. You cannot delete it!"));
        }
        companyRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /**
     * 组织动态 - 按 PHP company/dynamic，取该公司下动态（objectType=company, objectID=companyID 的操作记录）
     */
    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(@RequestParam(required = false) Integer companyID) {
        if (companyID == null || companyID <= 0) {
            return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
        }
        List<Action> actions = actionService.getList("company", companyID);
        return ResponseEntity.ok(Map.of("result", "success", "data", actions));
    }
}
