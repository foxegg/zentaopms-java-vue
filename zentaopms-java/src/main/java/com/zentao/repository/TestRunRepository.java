package com.zentao.repository;

import com.zentao.entity.TestRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestRunRepository extends JpaRepository<TestRun, Integer> {

    List<TestRun> findByTaskIdOrderByIdAsc(Integer taskId);

    Optional<TestRun> findByTaskIdAndCaseId(Integer taskId, Integer caseId);

    void deleteByTaskIdAndCaseId(Integer taskId, Integer caseId);

    Page<TestRun> findByAssignedToOrderByIdDesc(String assignedTo, Pageable pageable);
}
