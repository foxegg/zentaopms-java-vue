package com.zentao.controller;

import com.zentao.repository.BugRepository;
import com.zentao.repository.TaskRepository;
import com.zentao.repository.TodoRepository;
import com.zentao.repository.TestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 报表模块 - 对应 module/report
 */
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final BugRepository bugRepository;
    private final TaskRepository taskRepository;
    private final TodoRepository todoRepository;
    private final TestTaskRepository testTaskRepository;

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of("result", "success", "redirect", "/api/report/annualData"));
    }

    @GetMapping("/annualData")
    public ResponseEntity<Map<String, Object>> annualData(
            @RequestParam(required = false) Integer year) {
        int y = year != null ? year : java.time.Year.now().getValue();
        long bugCount = bugRepository.countByDeleted(0);
        long taskCount = taskRepository.countByDeleted(0);
        long todoCount = todoRepository.countByDeleted(0);
        long testTaskCount = testTaskRepository.countByDeleted(0);

        Map<String, Object> data = new HashMap<>();
        data.put("year", y);
        data.put("bugs", bugCount);
        data.put("tasks", taskCount);
        data.put("todos", todoCount);
        data.put("testTasks", testTaskCount);
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }

    /**
     * 每日提醒（日报邮件） - 占位实现，返回待发送的提醒摘要
     * 对应 PHP report.remind
     */
    @GetMapping("/remind")
    public ResponseEntity<Map<String, Object>> remind() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "reminder placeholder; integrate with mail queue when needed");
        return ResponseEntity.ok(Map.of("result", "success", "data", data));
    }
}
