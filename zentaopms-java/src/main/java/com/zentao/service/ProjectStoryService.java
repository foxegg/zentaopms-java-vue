package com.zentao.service;

import com.zentao.entity.Project;
import com.zentao.entity.ProjectStory;
import com.zentao.entity.Story;
import com.zentao.repository.ProjectRepository;
import com.zentao.repository.ProjectStoryRepository;
import com.zentao.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectStoryService {

    private final ProjectStoryRepository projectStoryRepository;
    private final ProjectRepository projectRepository;
    private final StoryRepository storyRepository;

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

    /** 批量关联需求，与 PHP execution linkStory(POST stories 数组) 一致；已关联的跳过 */
    @Transactional
    public void batchLinkStory(int projectId, List<Integer> storyIds, int productID, int branch) {
        if (storyIds == null || storyIds.isEmpty()) return;
        List<Integer> ids = storyIds.stream().filter(s -> s != null && s > 0).distinct().toList();
        if (ids.isEmpty()) return;
        Map<Integer, Story> storyMap = storyRepository.findByIdIn(ids).stream().collect(Collectors.toMap(Story::getId, s -> s, (a, b) -> a));
        for (Integer storyId : ids) {
            if (projectStoryRepository.findByProjectAndStory(projectId, storyId).isPresent()) continue;
            Story s = storyMap.get(storyId);
            int pid = productID > 0 ? productID : (s != null && s.getProduct() != null ? s.getProduct() : 0);
            int br = (s != null && s.getBranch() != null ? s.getBranch() : 0);
            if (branch != 0) br = branch;
            try {
                linkStory(projectId, storyId, pid, br);
            } catch (RuntimeException e) {
                if (!e.getMessage().contains("已关联")) throw e;
            }
        }
    }

    /** 与 PHP getExecutionStories 一致：返回已关联到该项目下执行（sprint/stage/kanban）的需求列表，用于批量移除时的提示 */
    public List<Map<String, Object>> getExecutionStories(int projectID, List<Integer> storyIdList) {
        if (storyIdList == null || storyIdList.isEmpty()) return List.of();
        List<Project> executions = projectRepository.findByProjectAndTypeInAndDeleted(projectID, List.of("sprint", "stage", "kanban"), 0);
        if (executions.isEmpty()) return List.of();
        List<Integer> executionIds = executions.stream().map(Project::getId).toList();
        List<ProjectStory> list = projectStoryRepository.findByStoryInAndProjectIn(storyIdList, executionIds);
        if (list.isEmpty()) return List.of();
        List<Integer> storyIds = list.stream().map(ProjectStory::getStory).distinct().toList();
        List<Story> stories = storyRepository.findByIdIn(storyIds);
        Map<Integer, String> storyTitleMap = stories.stream().collect(Collectors.toMap(Story::getId, s -> s.getTitle() != null ? s.getTitle() : "", (a, b) -> a));
        Map<Integer, String> execNameMap = executions.stream().collect(Collectors.toMap(Project::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
        List<Map<String, Object>> result = new ArrayList<>();
        for (ProjectStory ps : list) {
            result.add(Map.<String, Object>of(
                    "id", ps.getStory(),
                    "title", storyTitleMap.getOrDefault(ps.getStory(), ""),
                    "executionID", ps.getProject(),
                    "execution", execNameMap.getOrDefault(ps.getProject(), "")
            ));
        }
        return result;
    }
}
