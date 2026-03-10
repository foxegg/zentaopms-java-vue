package com.zentao.repository;

import com.zentao.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    List<TestCase> findByProductAndDeleted(int productId, int deleted);

    List<TestCase> findByLibAndDeleted(int libId, int deleted);

    /** 有待审核用例的用例库 ID 列表（lib>0 且 reviewedBy 为空，与 PHP caselib type=review 一致） */
    @Query("SELECT DISTINCT c.lib FROM TestCase c WHERE c.deleted = 0 AND c.lib > 0 AND (c.reviewedBy IS NULL OR c.reviewedBy = '')")
    List<Integer> findDistinctLibWithUnreviewedCases();

    Page<TestCase> findByOpenedByAndDeletedOrderByIdDesc(String openedBy, int deleted, Pageable pageable);

    List<TestCase> findByDeletedOrderByIdDesc(int deleted);

    /** 模块删除时将被删模块下的用例归到父模块，与 PHP tree remove 一致 */
    List<TestCase> findByModuleInAndDeleted(java.util.Collection<Integer> moduleIds, int deleted);
}
