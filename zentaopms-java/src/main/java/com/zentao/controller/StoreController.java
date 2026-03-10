package com.zentao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP store 一致：应用市场 browse/appView 接口形状（应用列表与分类可接外部或返回空） */
@RestController
@RequestMapping("/api/store")
public class StoreController {

    @GetMapping({ "/browse", "/list", "" })
    public ResponseEntity<Map<String, Object>> browse(
            @RequestParam(required = false, defaultValue = "create_time") String sortType,
            @RequestParam(required = false, defaultValue = "0") int categoryID,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") int pageID,
            @RequestParam(required = false, defaultValue = "12") int recPerPage) {
        Map<String, Object> data = new java.util.HashMap<>(Map.of(
                "cloudApps", java.util.List.of(),
                "installedApps", java.util.List.of(),
                "categories", Map.of(),
                "sortType", sortType,
                "currentCategoryID", categoryID,
                "keyword", keyword != null ? keyword : "",
                "pager", Map.of("total", 0, "pageID", pageID, "recPerPage", recPerPage)
        ));
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/appView")
    public ResponseEntity<Map<String, Object>> appView(
            @RequestParam(required = false, defaultValue = "0") int appID,
            @RequestParam(required = false, defaultValue = "1") int pageID,
            @RequestParam(required = false, defaultValue = "20") int recPerPage) {
        Map<String, Object> data = Map.of(
                "app", appID > 0 ? Map.of("id", appID) : Map.of(),
                "pager", Map.of("total", 0, "pageID", pageID, "recPerPage", recPerPage)
        );
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) {
        return PlaceholderResponses.emptyView(id);
    }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
