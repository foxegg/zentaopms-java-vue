package com.zentao.controller;

import com.zentao.entity.Company;
import com.zentao.repository.CompanyRepository;
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
        company.setId(id);
        companyRepository.save(company);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        companyRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(@RequestParam(required = false) Integer companyID) {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }
}
