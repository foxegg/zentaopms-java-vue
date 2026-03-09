package com.zentao.controller;

import com.zentao.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * API 入口 - 兼容 ZenTao PHP 的 api 调用，支持 entry key 认证
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiProxyController {

    private final EntryRepository entryRepository;

    @GetMapping
    public ResponseEntity<?> api(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String m,
            @RequestParam(required = false) String f) {

        if (key != null && !key.isEmpty()) {
            var entry = entryRepository.findByKeyStrAndDeleted(key, 0);
            if (entry.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("result", "fail", "message", "Invalid API key"));
            }
        }

        return ResponseEntity.ok(Map.of(
                "result", "success",
                "message", "ZenTao API - use REST: /api/product, /api/project, /api/task, etc."
        ));
    }
}
