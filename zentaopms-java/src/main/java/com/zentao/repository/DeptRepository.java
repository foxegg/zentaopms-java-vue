package com.zentao.repository;

import com.zentao.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptRepository extends JpaRepository<Dept, Integer> {

    List<Dept> findByParentOrderByOrderNumAsc(int parentId);
}
