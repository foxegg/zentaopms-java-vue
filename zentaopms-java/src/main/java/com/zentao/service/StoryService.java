package com.zentao.service;

import com.zentao.entity.Relation;
import com.zentao.entity.Story;
import com.zentao.repository.RelationRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final RelationRepository relationRepository;
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

    /** 需求 id->title 下拉用，与 PHP story getPairs(productID) 一致；product 为 0 时返回空 */
    public Map<Integer, String> getPairs(int productId) {
        return getPairs(productId, 0, true);
    }

    /** 与 PHP story getPairs(productID, planID, field, hasParent) 一致：支持按计划、是否含父需求过滤；planID=0 不按计划过滤，hasParent=false 仅非父需求 */
    public Map<Integer, String> getPairs(int productId, int planId, boolean hasParent) {
        if (productId <= 0) return Map.of();
        List<Story> list = getByProduct(productId);
        if (planId > 0) {
            list = list.stream().filter(s -> planContains(s.getPlan(), planId)).toList();
        }
        if (!hasParent) {
            list = list.stream().filter(s -> s.getIsParent() != null && s.getIsParent() == 0).toList();
        }
        return list.stream().collect(Collectors.toMap(Story::getId, s -> s.getTitle() != null ? s.getTitle() : "", (a, b) -> a));
    }

    private static boolean planContains(String plan, int planId) {
        if (plan == null || plan.isBlank()) return false;
        String needle = "," + planId + ",";
        return ("," + plan.trim() + ",").contains(needle);
    }

    /** 按需求 ID 列表返回 id→title，与 PHP story getPairsByList(storyIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> storyIdList) {
        return getPairsByList(storyIdList, false);
    }

    /** 与 PHP getPairsByList 一致：withEmptyOption 为 true 时在结果前加入 0→""（请选择），便于下拉与 PHP 对齐 */
    public Map<Integer, String> getPairsByList(List<Integer> storyIdList, boolean withEmptyOption) {
        Map<Integer, String> base;
        if (storyIdList == null || storyIdList.isEmpty()) {
            base = Map.of();
        } else {
            List<Story> list = storyRepository.findAllById(storyIdList);
            base = list.stream()
                    .filter(s -> s.getDeleted() == 0)
                    .collect(Collectors.toMap(Story::getId, s -> s.getTitle() != null ? s.getTitle() : "", (a, b) -> a));
        }
        if (!withEmptyOption) return base;
        Map<Integer, String> out = new LinkedHashMap<>();
        out.put(0, "");
        out.putAll(base);
        return out;
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

    /** 与 PHP 一致：创建时设置 openedBy、openedDate（当前用户与时间） */
    public Story create(Story story) {
        story.setDeleted(0);
        story.setOpenedBy(getCurrentAccount());
        story.setOpenedDate(LocalDateTime.now());
        Story saved = storyRepository.save(story);
        actionService.create("story", saved.getId(), "Opened");
        return saved;
    }

    /** 与 PHP 一致：仅更新可编辑字段，并设置 lastEditedBy、lastEditedDate，避免请求体未传字段覆盖 openedBy 等 */
    @Transactional
    public Story update(Story story) {
        Story existing = getById(story.getId()).orElseThrow(() -> new RuntimeException("需求不存在"));
        if (story.getProduct() != null) existing.setProduct(story.getProduct());
        if (story.getBranch() != null) existing.setBranch(story.getBranch());
        if (story.getModule() != null) existing.setModule(story.getModule());
        if (story.getPlan() != null) existing.setPlan(story.getPlan());
        if (story.getTitle() != null) existing.setTitle(story.getTitle());
        if (story.getType() != null) existing.setType(story.getType());
        if (story.getCategory() != null) existing.setCategory(story.getCategory());
        if (story.getPri() != null) existing.setPri(story.getPri());
        if (story.getEstimate() != null) existing.setEstimate(story.getEstimate());
        if (story.getStatus() != null) existing.setStatus(story.getStatus());
        if (story.getStage() != null) existing.setStage(story.getStage());
        if (story.getAssignedTo() != null) existing.setAssignedTo(story.getAssignedTo());
        existing.setLastEditedBy(getCurrentAccount());
        existing.setLastEditedDate(LocalDateTime.now());
        Story saved = storyRepository.save(existing);
        actionService.create("story", existing.getId(), "Edited");
        return saved;
    }

    @Transactional
    public Story close(int storyId, String closedReason) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        story.setStatus("closed");
        story.setClosedBy(getCurrentAccount());
        story.setClosedDate(LocalDateTime.now());
        if (closedReason != null) story.setClosedReason(closedReason);
        story.setAssignedTo("closed"); // 与 PHP story close 一致：关闭后置 assignedTo 为 closed
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

    /** 与 PHP story batchAssignTo 一致：跳过已关闭需求、跳过指派未变更 */
    @Transactional
    public void batchAssignTo(List<Integer> storyIds, String assignedTo) {
        if (storyIds == null) return;
        String to = assignedTo != null ? assignedTo : "";
        for (Integer id : storyIds) {
            Story story = getById(id).orElse(null);
            if (story == null) continue;
            if ("closed".equals(story.getStatus())) continue;
            if (to.equals(story.getAssignedTo() != null ? story.getAssignedTo() : "")) continue;
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

    /** 批量修改需求所属模块，与 PHP story batchChangeModule 一致：跳过模块未变更 */
    @Transactional
    public void batchChangeModule(List<Integer> storyIds, int moduleId) {
        if (storyIds == null || storyIds.isEmpty()) return;
        for (Integer sid : storyIds) {
            Story story = getById(sid).orElse(null);
            if (story == null) continue;
            if (moduleId == story.getModule()) continue;
            story.setModule(moduleId);
            storyRepository.save(story);
            actionService.create("story", sid, "Edited");
        }
    }

    /** 批量修改需求分支，与 PHP story batchChangeBranch 一致 */
    @Transactional
    public void batchChangeBranch(List<Integer> storyIds, int branchId) {
        if (storyIds == null || storyIds.isEmpty()) return;
        for (Integer sid : storyIds) {
            Story story = getById(sid).orElse(null);
            if (story == null) continue;
            story.setBranch(branchId);
            storyRepository.save(story);
            actionService.create("story", sid, "Edited");
        }
    }

    /** 与 PHP story linkStories 一致：双向维护 linkStories、同步写入 zt_relation（linkedto/linkedfrom）、action 记为 linkrelatedstory */
    @Transactional
    public Story linkStory(int storyId, int targetStoryId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        Story target = getById(targetStoryId).orElseThrow(() -> new RuntimeException("关联需求不存在"));
        String sid = String.valueOf(targetStoryId);
        String mainId = String.valueOf(storyId);
        List<String> ids = idsFromLinkStories(story.getLinkStories());
        boolean added = !ids.contains(sid);
        if (added) ids.add(sid);
        story.setLinkStories(String.join(",", ids));
        storyRepository.save(story);
        List<String> targetIds = idsFromLinkStories(target.getLinkStories());
        if (!targetIds.contains(mainId)) targetIds.add(mainId);
        target.setLinkStories(String.join(",", targetIds));
        storyRepository.save(target);
        if (added) {
        Relation linkedto = new Relation();
        linkedto.setProduct(story.getProduct() != null ? story.getProduct() : 0);
        linkedto.setAType(normalizeStoryType(story.getType()));
        linkedto.setAID(storyId);
        linkedto.setAVersion(String.valueOf(story.getVersion() != null ? story.getVersion() : 1));
        linkedto.setRelation("linkedto");
        linkedto.setBType(normalizeStoryType(target.getType()));
        linkedto.setBID(targetStoryId);
        linkedto.setBVersion(String.valueOf(target.getVersion() != null ? target.getVersion() : 1));
        relationRepository.save(linkedto);
        Relation linkedfrom = new Relation();
        linkedfrom.setProduct(target.getProduct() != null ? target.getProduct() : 0);
        linkedfrom.setAType(normalizeStoryType(target.getType()));
        linkedfrom.setAID(targetStoryId);
        linkedfrom.setAVersion(String.valueOf(target.getVersion() != null ? target.getVersion() : 1));
        linkedfrom.setRelation("linkedfrom");
        linkedfrom.setBType(normalizeStoryType(story.getType()));
        linkedfrom.setBID(storyId);
        linkedfrom.setBVersion(String.valueOf(story.getVersion() != null ? story.getVersion() : 1));
        relationRepository.save(linkedfrom);
        }
        actionService.create("story", storyId, "linkrelatedstory", "", sid);
        actionService.create("story", targetStoryId, "linkrelatedstory", "", mainId);
        return getById(storyId).orElse(story);
    }

    /** 与 PHP story unlinkStory 一致：双向移除 linkStories、删除 zt_relation 中对应行，并记两条 action（unlinkrelatedstory） */
    @Transactional
    public Story unlinkStory(int storyId, int targetStoryId) {
        Story story = getById(storyId).orElseThrow(() -> new RuntimeException("需求不存在"));
        Story target = getById(targetStoryId).orElse(null);
        List<String> ids = idsFromLinkStories(story.getLinkStories());
        ids.remove(String.valueOf(targetStoryId));
        story.setLinkStories(String.join(",", ids));
        storyRepository.save(story);
        if (target != null) {
            List<String> targetIds = idsFromLinkStories(target.getLinkStories());
            targetIds.remove(String.valueOf(storyId));
            target.setLinkStories(String.join(",", targetIds));
            storyRepository.save(target);
        }
        relationRepository.deleteStoryLink(storyId, targetStoryId);
        actionService.create("story", storyId, "unlinkrelatedstory", "", String.valueOf(targetStoryId));
        actionService.create("story", targetStoryId, "unlinkrelatedstory", "", String.valueOf(storyId));
        return getById(storyId).orElse(story);
    }

    private static List<String> idsFromLinkStories(String raw) {
        if (raw == null || raw.isBlank()) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>(java.util.Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }

    private static String normalizeStoryType(String type) {
        if (type == null || type.isBlank()) return "story";
        return type;
    }

    /**
     * 一次性迁移：将现有需求的 linkStories 数据同步到 zt_relation 表。
     * 用于已有 linkStories 但 zt_relation 为空或不全的部署，与 PHP 表结构对齐后可执行一次。
     * 每对 (storyId, linkedId) 只写入一次，已存在的关联会跳过。
     */
    @Transactional
    public int syncLinkStoriesToRelation() {
        List<Story> all = storyRepository.findAll().stream()
                .filter(s -> s.getDeleted() == 0 && s.getLinkStories() != null && !s.getLinkStories().isBlank())
                .toList();
        int count = 0;
        for (Story story : all) {
            List<String> ids = idsFromLinkStories(story.getLinkStories());
            int storyId = story.getId();
            for (String s : ids) {
                if (!s.matches("\\d+")) continue;
                int linkedId = Integer.parseInt(s);
                if (storyId == linkedId) continue;
                int id1 = Math.min(storyId, linkedId);
                int id2 = Math.max(storyId, linkedId);
                if (relationRepository.existsByAIDAndBIDAndRelation(id1, id2, "linkedto")) continue;
                Story other = getById(linkedId).orElse(null);
                if (other == null) continue;
                Story main = id1 == storyId ? story : other;
                Story target = id1 == storyId ? other : story;
                Relation linkedto = new Relation();
                linkedto.setProduct(main.getProduct() != null ? main.getProduct() : 0);
                linkedto.setAType(normalizeStoryType(main.getType()));
                linkedto.setAID(main.getId());
                linkedto.setAVersion(String.valueOf(main.getVersion() != null ? main.getVersion() : 1));
                linkedto.setRelation("linkedto");
                linkedto.setBType(normalizeStoryType(target.getType()));
                linkedto.setBID(target.getId());
                linkedto.setBVersion(String.valueOf(target.getVersion() != null ? target.getVersion() : 1));
                relationRepository.save(linkedto);
                Relation linkedfrom = new Relation();
                linkedfrom.setProduct(target.getProduct() != null ? target.getProduct() : 0);
                linkedfrom.setAType(normalizeStoryType(target.getType()));
                linkedfrom.setAID(target.getId());
                linkedfrom.setAVersion(String.valueOf(target.getVersion() != null ? target.getVersion() : 1));
                linkedfrom.setRelation("linkedfrom");
                linkedfrom.setBType(normalizeStoryType(main.getType()));
                linkedfrom.setBID(main.getId());
                linkedfrom.setBVersion(String.valueOf(main.getVersion() != null ? main.getVersion() : 1));
                relationRepository.save(linkedfrom);
                count++;
            }
        }
        return count;
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
