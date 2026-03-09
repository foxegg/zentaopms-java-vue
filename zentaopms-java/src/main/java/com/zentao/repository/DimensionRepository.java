package com.zentao.repository;

import com.zentao.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
    List<Dimension> findByDeletedOrderByIdAsc(int deleted);
}
