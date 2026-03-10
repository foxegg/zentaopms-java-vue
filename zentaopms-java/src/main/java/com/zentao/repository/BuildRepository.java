package com.zentao.repository;

import com.zentao.entity.Build;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildRepository extends JpaRepository<Build, Integer> {

    List<Build> findByProjectAndDeleted(int projectId, int deleted);

    List<Build> findByExecutionAndDeleted(int executionId, int deleted);

    List<Build> findByProductAndDeleted(int productId, int deleted);

    List<Build> findByDeletedOrderByIdDesc(int deleted);
}
