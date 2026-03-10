package com.zentao.repository;

import com.zentao.entity.SuiteCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SuiteCaseRepository extends JpaRepository<SuiteCase, Integer> {

    List<SuiteCase> findBySuite(int suiteId);

    Optional<SuiteCase> findBySuiteAndCaseId(int suite, int caseId);

    @Modifying
    @Query("DELETE FROM SuiteCase s WHERE s.suite = :suite AND s.caseId = :caseId")
    void deleteBySuiteAndCaseId(int suite, int caseId);
}
