package com.zentao.service;

import com.zentao.entity.Project;
import com.zentao.repository.ProjectRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Optional<Project> getById(int id) {
        return projectRepository.findById(id).filter(p -> p.getDeleted() == 0);
    }

    public Page<Project> getList(Specification<Project> spec, Pageable pageable) {
        return projectRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    /** 构建项目列表筛选条件，与 PHP project getList(status, orderBy) 一致；仅项目/项目集（排除执行 type），status: all/undone/wait/doing/done/closed */
    public Specification<Project> buildListSpec(String status, String type) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (type != null && !type.isEmpty() && !"all".equalsIgnoreCase(type)) {
                preds.add(cb.equal(root.get("type"), type));
            } else {
                preds.add(root.get("type").in("project", "program"));
            }
            if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
                if ("undone".equalsIgnoreCase(status)) {
                    preds.add(cb.not(root.get("status").in("done", "closed")));
                } else if ("done".equalsIgnoreCase(status)) {
                    preds.add(root.get("status").in("done", "closed"));
                } else {
                    preds.add(cb.equal(root.get("status"), status));
                }
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

    public List<Project> getAll() {
        return projectRepository.findByDeletedOrderByIdDesc(0);
    }

    /** 项目/项目集 id->name 下拉用，与 PHP project getPairsByProgram 一致；mode=all 全部，noclosed 排除 closed；programID>0 仅该项目集下 */
    public Map<Integer, String> getPairs(String mode, Integer programID) {
        Specification<Project> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.equal(root.get("deleted"), 0));
            preds.add(root.get("type").in("project", "program"));
            if (mode != null && "noclosed".equalsIgnoreCase(mode)) {
                preds.add(cb.not(root.get("status").in("done", "closed")));
            }
            if (programID != null && programID > 0) {
                preds.add(cb.equal(root.get("project"), programID));
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
        List<Project> list = projectRepository.findAll(spec);
        return list.stream().collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    /** 按项目 ID 列表返回 id→name（仅 type=project/program），与 PHP project getPairsByIdList(projectIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> projectIdList) {
        if (projectIdList == null || projectIdList.isEmpty()) return Map.of();
        List<Project> list = projectRepository.findAllById(projectIdList);
        return list.stream()
                .filter(p -> p.getDeleted() == 0 && (p.getType() != null && ("project".equals(p.getType()) || "program".equals(p.getType()))))
                .collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    /** 项目集 id→name 下拉用，与 PHP program getPairs 一致；仅 type=program、deleted=0 */
    public Map<Integer, String> getProgramPairs() {
        Specification<Project> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("type"), "program"));
        List<Project> list = projectRepository.findAll(spec);
        return list.stream().collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    /** 按项目集 ID 列表返回 id→name，与 PHP program getPairsByList(programIDList) 一致；空列表返回空 Map */
    public Map<Integer, String> getProgramPairsByList(List<Integer> programIdList) {
        if (programIdList == null || programIdList.isEmpty()) return Map.of();
        List<Project> list = projectRepository.findAllById(programIdList);
        return list.stream()
                .filter(p -> p.getDeleted() == 0 && "program".equals(p.getType()))
                .collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    /** 与 PHP 一致：创建项目时设置 openedBy、openedDate（当前用户与时间） */
    public Project create(Project project) {
        project.setDeleted(0);
        project.setOpenedBy(getCurrentAccount());
        project.setOpenedDate(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public Project start(int projectId) {
        Project p = getById(projectId).orElseThrow(() -> new RuntimeException("项目不存在"));
        p.setStatus("doing");
        p.setRealBegan(java.time.LocalDate.now());
        return projectRepository.save(p);
    }

    @Transactional
    public Project suspend(int projectId) {
        Project p = getById(projectId).orElseThrow(() -> new RuntimeException("项目不存在"));
        p.setStatus("suspended");
        return projectRepository.save(p);
    }

    @Transactional
    public Project close(int projectId) {
        Project p = getById(projectId).orElseThrow(() -> new RuntimeException("项目不存在"));
        p.setStatus("closed");
        p.setClosedBy(getCurrentAccount());
        p.setClosedDate(LocalDateTime.now());
        p.setRealEnd(java.time.LocalDate.now());
        return projectRepository.save(p);
    }

    @Transactional
    public Project activate(int projectId) {
        Project p = getById(projectId).orElseThrow(() -> new RuntimeException("项目不存在"));
        p.setStatus("doing");
        p.setClosedBy(null);
        p.setClosedDate(null);
        return projectRepository.save(p);
    }

    @Transactional
    public void delete(int projectId) {
        Project p = getById(projectId).orElseThrow(() -> new RuntimeException("项目不存在"));
        p.setDeleted(1);
        projectRepository.save(p);
    }

    @Transactional
    public void batchEdit(List<Integer> projectIds, java.util.Map<String, Object> fields) {
        if (projectIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : projectIds) {
            Project p = getById(id).orElse(null);
            if (p == null) continue;
            if (fields.containsKey("name")) p.setName(fields.get("name") != null ? fields.get("name").toString() : p.getName());
            if (fields.containsKey("code")) p.setCode(fields.get("code") != null ? fields.get("code").toString() : p.getCode());
            if (fields.containsKey("model")) p.setModel(fields.get("model") != null ? fields.get("model").toString() : p.getModel());
            projectRepository.save(p);
        }
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
