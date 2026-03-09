package com.zentao.service;

import com.zentao.entity.ProjectStory;
import com.zentao.repository.ProjectStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectStoryService {

    private final ProjectStoryRepository projectStoryRepository;

    public List<ProjectStory> getByProject(int projectId) {
        return projectStoryRepository.findByProjectOrderByOrderNumAsc(projectId);
    }

    public List<ProjectStory> getByStory(int storyId) {
        return projectStoryRepository.findByStory(storyId);
    }

    @Transactional
    public ProjectStory linkStory(int projectId, int storyId, int productId, int branch) {
        projectStoryRepository.findByProjectAndStory(projectId, storyId).ifPresent(ps -> {
            throw new RuntimeException("需求已关联到该项目");
        });
        ProjectStory ps = new ProjectStory();
        ps.setProject(projectId);
        ps.setStory(storyId);
        ps.setProduct(productId > 0 ? productId : 0);
        ps.setBranch(branch);
        ps.setVersion(1);
        ps.setOrderNum(0);
        return projectStoryRepository.save(ps);
    }

    @Transactional
    public void unlinkStory(int projectId, int storyId) {
        projectStoryRepository.deleteByProjectAndStory(projectId, storyId);
    }

    @Transactional
    public void batchUnlinkStory(int projectId, List<Integer> storyIds) {
        if (storyIds == null) return;
        for (Integer storyId : storyIds) {
            if (storyId != null && storyId > 0) {
                projectStoryRepository.deleteByProjectAndStory(projectId, storyId);
            }
        }
    }
}
