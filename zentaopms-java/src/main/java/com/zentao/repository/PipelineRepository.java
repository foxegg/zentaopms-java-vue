package com.zentao.repository;

import com.zentao.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineRepository extends JpaRepository<Pipeline, Integer> {
    List<Pipeline> findByDeletedOrderByIdAsc(int deleted);
    List<Pipeline> findByTypeAndDeletedOrderByIdAsc(String type, int deleted);
}
