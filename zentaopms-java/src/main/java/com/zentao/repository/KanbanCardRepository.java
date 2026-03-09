package com.zentao.repository;

import com.zentao.entity.KanbanCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, Integer> {

    List<KanbanCard> findByKanbanAndDeletedOrderByOrderNumAsc(int kanbanId, int deleted);

    List<KanbanCard> findByRegionAndDeletedOrderByOrderNumAsc(int regionId, int deleted);
}
