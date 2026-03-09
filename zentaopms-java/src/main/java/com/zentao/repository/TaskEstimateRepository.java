package com.zentao.repository;

import com.zentao.entity.TaskEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskEstimateRepository extends JpaRepository<TaskEstimate, Integer> {

    List<TaskEstimate> findByTaskOrderByOrderNumAsc(int taskId);
}
