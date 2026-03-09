package com.zentao.repository;

import com.zentao.entity.PlanStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanStoryRepository extends JpaRepository<PlanStory, Integer> {

    List<PlanStory> findByPlanOrderByOrderNumAsc(int planId);

    Optional<PlanStory> findByPlanAndStory(int planId, int storyId);

    void deleteByPlanAndStory(int planId, int storyId);
}
