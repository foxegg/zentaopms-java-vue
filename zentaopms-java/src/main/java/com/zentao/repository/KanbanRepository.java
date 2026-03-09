package com.zentao.repository;

import com.zentao.entity.Kanban;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KanbanRepository extends JpaRepository<Kanban, Integer> {

    List<Kanban> findBySpaceAndDeletedOrderByOrderNumAsc(int spaceId, int deleted);
}
