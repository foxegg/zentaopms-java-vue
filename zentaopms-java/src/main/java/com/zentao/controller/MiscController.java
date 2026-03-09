package com.zentao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 杂项模块 - 对应 module/misc
 */
@RestController
@RequestMapping("/api/misc")
@RequiredArgsConstructor
public class MiscController {

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(Map.of("result", "success", "data", "pong"));
    }

    @GetMapping("/time")
    public ResponseEntity<Map<String, Object>> time() {
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", Map.of(
                        "serverTime", System.currentTimeMillis(),
                        "timezone", java.time.ZoneId.systemDefault().getId()
                )
        ));
    }
}
