package com.zentao.repository;

import com.zentao.entity.KanbanSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KanbanSpaceRepository extends JpaRepository<KanbanSpace, Integer> {

    List<KanbanSpace> findByDeletedOrderByOrderNumAsc(int deleted);

    List<KanbanSpace> findByTypeAndDeletedOrderByOrderNumAsc(String type, int deleted);
}
