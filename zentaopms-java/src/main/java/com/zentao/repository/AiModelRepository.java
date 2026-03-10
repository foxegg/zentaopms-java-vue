package com.zentao.repository;

import com.zentao.entity.AiModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiModelRepository extends JpaRepository<AiModel, Integer> {

    List<AiModel> findByDeletedOrderByIdDesc(int deleted);

    Optional<AiModel> findByIdAndDeleted(int id, int deleted);
}
