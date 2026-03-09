package com.zentao.service;

import com.zentao.entity.Task;
import com.zentao.repository.TaskRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ActionService actionService;

    public Optional<Task> getById(int id) {
        return taskRepository.findById(id).filter(t -> t.getDeleted() == 0);
    }

    public List<Task> getByProject(int projectId) {
        return taskRepository.findByProjectAndDeleted(projectId, 0);
    }

    public List<Task> getByExecution(int executionId) {
        return taskRepository.findByExecutionAndDeleted(executionId, 0);
    }

    public List<Task> getByAssignedTo(String account) {
        return taskRepository.findByAssignedToAndDeleted(account, 0);
    }

    public Page<Task> getList(Specification<Task> spec, Pageable pageable) {
        return taskRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    public Task create(Task task) {
        task.setDeleted(0);
        task.setStatus("wait");
        Task saved = taskRepository.save(task);
        actionService.create("task", saved.getId(), "Opened");
        return saved;
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTo(int taskId, String assignedTo) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setAssignedTo(assignedTo);
        task.setAssignedDate(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Assigned", "", assignedTo);
        return saved;
    }

    @Transactional
    public Task start(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("doing");
        task.setRealStarted(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Started");
        return saved;
    }

    @Transactional
    public Task finish(int taskId, java.math.BigDecimal consumed, java.math.BigDecimal left) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("done");
        task.setFinishedBy(getCurrentAccount());
        task.setFinishedDate(LocalDateTime.now());
        if (consumed != null) task.setConsumed(consumed);
        if (left != null) task.setLeft_(left);
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Finished");
        return saved;
    }

    @Transactional
    public Task pause(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("pause");
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Paused");
        return saved;
    }

    @Transactional
    public Task restart(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("doing");
        task.setActivatedDate(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Restarted");
        return saved;
    }

    @Transactional
    public Task close(int taskId, String closedReason) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("closed");
        task.setClosedBy(getCurrentAccount());
        task.setClosedDate(LocalDateTime.now());
        if (closedReason != null) task.setClosedReason(closedReason);
        task.setAssignedTo("closed");
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Closed");
        return saved;
    }

    @Transactional
    public Task cancel(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("cancel");
        task.setCanceledBy(getCurrentAccount());
        task.setCanceledDate(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Canceled");
        return saved;
    }

    @Transactional
    public Task activate(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("doing");
        task.setActivatedDate(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "Activated");
        return saved;
    }

    @Transactional
    public void delete(int taskId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setDeleted(1);
        taskRepository.save(task);
        actionService.create("task", taskId, "deleted");
    }

    @Transactional
    public void batchAssignTo(List<Integer> taskIds, String assignedTo) {
        for (Integer id : taskIds) {
            assignTo(id, assignedTo);
        }
    }

    @Transactional
    public void batchClose(List<Integer> taskIds) {
        for (Integer id : taskIds) {
            close(id, null);
        }
    }

    @Transactional
    public void batchChangeModule(List<Integer> taskIds, int moduleId) {
        if (taskIds == null) return;
        for (Integer id : taskIds) {
            Task task = getById(id).orElse(null);
            if (task != null) {
                task.setModule(moduleId);
                taskRepository.save(task);
            }
        }
    }

    @Transactional
    public void batchCancel(List<Integer> taskIds) {
        if (taskIds == null) return;
        for (Integer id : taskIds) {
            Task task = getById(id).orElse(null);
            if (task != null && "wait".equals(task.getStatus())) {
                task.setStatus("cancel");
                task.setCanceledBy(getCurrentAccount());
                task.setCanceledDate(LocalDateTime.now());
                taskRepository.save(task);
                actionService.create("task", id, "Canceled");
            }
        }
    }

    @Transactional
    public void batchEdit(List<Integer> taskIds, java.util.Map<String, Object> fields) {
        if (taskIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : taskIds) {
            Task task = getById(id).orElse(null);
            if (task == null) continue;
            if (fields.containsKey("assignedTo")) task.setAssignedTo(fields.get("assignedTo") != null ? fields.get("assignedTo").toString() : task.getAssignedTo());
            if (fields.containsKey("module")) task.setModule(fields.get("module") instanceof Number n ? n.intValue() : task.getModule());
            if (fields.containsKey("estimate")) task.setEstimate(fields.get("estimate") != null ? new java.math.BigDecimal(fields.get("estimate").toString()) : task.getEstimate());
            if (fields.containsKey("pri")) task.setPri(fields.get("pri") instanceof Number n ? n.intValue() : task.getPri());
            if (fields.containsKey("deadline")) task.setDeadline(fields.get("deadline") != null ? java.time.LocalDate.parse(fields.get("deadline").toString()) : task.getDeadline());
            taskRepository.save(task);
            actionService.create("task", id, "Edited");
        }
    }

    @Transactional
    public List<Integer> batchCreate(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return List.of();
        List<Integer> ids = new java.util.ArrayList<>();
        for (Task t : tasks) {
            Task created = create(t);
            ids.add(created.getId());
        }
        return ids;
    }

    @Transactional
    public Task confirmStoryChange(int taskId, int storyId) {
        Task task = getById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStory(storyId);
        task.setStoryVersion(task.getStoryVersion() != null ? task.getStoryVersion() + 1 : 1);
        Task saved = taskRepository.save(task);
        actionService.create("task", taskId, "confirmStoryChange", "", String.valueOf(storyId));
        return saved;
    }

    /** 任务报表摘要（按项目/执行统计） */
    public java.util.Map<String, Object> getReport(Integer projectId, Integer executionId) {
        List<Task> list = executionId != null && executionId > 0
                ? getByExecution(executionId)
                : projectId != null && projectId > 0 ? getByProject(projectId) : List.of();
        long wait = list.stream().filter(t -> "wait".equals(t.getStatus())).count();
        long doing = list.stream().filter(t -> "doing".equals(t.getStatus())).count();
        long done = list.stream().filter(t -> "done".equals(t.getStatus())).count();
        long closed = list.stream().filter(t -> "closed".equals(t.getStatus())).count();
        return java.util.Map.of(
                "total", list.size(),
                "wait", wait, "doing", doing, "done", done, "closed", closed
        );
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
