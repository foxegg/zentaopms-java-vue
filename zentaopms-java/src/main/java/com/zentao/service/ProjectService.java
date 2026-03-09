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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public List<Project> getAll() {
        return projectRepository.findByDeletedOrderByIdDesc(0);
    }

    public Project create(Project project) {
        project.setDeleted(0);
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
