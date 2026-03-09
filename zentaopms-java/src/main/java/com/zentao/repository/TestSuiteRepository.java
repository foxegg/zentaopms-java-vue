package com.zentao.repository;

import com.zentao.entity.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSuiteRepository extends JpaRepository<TestSuite, Integer> {

    List<TestSuite> findByProductAndDeletedOrderByOrderNumAsc(int productId, int deleted);

    List<TestSuite> findByProjectAndDeletedOrderByOrderNumAsc(int projectId, int deleted);

    List<TestSuite> findByTypeAndDeletedOrderByOrderNumAsc(String type, int deleted);

    List<TestSuite> findByTypeAndProductAndDeletedOrderByOrderNumAsc(String type, int productId, int deleted);

    List<TestSuite> findByTypeAndProjectAndDeletedOrderByOrderNumAsc(String type, int projectId, int deleted);
}
