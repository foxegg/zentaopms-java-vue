package com.zentao.repository;

import com.zentao.entity.ProjectStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectStoryRepository extends JpaRepository<ProjectStory, Integer> {

    List<ProjectStory> findByProjectOrderByOrderNumAsc(int projectId);

    List<ProjectStory> findByStory(int storyId);

    Optional<ProjectStory> findByProjectAndStory(int projectId, int storyId);

    void deleteByProjectAndStory(int projectId, int storyId);
}
