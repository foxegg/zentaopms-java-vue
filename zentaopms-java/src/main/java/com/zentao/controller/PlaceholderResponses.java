package com.zentao.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/** 占位模块统一响应：空列表、空详情、成功操作，供扩展/集成模块占位 Controller 使用 */
public final class PlaceholderResponses {
    public static ResponseEntity<Map<String, Object>> emptyList() {
        return ResponseEntity.ok(Map.of("result", "success", "data", List.of()));
    }
    public static ResponseEntity<Map<String, Object>> emptyView(Object id) {
        return ResponseEntity.ok(Map.of("result", "success", "data", Map.of("id", id)));
    }
    /** create/edit/delete 等写操作占位：返回 success，与 PHP 接口形状一致 */
    public static ResponseEntity<Map<String, Object>> success() {
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
