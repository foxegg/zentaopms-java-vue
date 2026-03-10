package com.zentao.repository;

import com.zentao.entity.WorkEstimation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkEstimationRepository extends JpaRepository<WorkEstimation, Integer> {

    List<WorkEstimation> findByDeletedOrderByIdDesc(int deleted);

    List<WorkEstimation> findByProjectAndDeletedOrderByIdDesc(int project, int deleted);

    Optional<WorkEstimation> findByProjectAndDeleted(int project, int deleted);

    Optional<WorkEstimation> findByIdAndDeleted(int id, int deleted);
}
