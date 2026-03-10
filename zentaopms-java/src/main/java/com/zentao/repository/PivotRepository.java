package com.zentao.repository;

import com.zentao.entity.Pivot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PivotRepository extends JpaRepository<Pivot, Integer> {

    List<Pivot> findByDeletedOrderByIdDesc(int deleted);

    List<Pivot> findByDimensionAndDeletedOrderByIdDesc(int dimension, int deleted);

    Optional<Pivot> findByIdAndDeleted(int id, int deleted);
}
