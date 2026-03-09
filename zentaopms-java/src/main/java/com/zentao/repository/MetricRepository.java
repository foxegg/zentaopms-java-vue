package com.zentao.repository;

import com.zentao.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Integer> {

    List<Metric> findByDeletedOrderByOrderNumAscIdAsc(int deleted);
}
