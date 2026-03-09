package com.zentao.service;

import com.zentao.entity.Project;
import com.zentao.repository.ProjectRepository;
import com.zentao.security.UserPrincipal;
import com.zentao.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 执行/迭代服务 - execution 使用 zt_project 表，type in (sprint, stage, kanban)
 */
@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final ProjectRepository projectRepository;
    private final CommonService commonService;

    private static final List<String> EXECUTION_TYPES = List.of("sprint", "stage", "kanban");

    public Optional<Project> getById(int id) {
        return projectRepository.findById(id)
                .filter(p -> p.getDeleted() == 0 && EXECUTION_TYPES.contains(p.getType()));
    }

    public Map<Integer, String> getPairs(int projectId, String mode) {
        List<Project> projects = projectRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("project"), projectId),
                root.get("type").in(EXECUTION_TYPES)
        ));
        return projects.stream().collect(Collectors.toMap(Project::getId, Project::getName));
    }

    public List<Project> getByProject(int projectId) {
        return projectRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("project"), projectId),
                root.get("type").in(EXECUTION_TYPES)
        ));
    }

    /** 按参与人（创建人）分页查询执行，对应 PHP user getExecutions(account). 简化实现：按 openedBy 过滤. */
    public Page<Project> getByAccount(String account, String status, Pageable pageable) {
        if (account == null || account.isEmpty()) return Page.empty(pageable);
        return projectRepository.findAll((root, query, cb) -> {
            var base = cb.and(
                    cb.equal(root.get("deleted"), 0),
                    cb.equal(root.get("openedBy"), account),
                    root.get("type").in(EXECUTION_TYPES)
            );
            if (status != null && !status.isEmpty() && !"all".equals(status)) {
                if ("done".equals(status)) {
                    return cb.and(base, root.get("status").in("done", "closed"));
                }
                if ("undone".equals(status)) {
                    return cb.and(base, cb.not(root.get("status").in("done", "closed")));
                }
                return cb.and(base, cb.equal(root.get("status"), status));
            }
            return base;
        }, pageable);
    }

    public Project create(Project execution) {
        if (execution.getType() == null || !EXECUTION_TYPES.contains(execution.getType())) {
            execution.setType("sprint");
        }
        execution.setDeleted(0);
        return projectRepository.save(execution);
    }

    public Project update(Project execution) {
        return projectRepository.save(execution);
    }

    @Transactional
    public Project start(int executionId) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        p.setStatus("doing");
        p.setRealBegan(java.time.LocalDate.now());
        return projectRepository.save(p);
    }

    @Transactional
    public Project close(int executionId) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        p.setStatus("closed");
        var auth = SecurityContextHolder.getContext().getAuthentication();
        p.setClosedBy(auth != null && auth.getPrincipal() instanceof UserPrincipal up ? up.getUsername() : "");
        p.setClosedDate(java.time.LocalDateTime.now());
        p.setRealEnd(java.time.LocalDate.now());
        return projectRepository.save(p);
    }

    @Transactional
    public void delete(int executionId) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        p.setDeleted(1);
        projectRepository.save(p);
    }

    @Transactional
    public Project suspend(int executionId) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        p.setStatus("suspended");
        return projectRepository.save(p);
    }

    @Transactional
    public Project activate(int executionId) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        p.setStatus("doing");
        p.setClosedBy(null);
        p.setClosedDate(null);
        return projectRepository.save(p);
    }

    @Transactional
    public void batchEdit(List<Integer> executionIds, Map<String, Object> fields) {
        if (executionIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : executionIds) {
            Project p = getById(id).orElse(null);
            if (p == null) continue;
            if (fields.containsKey("name")) p.setName(fields.get("name") != null ? fields.get("name").toString() : p.getName());
            if (fields.containsKey("begin")) p.setBegin(fields.get("begin") != null ? java.time.LocalDate.parse(fields.get("begin").toString()) : p.getBegin());
            if (fields.containsKey("end")) p.setEnd(fields.get("end") != null ? java.time.LocalDate.parse(fields.get("end").toString()) : p.getEnd());
            projectRepository.save(p);
        }
    }
}
