package com.zentao.controller;

import com.zentao.entity.Acl;
import com.zentao.service.AclService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 与 PHP personnel 一致：白名单基于 zt_acl，getWhitelist/updateWhitelist/deleteWhitelist */
@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class PersonnelController {

    private final AclService aclService;

    /** 与 PHP getWhitelist 一致：按 objectType、objectID 返回白名单列表；objectID≤0 或 objectType 空时返回 data: [] */
    @GetMapping({ "/whitelist", "/list" })
    public ResponseEntity<Map<String, Object>> whitelist(
            @RequestParam(required = false) String objectType,
            @RequestParam(required = false, defaultValue = "0") int objectID) {
        if (objectID <= 0 || objectType == null || objectType.isBlank())
            return ResponseEntity.ok(Map.of("result", "success", "data", List.<Acl>of()));
        List<Acl> list = aclService.getWhitelist(objectType, objectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    /** 与 PHP getWhitelistAccount 一致：仅返回账号列表；objectID≤0 或 objectType 空时返回 data: [] */
    @GetMapping("/whitelist/accounts")
    public ResponseEntity<Map<String, Object>> whitelistAccounts(
            @RequestParam(required = false) String objectType,
            @RequestParam(required = false, defaultValue = "0") int objectID) {
        if (objectID <= 0 || objectType == null || objectType.isBlank())
            return ResponseEntity.ok(Map.of("result", "success", "data", List.<String>of()));
        List<String> accounts = aclService.getWhitelistAccounts(objectType, objectID);
        return ResponseEntity.ok(Map.of("result", "success", "data", accounts));
    }

    /** 与 PHP updateWhitelist 一致：更新白名单，body: objectType, objectID, accounts[], updateType=replace|increase */
    @PostMapping("/whitelist")
    public ResponseEntity<Map<String, Object>> updateWhitelist(@RequestBody Map<String, Object> body) {
        String objectType = (String) body.get("objectType");
        Number oid = (Number) body.get("objectID");
        List<String> accounts = List.of();
        if (body.get("accounts") instanceof List<?> list) {
            accounts = list.stream().map(o -> o != null ? o.toString() : null).filter(s -> s != null && !s.isBlank()).toList();
        }
        String updateType = body.get("updateType") != null ? body.get("updateType").toString() : "replace";
        if (objectType == null || oid == null) {
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "objectType and objectID required"));
        }
        int objectID = oid.intValue();
        if (objectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid objectID"));
        aclService.updateWhitelist(objectType, objectID, accounts, "replace".equalsIgnoreCase(updateType));
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    /** 从白名单移除指定账号 */
    @DeleteMapping("/whitelist")
    public ResponseEntity<Map<String, Object>> deleteFromWhitelist(
            @RequestParam String objectType,
            @RequestParam int objectID,
            @RequestParam String account) {
        if (objectID <= 0) return ResponseEntity.badRequest().body(Map.of("result", "fail", "message", "invalid objectID"));
        aclService.deleteFromWhitelist(objectType, objectID, account);
        return ResponseEntity.ok(Map.of("result", "success"));
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
