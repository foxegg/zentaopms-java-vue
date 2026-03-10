package com.zentao.repository;

import com.zentao.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    List<Task> findByProjectAndDeleted(int projectId, int deleted);

    List<Task> findByExecutionAndDeleted(int executionId, int deleted);

    List<Task> findByAssignedToAndDeleted(String account, int deleted);

    List<Task> findByDeleted(int deleted);

    List<Task> findByNameContainingAndDeleted(String name, int deleted, org.springframework.data.domain.Pageable pageable);

    long countByDeleted(int deleted);

    /** 模块删除时将被删模块下的任务归到父模块，与 PHP tree remove 一致 */
    List<Task> findByModuleInAndDeleted(java.util.Collection<Integer> moduleIds, int deleted);
}
