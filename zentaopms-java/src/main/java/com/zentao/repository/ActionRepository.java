package com.zentao.repository;

import com.zentao.entity.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {

    List<Action> findByObjectTypeAndObjectIDOrderByDateDesc(String objectType, int objectId);

    Page<Action> findByObjectTypeAndObjectIDOrderByDateDesc(String objectType, int objectId, Pageable pageable);

    List<Action> findByActorOrderByDateDesc(String actor, Pageable pageable);

    List<Action> findByProjectOrderByDateDesc(Integer project, Pageable pageable);

    Page<Action> findByExecutionOrderByDateDesc(Integer execution, Pageable pageable);
}
