package com.zentao.repository;

import com.zentao.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Integer>, JpaSpecificationExecutor<Bug> {

    List<Bug> findByProductAndDeleted(int productId, int deleted);

    List<Bug> findByProjectAndDeleted(int projectId, int deleted);

    List<Bug> findByPlanAndDeleted(int planId, int deleted);

    List<Bug> findByAssignedToAndDeleted(String account, int deleted);

    List<Bug> findByTitleContainingAndDeleted(String title, int deleted, org.springframework.data.domain.Pageable pageable);

    long countByDeleted(int deleted);

    /** 模块删除时将被删模块下的缺陷归到父模块，与 PHP tree remove 一致 */
    List<Bug> findByModuleInAndDeleted(java.util.Collection<Integer> moduleIds, int deleted);
}
