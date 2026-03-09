package com.zentao.repository;

import com.zentao.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    List<TestCase> findByProductAndDeleted(int productId, int deleted);

    List<TestCase> findByLibAndDeleted(int libId, int deleted);

    Page<TestCase> findByOpenedByAndDeletedOrderByIdDesc(String openedBy, int deleted, Pageable pageable);
}
