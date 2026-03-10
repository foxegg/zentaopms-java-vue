package com.zentao.controller;

import com.zentao.entity.Product;
import com.zentao.service.ActionService;
import com.zentao.service.ExportService;
import com.zentao.service.ProductPlanService;
import com.zentao.service.ProductService;
import com.zentao.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品模块 - 对应 module/product
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ExportService exportService;
    private final ActionService actionService;
    private final ProductPlanService productPlanService;
    private final ReleaseService releaseService;

    /** 与 PHP product list 一致：product 为产品 ID 筛选；status、branch 筛选；pageID、recPerPage 分页；分页时返回 pager */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "all") String mode,
            @RequestParam(required = false, defaultValue = "0") int product,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false, defaultValue = "1") int pageID,
            @RequestParam(required = false, defaultValue = "0") int recPerPage) {
        if (product > 0) {
            var one = productService.getById(product);
            if (one.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "result", "success",
                        "data", List.<Product>of(),
                        "pager", Map.of("recTotal", 0, "recPerPage", recPerPage > 0 ? recPerPage : 20, "pageID", pageID)));
            }
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", List.of(one.get()),
                    "pager", Map.of("recTotal", 1, "recPerPage", recPerPage > 0 ? recPerPage : 20, "pageID", pageID)));
        }
        if (recPerPage > 0) {
            var page = productService.getListPage("all".equals(mode) ? null : mode, status, pageID, recPerPage);
            return ResponseEntity.ok(Map.of(
                    "result", "success",
                    "data", page.getContent(),
                    "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)));
        }
        List<Product> products = productService.getList("all".equals(mode) ? null : mode, status, pageID, recPerPage);
        return ResponseEntity.ok(Map.of("result", "success", "data", products));
    }

    @GetMapping("/pairs")
    public ResponseEntity<Map<String, Object>> pairs(
            @RequestParam(defaultValue = "all") String mode,
            @RequestParam(defaultValue = "0") int programID) {
        Map<Integer, String> pairs = productService.getPairs(mode, programID);
        return ResponseEntity.ok(Map.of("result", "success", "data", pairs));
    }

    @GetMapping("/pairsByList")
    public ResponseEntity<Map<String, Object>> pairsByList(@RequestParam(required = false) String ids) {
        List<Integer> idList = ids != null && !ids.isBlank()
                ? java.util.Arrays.stream(ids.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(s -> { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; } })
                        .filter(i -> i != null && i > 0)
                        .distinct()
                        .toList()
                : List.of();
        return ResponseEntity.ok(Map.of("result", "success", "data", productService.getPairsByList(idList)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return productService.getById(id)
                .map(p -> ResponseEntity.ok(Map.of("result", "success", "data", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/dynamic")
    public ResponseEntity<Map<String, Object>> dynamic(@PathVariable int id,
            @RequestParam(defaultValue = "0") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        if (id <= 0) return ResponseEntity.notFound().build();
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        var page = actionService.getByProduct(id, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        return ResponseEntity.ok(Map.of(
                "result", "success",
                "data", page.getContent(),
                "pager", Map.of("recTotal", page.getTotalElements(), "recPerPage", recPerPage, "pageID", pageID)
        ));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Product product) {
        Product created = productService.create(product);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Product product) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        product.setId(id);
        productService.update(product);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activate(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Product product = productService.activate(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", product));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Map<String, Object>> close(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Product product = productService.close(id);
        return ResponseEntity.ok(Map.of("result", "success", "data", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        productService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PostMapping("/batchEdit")
    public ResponseEntity<Map<String, Object>> batchEdit(@RequestBody Map<String, Object> body) {
        List<Integer> productIds = toIntList(body.get("productIds"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        productService.batchEdit(productIds, fields != null ? fields : Map.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<Map<String, Object>> updateOrder(@RequestBody Map<String, Object> body) {
        List<Integer> productIDs = toIntList(body.get("productIDs"));
        productService.updateOrder(productIDs != null ? productIDs : List.of());
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam(defaultValue = "5000") int maxRows) throws java.io.IOException {
        byte[] data = exportService.exportProducts(maxRows);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).body(data);
    }

    /** 与 PHP product/roadmap 一致：返回产品的计划与发布列表供路线图展示 */
    @GetMapping("/{id}/roadmap")
    public ResponseEntity<Map<String, Object>> roadmap(@PathVariable int id,
            @RequestParam(required = false, defaultValue = "all") String branch) {
        if (id <= 0) return ResponseEntity.notFound().build();
        if (productService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Map<String, Object> data = Map.of(
                "plans", productPlanService.getByProduct(id),
                "releases", releaseService.getByProduct(id)
        );
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    private static List<Integer> toIntList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList();
        }
        return List.of();
    }
}
