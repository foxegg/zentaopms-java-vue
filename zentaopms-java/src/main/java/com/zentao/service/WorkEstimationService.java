package com.zentao.service;

import com.zentao.entity.WorkEstimation;
import com.zentao.repository.WorkEstimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作量估算服务 - 对应 module/workestimation（getBudget 按 project 查询）
 */
@Service
@RequiredArgsConstructor
public class WorkEstimationService {

    private final WorkEstimationRepository workEstimationRepository;

    public List<WorkEstimation> getList() {
        return workEstimationRepository.findByDeletedOrderByIdDesc(0);
    }

    public List<WorkEstimation> getByProject(int projectId) {
        return workEstimationRepository.findByProjectAndDeletedOrderByIdDesc(projectId, 0);
    }

    /** 按项目取唯一预算记录，与 PHP getBudget(projectID) 一致 */
    public Optional<WorkEstimation> getBudget(int projectId) {
        return workEstimationRepository.findByProjectAndDeleted(projectId, 0);
    }

    public Optional<WorkEstimation> getById(int id) {
        return workEstimationRepository.findByIdAndDeleted(id, 0);
    }

    public WorkEstimation create(WorkEstimation entity) {
        entity.setDeleted(0);
        if (entity.getCreatedDate() == null) entity.setCreatedDate(LocalDateTime.now());
        return workEstimationRepository.save(entity);
    }

    public WorkEstimation update(WorkEstimation entity) {
        if (entity.getEditedDate() == null) entity.setEditedDate(LocalDateTime.now());
        return workEstimationRepository.save(entity);
    }

    @Transactional
    public void delete(int id) {
        workEstimationRepository.findById(id).ifPresent(e -> {
            e.setDeleted(1);
            workEstimationRepository.save(e);
        });
    }
}
