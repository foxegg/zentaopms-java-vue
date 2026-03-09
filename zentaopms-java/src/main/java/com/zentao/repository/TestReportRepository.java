package com.zentao.repository;

import com.zentao.entity.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestReportRepository extends JpaRepository<TestReport, Integer> {

    List<TestReport> findByProjectAndDeletedOrderByIdDesc(int projectId, int deleted);

    List<TestReport> findByProductAndDeletedOrderByIdDesc(int productId, int deleted);

    List<TestReport> findByExecutionAndDeletedOrderByIdDesc(int executionId, int deleted);
}
