package com.zentao.service;

import com.zentao.entity.Story;
import com.zentao.repository.StoryRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final ActionService actionService;

    public Optional<Story> getById(int id) {
        return storyRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<Story> getByProduct(int productId) {
        return storyRepository.findByProductAndDeleted(productId, 0);
    }

    public List<Story> getByAssignedTo(String account) {
        return storyRepository.findByAssignedToAndDeleted(account, 0);
    }

    public List<Story> getByProductAndType(int productId, String type) {
        return storyRepository.findByProductAndTypeAndDeleted(productId, type, 0);
    }

    public Page<Story> getList(Specification<Story> spec, Pageable pageable) {
        return storyRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    public Page<Story> getListByType(String type, Specification<Story> spec, Pageable pageable) {
        return storyRepository.findAll(
                Specification.where(spec)
                        .and((root, q, cb) -> cb.equal(root.get("type"), type))
                        .and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    public Story create(Story story) {
        story.setDeleted(0);
        Story saved = storyRepository.save(story);
        actionService.create("story", saved.getId(), "Opened");
        return saved;
    }

    public Story update(Story story) {
        return storyRepository.save(story);
    }

    @Transactional
    public Story close(int storyId, String closedReason) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        story.setStatus("closed");
        story.setClosedBy(getCurrentAccount());
        story.setClosedDate(LocalDateTime.now());
        if (closedReason != null) story.setClosedReason(closedReason);
        Story saved = storyRepository.save(story);
        actionService.create("story", storyId, "Closed");
        return saved;
    }

    @Transactional
    public Story activate(int storyId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        story.setStatus("active");
        story.setActivatedDate(LocalDateTime.now());
        Story saved = storyRepository.save(story);
        actionService.create("story", storyId, "Activated");
        return saved;
    }

    @Transactional
    public void delete(int storyId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        story.setDeleted(1);
        storyRepository.save(story);
        actionService.create("story", storyId, "deleted");
    }

    @Transactional
    public Story assignTo(int storyId, String assignedTo) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        story.setAssignedTo(assignedTo != null ? assignedTo : "");
        Story saved = storyRepository.save(story);
        actionService.create("story", storyId, "Assigned", "", assignedTo != null ? assignedTo : "");
        return saved;
    }

    @Transactional
    public void batchAssignTo(List<Integer> storyIds, String assignedTo) {
        for (Integer id : storyIds) {
            assignTo(id, assignedTo);
        }
    }

    @Transactional
    public void batchClose(List<Integer> storyIds, String closedReason) {
        for (Integer id : storyIds) {
            close(id, closedReason);
        }
    }

    @Transactional
    public List<Integer> batchCreate(List<Story> stories) {
        if (stories == null || stories.isEmpty()) return List.of();
        List<Integer> ids = new java.util.ArrayList<>();
        for (Story s : stories) {
            Story created = create(s);
            ids.add(created.getId());
        }
        return ids;
    }

    @Transactional
    public void batchEdit(List<Integer> storyIds, java.util.Map<String, Object> fields) {
        if (storyIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : storyIds) {
            Story story = getById(id).orElse(null);
            if (story == null) continue;
            if (fields.containsKey("product")) story.setProduct(fields.get("product") instanceof Number n ? n.intValue() : story.getProduct());
            if (fields.containsKey("module")) story.setModule(fields.get("module") instanceof Number n ? n.intValue() : story.getModule());
            if (fields.containsKey("plan")) story.setPlan(fields.get("plan") != null ? fields.get("plan").toString() : story.getPlan());
            if (fields.containsKey("assignedTo")) story.setAssignedTo(fields.get("assignedTo") != null ? fields.get("assignedTo").toString() : story.getAssignedTo());
            if (fields.containsKey("pri")) story.setPri(fields.get("pri") instanceof Number n ? n.intValue() : story.getPri());
            if (fields.containsKey("estimate")) story.setEstimate(fields.get("estimate") != null ? new java.math.BigDecimal(fields.get("estimate").toString()) : story.getEstimate());
            storyRepository.save(story);
            actionService.create("story", id, "Edited");
        }
    }

    @Transactional
    public void batchChangePlan(List<Integer> storyIds, int planId) {
        if (storyIds == null || storyIds.isEmpty()) return;
        String planStr = planId > 0 ? String.valueOf(planId) : "";
        for (Integer id : storyIds) {
            Story story = getById(id).orElse(null);
            if (story == null) continue;
            story.setPlan(planStr);
            storyRepository.save(story);
            actionService.create("story", id, "Changed");
        }
    }

    @Transactional
    public Story linkStory(int storyId, int targetStoryId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        List<String> ids = idsFromLinkStories(story.getLinkStories());
        String sid = String.valueOf(targetStoryId);
        if (!ids.contains(sid)) ids.add(sid);
        story.setLinkStories(String.join(",", ids));
        Story saved = storyRepository.save(story);
        actionService.create("story", storyId, "linkStory", "", sid);
        return saved;
    }

    @Transactional
    public Story unlinkStory(int storyId, int targetStoryId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        List<String> ids = idsFromLinkStories(story.getLinkStories());
        ids.remove(String.valueOf(targetStoryId));
        story.setLinkStories(String.join(",", ids));
        Story saved = storyRepository.save(story);
        actionService.create("story", storyId, "unlinkStory", "", String.valueOf(targetStoryId));
        return saved;
    }

    private static List<String> idsFromLinkStories(String raw) {
        if (raw == null || raw.isBlank()) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>(java.util.Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }

    /** 需求报表摘要（按产品统计各状态数量） */
    public java.util.Map<String, Object> getReport(Integer productId) {
        List<Story> list = productId != null && productId > 0 ? getByProduct(productId) : List.of();
        long draft = list.stream().filter(s -> "draft".equals(s.getStatus())).count();
        long active = list.stream().filter(s -> "active".equals(s.getStatus())).count();
        long closed = list.stream().filter(s -> "closed".equals(s.getStatus())).count();
        long changed = list.stream().filter(s -> "changed".equals(s.getStatus())).count();
        return java.util.Map.of("total", list.size(), "draft", draft, "active", active, "closed", closed, "changed", changed);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
