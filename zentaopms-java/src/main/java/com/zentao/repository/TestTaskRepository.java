package com.zentao.repository;

import com.zentao.entity.TestTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestTaskRepository extends JpaRepository<TestTask, Integer> {

    List<TestTask> findByProductAndDeletedOrderByIdDesc(int productId, int deleted);

    List<TestTask> findByProjectAndDeletedOrderByIdDesc(int projectId, int deleted);

    List<TestTask> findByExecutionAndDeletedOrderByIdDesc(int executionId, int deleted);

    Page<TestTask> findByOwnerAndDeletedOrderByIdDesc(String owner, int deleted, Pageable pageable);

    long countByDeleted(int deleted);

    List<TestTask> findByDeletedOrderByIdDesc(int deleted);
}
