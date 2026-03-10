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

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

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

    /** 与 PHP execution getPairs(projectID, type, mode) 一致；projectID≤0 时返回空 */
    public Map<Integer, String> getPairs(int projectId, String mode) {
        if (projectId <= 0) return Map.of();
        List<Project> projects = projectRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("project"), projectId),
                root.get("type").in(EXECUTION_TYPES)
        ));
        return projects.stream().collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : ""));
    }

    /** 按执行 ID 列表返回 id→name，与 PHP execution getPairsByList(executionIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> executionIdList) {
        if (executionIdList == null || executionIdList.isEmpty()) return Map.of();
        List<Project> list = projectRepository.findAllById(executionIdList);
        return list.stream()
                .filter(p -> p.getDeleted() == 0 && EXECUTION_TYPES.contains(p.getType()))
                .collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    public List<Project> getByProject(int projectId) {
        return getByProject(projectId, "all", "all");
    }

    /** 按项目、类型、状态筛选执行列表，与 PHP execution getList(projectID, type, status) 一致；projectID≤0 时返回空列表 */
    public List<Project> getByProject(int projectId, String type, String status) {
        if (projectId <= 0) return List.of();
        Specification<Project> spec = (root, query, cb) -> {
            List<Predicate> preds = new java.util.ArrayList<>();
            preds.add(cb.equal(root.get("deleted"), 0));
            preds.add(cb.equal(root.get("project"), projectId));
            if (type == null || type.isEmpty() || "all".equalsIgnoreCase(type)) {
                preds.add(root.get("type").in(EXECUTION_TYPES));
            } else {
                preds.add(cb.equal(root.get("type"), type));
            }
            if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
                if ("undone".equalsIgnoreCase(status)) {
                    preds.add(cb.not(root.get("status").in("done", "closed")));
                } else if ("delayed".equalsIgnoreCase(status)) {
                    preds.add(cb.greaterThan(root.get("end"), LocalDate.of(1970, 1, 1)));
                    preds.add(cb.lessThan(root.get("end"), LocalDate.now()));
                    preds.add(cb.not(root.get("status").in("done", "closed", "suspended")));
                } else {
                    preds.add(cb.equal(root.get("status"), status));
                }
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(preds.toArray(new Predicate[0]));
        };
        return projectRepository.findAll(spec);
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

    /** 与 PHP 一致：创建执行时设置 openedBy、openedDate（当前用户与时间） */
    public Project create(Project execution) {
        if (execution.getType() == null || !EXECUTION_TYPES.contains(execution.getType())) {
            execution.setType("sprint");
        }
        execution.setDeleted(0);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        execution.setOpenedBy(auth != null && auth.getPrincipal() instanceof UserPrincipal up ? up.getUsername() : "");
        execution.setOpenedDate(java.time.LocalDateTime.now());
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

    /**
     * 延期执行，与 PHP execution putoff(executionID, postData) 一致。
     * 更新 begin/end/days，并记录 lastEditedBy/lastEditedDate。
     */
    @Transactional
    public Project putoff(int executionId, Map<String, Object> body) {
        Project p = getById(executionId).orElseThrow(() -> new RuntimeException("迭代不存在"));
        if (body.containsKey("begin")) {
            Object v = body.get("begin");
            if (v != null && !v.toString().isBlank()) p.setBegin(java.time.LocalDate.parse(v.toString()));
        }
        if (body.containsKey("end")) {
            Object v = body.get("end");
            if (v != null && !v.toString().isBlank()) p.setEnd(java.time.LocalDate.parse(v.toString()));
        }
        if (body.containsKey("days")) {
            Object v = body.get("days");
            if (v instanceof Number) p.setDays(((Number) v).intValue());
            else if (v != null && !v.toString().isBlank()) p.setDays(Integer.parseInt(v.toString()));
        }
        var auth = SecurityContextHolder.getContext().getAuthentication();
        p.setLastEditedBy(auth != null && auth.getPrincipal() instanceof UserPrincipal up ? up.getUsername() : "");
        p.setLastEditedDate(java.time.LocalDateTime.now());
        return projectRepository.save(p);
    }
}
