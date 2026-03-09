package com.zentao.repository;

import com.zentao.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Integer> {

    List<Stage> findByWorkflowGroupAndDeletedOrderByOrderNumAsc(int workflowGroup, int deleted);

    List<Stage> findByDeletedOrderByOrderNumAsc(int deleted);
}
