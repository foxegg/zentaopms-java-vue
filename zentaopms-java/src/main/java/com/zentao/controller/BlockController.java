package com.zentao.controller;

import com.zentao.entity.Block;
import com.zentao.service.BlockService;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 区块/仪表盘 - 对应 module/block
 */
@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String dashboard) {
        String account = getCurrentAccount();
        List<Block> list = (dashboard != null && !dashboard.isEmpty())
                ? blockService.getByAccountAndDashboard(account, dashboard)
                : blockService.getByAccount(account);
        return ResponseEntity.ok(Map.of("result", "success", "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable int id) {
        return blockService.getById(id)
                .map(b -> ResponseEntity.ok(Map.of("result", "success", "data", b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Block block) {
        if (block.getAccount() == null || block.getAccount().isEmpty()) {
            block.setAccount(getCurrentAccount());
        }
        Block created = blockService.create(block);
        return ResponseEntity.ok(Map.of("result", "success", "id", created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> edit(@PathVariable int id, @RequestBody Block block) {
        block.setId(id);
        blockService.update(block);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        blockService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
