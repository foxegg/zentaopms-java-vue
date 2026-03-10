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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /** 按任务 ID 列表返回 id→name，与 PHP task getPairsByIdList(taskIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByIdList(List<Integer> taskIdList) {
        if (taskIdList == null || taskIdList.isEmpty()) return Map.of();
        List<Task> list = taskRepository.findAllById(taskIdList);
        return list.stream()
                .filter(t -> t.getDeleted() == 0)
                .collect(Collectors.toMap(Task::getId, t -> t.getName() != null ? t.getName() : "", (a, b) -> a));
    }

    public Page<Task> getList(Specification<Task> spec, Pageable pageable) {
        return taskRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    /** 与 PHP 一致：创建时设置 openedBy、openedDate（当前用户与时间） */
    public Task create(Task task) {
        task.setDeleted(0);
        task.setStatus("wait");
        task.setOpenedBy(getCurrentAccount());
        task.setOpenedDate(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        actionService.create("task", saved.getId(), "Opened");
        return saved;
    }

    /** 与 PHP 一致：仅更新可编辑字段，并设置 lastEditedBy、lastEditedDate，避免请求体未传字段覆盖 openedBy 等 */
    @Transactional
    public Task update(Task task) {
        Task existing = getById(task.getId()).orElseThrow(() -> new RuntimeException("任务不存在"));
        if (task.getProject() != null) existing.setProject(task.getProject());
        if (task.getExecution() != null) existing.setExecution(task.getExecution());
        if (task.getModule() != null) existing.setModule(task.getModule());
        if (task.getStory() != null) existing.setStory(task.getStory());
        if (task.getName() != null) existing.setName(task.getName());
        if (task.getType() != null) existing.setType(task.getType());
        if (task.getPri() != null) existing.setPri(task.getPri());
        if (task.getEstimate() != null) existing.setEstimate(task.getEstimate());
        if (task.getDeadline() != null) existing.setDeadline(task.getDeadline());
        if (task.getStatus() != null) existing.setStatus(task.getStatus());
        if (task.getAssignedTo() != null) existing.setAssignedTo(task.getAssignedTo());
        if (task.getDescription() != null) existing.setDescription(task.getDescription());
        existing.setLastEditedBy(getCurrentAccount());
        existing.setLastEditedDate(LocalDateTime.now());
        Task saved = taskRepository.save(existing);
        actionService.create("task", existing.getId(), "Edited");
        return saved;
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

    /** 与 PHP task batchClose 一致：跳过已关闭任务；若父子同在列表中则只关闭父任务不重复关闭子任务 */
    @Transactional
    public void batchClose(List<Integer> taskIds) {
        if (taskIds == null) return;
        java.util.Set<Integer> idSet = new java.util.HashSet<>(taskIds);
        for (Integer id : taskIds) {
            Task task = getById(id).orElse(null);
            if (task == null) continue;
            if ("closed".equals(task.getStatus())) continue;
            Integer parentId = task.getParent();
            if (parentId != null && parentId > 0 && idSet.contains(parentId)) continue;
            close(id, null);
        }
    }

    /** 与 PHP task batchChangeModule 一致：跳过模块未变更，并记录 Edited 操作记录 */
    @Transactional
    public void batchChangeModule(List<Integer> taskIds, int moduleId) {
        if (taskIds == null) return;
        for (Integer id : taskIds) {
            Task task = getById(id).orElse(null);
            if (task == null) continue;
            if (moduleId == task.getModule()) continue;
            task.setModule(moduleId);
            taskRepository.save(task);
            actionService.create("task", id, "Edited");
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
