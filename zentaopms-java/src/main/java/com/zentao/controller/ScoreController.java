package com.zentao.controller;

import com.zentao.entity.Score;
import com.zentao.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService service;

    @GetMapping({ "/list", "", "/" })
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String account,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        var page = service.getList(account, pageID, recPerPage);
        return ResponseEntity.ok(Map.of("result", "success", "data", page.getContent(), "pager",
                Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable long id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return service.getById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }
}
