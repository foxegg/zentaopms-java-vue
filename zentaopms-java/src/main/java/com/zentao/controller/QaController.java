package com.zentao.controller;

import com.zentao.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 与 PHP qa 一致：index 返回产品列表与仪表盘数据（测试入口） */
@RestController
@RequestMapping("/api/qa")
@RequiredArgsConstructor
public class QaController {

    private final ProductService productService;

    @GetMapping({ "/index", "/list", "" })
    public ResponseEntity<Map<String, Object>> index(
            @RequestParam(required = false) Integer productID,
            @RequestParam(required = false) Integer projectID) {
        Map<Integer, String> products = productService.getPairs("noclosed", 0);
        if (productID != null && productID > 0 && !products.containsKey(productID)) {
            productID = products.isEmpty() ? null : products.keySet().iterator().next();
        }
        Map<String, Object> data = new java.util.HashMap<>(Map.of(
                "products", products,
                "dashboard", Map.of()
        ));
        if (productID != null) data.put("productID", productID);
        if (projectID != null) data.put("projectID", projectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable Object id) { return PlaceholderResponses.emptyView(id); }
    @PostMapping({ "/create", "" })
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable Object id, @RequestBody Map<String, Object> body) { return PlaceholderResponses.success(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Object id) { return PlaceholderResponses.success(); }
}
