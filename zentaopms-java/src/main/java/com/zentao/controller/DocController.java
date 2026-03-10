package com.zentao.controller;

import com.zentao.entity.Doc;
import com.zentao.entity.DocLib;
import com.zentao.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文档模块 - 对应 module/doc
 */
@RestController
@RequestMapping("/api/doc")
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> index() {
        List<DocLib> libs = docService.getAllLibs();
        return ResponseEntity.ok(Map.of("result", "success", "data", libs));
    }

    @GetMapping("/mySpace")
    public ResponseEntity<Map<String, Object>> mySpace(
            @RequestParam(defaultValue = "0") int objectID,
            @RequestParam(defaultValue = "0") int libID,
            @RequestParam(defaultValue = "1") int pageID,
            @RequestParam(defaultValue = "20") int recPerPage) {
        List<DocLib> libs = docService.getAllLibs();
        return ResponseEntity.ok(Map.of("result", "success", "data", libs, "pager", Map.of("recTotal", libs.size(), "recPerPage", recPerPage, "pageID", pageID)));
    }

    @GetMapping("/libPairs")
    public ResponseEntity<Map<String, Object>> libPairs(@RequestParam(defaultValue = "0") int product) {
        return ResponseEntity.ok(Map.of("result", "success", "data", docService.getLibPairs(product)));
    }

    /** 与 PHP doc browse 一致：按库与模块筛选；libID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "0") int libID,
            @RequestParam(required = false, defaultValue = "0") int moduleID) {
        if (libID <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<Doc>of()));
        List<Doc> docs = docService.getByLibAndModule(libID, moduleID);
        return ResponseEntity.ok(Map.of("result", "success", "data", docs));
    }

    /** 与 PHP 一致；productId≤0 时返回 data: [] */
    @GetMapping("/libs/product/{productId}")
    public ResponseEntity<Map<String, Object>> libsByProduct(@PathVariable int productId) {
        if (productId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<DocLib>of()));
        List<DocLib> libs = docService.getLibsByProduct(productId);
        return ResponseEntity.ok(Map.of("result", "success", "data", libs));
    }

    @GetMapping("/libs/project/{projectId}")
    public ResponseEntity<Map<String, Object>> libsByProject(@PathVariable int projectId) {
        if (projectId <= 0) return ResponseEntity.ok(Map.of("result", "success", "data", List.<DocLib>of()));
        List<DocLib> libs = docService.getLibsByProject(projectId);
        return ResponseEntity.ok(Map.of("result", "success", "data", libs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return docService.getById(id)
                .map(d -> ResponseEntity.ok(Map.of("result", "success", "data", d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Doc doc) {
        Doc created = docService.create(doc);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Doc doc) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (docService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        doc.setId(id);
        docService.update(doc);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (docService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        docService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 批量移动文档，与 PHP doc batchMoveDoc 一致；lib≤0 时返回 400 */
    @PostMapping("/batchMoveDoc")
    public ResponseEntity<Map<String, Object>> batchMoveDoc(@RequestBody Map<String, Object> body) {
        List<Integer> docIds = body.get("docIds") instanceof List<?> list
                ? list.stream().filter(o -> o instanceof Number).map(o -> ((Number) o).intValue()).toList()
                : List.of();
        int lib = body.get("lib") instanceof Number n ? n.intValue() : 0;
        int module = body.get("module") instanceof Number n ? n.intValue() : 0;
        if (lib <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid lib"));
        docService.batchMoveDoc(docIds, lib, module);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
