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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam int libID) {
        List<Doc> docs = docService.getByLib(libID);
        return ResponseEntity.ok(Map.of("result", "success", "data", docs));
    }

    @GetMapping("/libs/product/{productId}")
    public ResponseEntity<Map<String, Object>> libsByProduct(@PathVariable int productId) {
        List<DocLib> libs = docService.getLibsByProduct(productId);
        return ResponseEntity.ok(Map.of("result", "success", "data", libs));
    }

    @GetMapping("/libs/project/{projectId}")
    public ResponseEntity<Map<String, Object>> libsByProject(@PathVariable int projectId) {
        List<DocLib> libs = docService.getLibsByProject(projectId);
        return ResponseEntity.ok(Map.of("result", "success", "data", libs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
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
        doc.setId(id);
        docService.update(doc);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        docService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
