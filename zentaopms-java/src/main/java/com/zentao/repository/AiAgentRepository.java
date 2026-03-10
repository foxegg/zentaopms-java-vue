package com.zentao.repository;

import com.zentao.entity.AiAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiAgentRepository extends JpaRepository<AiAgent, Integer> {

    List<AiAgent> findByDeletedOrderByIdAsc(int deleted);

    List<AiAgent> findByModuleAndDeleted(String module, int deleted);

    Optional<AiAgent> findByIdAndDeleted(int id, int deleted);
}
