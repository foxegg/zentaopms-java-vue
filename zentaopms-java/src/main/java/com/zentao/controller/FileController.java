package com.zentao.controller;

import com.zentao.entity.File;
import com.zentao.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件模块 - 对应 module/file
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /** 与 PHP file getList 一致：按 objectType+objectID 查附件；objectID≤0 时返回空列表 */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String objectType,
            @RequestParam(required = false, defaultValue = "0") int objectID) {
        if (objectID <= 0 || objectType == null || objectType.isBlank()) {
            return ResponseEntity.ok(Map.of("result", "success", "data", List.<File>of()));
        }
        List<File> files = fileService.getByObject(objectType, objectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", files));
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.notFound().build();
        return fileService.getById(id)
                .map(f -> ResponseEntity.ok(Map.of("result", "success", "data", f)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable int id) throws java.io.IOException {
        if (id <= 0) return ResponseEntity.notFound().build();
        var fileOpt = fileService.getById(id);
        if (fileOpt.isEmpty()) return ResponseEntity.notFound().build();
        File f = fileOpt.get();
        var pathOpt = fileService.getRealPath(f);
        if (pathOpt.isEmpty()) return ResponseEntity.notFound().build();
        Resource r = new UrlResource(pathOpt.get().toUri());
        String filename = f.getTitle() != null ? f.getTitle() : "file";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(r);
    }

    /** 与 PHP 一致；id≤0 时返回 404 */
    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(@PathVariable int id) throws java.io.IOException {
        if (id <= 0) return ResponseEntity.notFound().build();
        var fileOpt = fileService.getById(id);
        if (fileOpt.isEmpty()) return ResponseEntity.notFound().build();
        File f = fileOpt.get();
        var pathOpt = fileService.getRealPath(f);
        if (pathOpt.isEmpty()) return ResponseEntity.notFound().build();
        Resource r = new UrlResource(pathOpt.get().toUri());
        String filename = f.getTitle() != null ? f.getTitle() : "file";
        MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
        String ext = filename.contains(".") ? filename.substring(filename.lastIndexOf('.') + 1).toLowerCase() : "";
        if ("jpg".equals(ext) || "jpeg".equals(ext)) mt = MediaType.IMAGE_JPEG;
        else if ("png".equals(ext)) mt = MediaType.IMAGE_PNG;
        else if ("gif".equals(ext)) mt = MediaType.IMAGE_GIF;
        else if ("webp".equals(ext)) mt = MediaType.parseMediaType("image/webp");
        else if ("pdf".equals(ext)) mt = MediaType.APPLICATION_PDF;
        else if ("txt".equals(ext)) mt = MediaType.TEXT_PLAIN;
        return ResponseEntity.ok()
                .contentType(mt)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(r);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "") String objectType,
            @RequestParam(required = false, defaultValue = "0") int objectID,
            @RequestParam(required = false, defaultValue = "") String extra) {
        try {
            File created = fileService.uploadFile(file, objectType, objectID, extra);
            return ResponseEntity.ok(Map.of("result", "success", "id", created.getId(), "data", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody File file) {
        File created = fileService.create(file);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        if (id <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid id"));
        if (fileService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        fileService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
