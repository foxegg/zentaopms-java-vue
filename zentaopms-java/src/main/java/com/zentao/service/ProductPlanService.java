package com.zentao.service;

import com.zentao.entity.Bug;
import com.zentao.entity.PlanStory;
import com.zentao.entity.ProductPlan;
import com.zentao.repository.BugRepository;
import com.zentao.repository.PlanStoryRepository;
import com.zentao.repository.ProductPlanRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPlanService {

    private final ProductPlanRepository productPlanRepository;
    private final PlanStoryRepository planStoryRepository;
    private final BugRepository bugRepository;
    private final ActionService actionService;

    public Optional<ProductPlan> getById(int id) {
        return productPlanRepository.findById(id).filter(p -> p.getDeleted() == 0);
    }

    public List<ProductPlan> getByProduct(int productId) {
        return productPlanRepository.findByProductAndDeletedOrderByBeginAsc(productId, 0);
    }

    /** 按产品与分支获取计划列表，与 PHP productplan getList(productID, branch) 一致；productID≤0 时返回空列表 */
    public List<ProductPlan> getByProductAndBranch(int productId, String branch) {
        if (productId <= 0) return List.of();
        if (branch != null && !branch.isEmpty() && !"all".equalsIgnoreCase(branch)) {
            return productPlanRepository.findByProductAndBranchAndDeletedOrderByBeginAsc(productId, branch, 0);
        }
        return getByProduct(productId);
    }

    /** 计划 id->title 下拉用，与 PHP productplan getPairs 一致；productID≤0 时返回空 Map */
    public Map<Integer, String> getPairs(int productId, String branch) {
        if (productId <= 0) return Map.of();
        List<ProductPlan> list = getByProductAndBranch(productId, branch);
        return list.stream().collect(Collectors.toMap(ProductPlan::getId, p -> p.getTitle() != null ? p.getTitle() : "", (a, b) -> a));
    }

    /** 按计划 ID 列表返回 id→title，空列表返回空 Map；与 getPairs 互补，便于按 ID 列表取计划名称 */
    public Map<Integer, String> getPairsByList(List<Integer> planIdList) {
        if (planIdList == null || planIdList.isEmpty()) return Map.of();
        List<ProductPlan> list = productPlanRepository.findAllById(planIdList);
        return list.stream()
                .filter(p -> p.getDeleted() == 0)
                .collect(Collectors.toMap(ProductPlan::getId, p -> p.getTitle() != null ? p.getTitle() : "", (a, b) -> a));
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public ProductPlan create(ProductPlan plan) {
        plan.setDeleted(0);
        plan.setCreatedBy(getCurrentAccount());
        plan.setCreatedDate(LocalDateTime.now());
        ProductPlan saved = productPlanRepository.save(plan);
        actionService.create("productplan", saved.getId(), "opened");
        return saved;
    }

    public ProductPlan update(ProductPlan plan) {
        return productPlanRepository.save(plan);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    @Transactional
    public ProductPlan start(int planId) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        p.setStatus("doing");
        ProductPlan saved = productPlanRepository.save(p);
        actionService.create("productplan", planId, "started");
        return saved;
    }

    @Transactional
    public ProductPlan finish(int planId) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        p.setStatus("done");
        p.setFinishedDate(LocalDateTime.now());
        ProductPlan saved = productPlanRepository.save(p);
        actionService.create("productplan", planId, "finished");
        return saved;
    }

    @Transactional
    public ProductPlan close(int planId, String closedReason) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        p.setStatus("closed");
        p.setClosedDate(LocalDateTime.now());
        if (closedReason != null) p.setClosedReason(closedReason);
        ProductPlan saved = productPlanRepository.save(p);
        actionService.create("productplan", planId, "closed");
        return saved;
    }

    @Transactional
    public ProductPlan activate(int planId) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        p.setStatus("wait");
        p.setClosedDate(null);
        p.setClosedReason(null);
        ProductPlan saved = productPlanRepository.save(p);
        actionService.create("productplan", planId, "activated");
        return saved;
    }

    /** 与 PHP productplan batchChangeStatus 一致：仅对状态发生变更的计划更新并记 edited */
    @Transactional
    public void batchChangeStatus(List<Integer> planIds, String status, java.util.Map<Integer, String> closedReasons) {
        if (planIds == null || status == null || status.isEmpty()) return;
        if ("closed".equals(status) && (closedReasons == null || closedReasons.isEmpty())) return;
        for (Integer planId : planIds) {
            if (planId == null || planId <= 0) continue;
            ProductPlan p = getById(planId).orElse(null);
            if (p == null) continue;
            if (status.equals(p.getStatus())) continue;
            if ("doing".equals(status)) {
                p.setStatus("doing");
                productPlanRepository.save(p);
                actionService.create("productplan", planId, "started");
            } else if ("done".equals(status)) {
                p.setStatus("done");
                p.setFinishedDate(LocalDateTime.now());
                productPlanRepository.save(p);
                actionService.create("productplan", planId, "finished");
            } else if ("closed".equals(status)) {
                String reason = closedReasons != null ? closedReasons.get(planId) : null;
                close(planId, reason);
            } else if ("wait".equals(status)) {
                activate(planId);
            }
        }
    }

    /** 与 PHP productplan delete 一致：parent&lt;0 禁止删除（父计划）；删除后若 parent&gt;0 调用 changeParentField */
    @Transactional
    public void delete(int planId) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        if (p.getParent() != null && p.getParent() < 0) {
            throw new RuntimeException("不能删除父计划");
        }
        Integer parentId = p.getParent() != null && p.getParent() > 0 ? p.getParent() : null;
        p.setDeleted(1);
        productPlanRepository.save(p);
        actionService.create("productplan", planId, "deleted");
        if (parentId != null) changeParentField(planId, parentId);
    }

    /** 与 PHP changeParentField 一致：删除子计划后更新父计划的 parent 标记（无子时改为 0，否则 -1），并将被删计划的 parent 置 0 */
    private void changeParentField(int deletedPlanId, int parentPlanId) {
        int childCount = productPlanRepository.findByParentAndDeleted(parentPlanId, 0).size();
        productPlanRepository.findById(parentPlanId).filter(pp -> pp.getDeleted() == 0).ifPresent(pp -> {
            pp.setParent(childCount == 0 ? 0 : -1);
            productPlanRepository.save(pp);
        });
        productPlanRepository.findById(deletedPlanId).ifPresent(pp -> {
            pp.setParent(0);
            productPlanRepository.save(pp);
        });
    }

    public List<PlanStory> getStoriesByPlan(int planId) {
        return planStoryRepository.findByPlanOrderByOrderNumAsc(planId);
    }

    /** 计划关联的 Bug 列表（zt_bug.plan），与 PHP productplan view type=bug 一致 */
    public List<Bug> getBugsByPlan(int planId) {
        return bugRepository.findByPlanAndDeleted(planId, 0);
    }

    @Transactional
    public PlanStory linkStory(int planId, int storyId) {
        if (planStoryRepository.findByPlanAndStory(planId, storyId).isPresent()) {
            throw new RuntimeException("需求已关联到该计划");
        }
        PlanStory ps = new PlanStory();
        ps.setPlan(planId);
        ps.setStory(storyId);
        ps.setOrderNum(0);
        PlanStory saved = planStoryRepository.save(ps);
        actionService.create("story", storyId, "linked2plan", "", String.valueOf(planId));
        return saved;
    }

    @Transactional
    public void unlinkStory(int planId, int storyId) {
        planStoryRepository.deleteByPlanAndStory(planId, storyId);
        actionService.create("story", storyId, "unlinkedfromplan", "", String.valueOf(planId));
    }

    @Transactional
    public void batchUnlinkStory(int planId, List<Integer> storyIds) {
        if (storyIds == null) return;
        for (Integer storyId : storyIds) {
            if (storyId != null && storyId > 0) {
                planStoryRepository.deleteByPlanAndStory(planId, storyId);
                actionService.create("story", storyId, "unlinkedfromplan", "", String.valueOf(planId));
            }
        }
    }

    /** 批量关联需求，与 PHP productplan linkStory(POST stories 数组) 一致；已关联的跳过 */
    @Transactional
    public void batchLinkStory(int planId, List<Integer> storyIds) {
        if (storyIds == null) return;
        for (Integer storyId : storyIds) {
            if (storyId == null || storyId <= 0) continue;
            if (planStoryRepository.findByPlanAndStory(planId, storyId).isPresent()) continue;
            try {
                linkStory(planId, storyId);
            } catch (RuntimeException e) {
                if (!e.getMessage().contains("已关联")) throw e;
            }
        }
    }

    /** 关联 Bug 到计划，与 PHP productplan linkBug 一致（更新 zt_bug.plan） */
    @Transactional
    public void linkBug(int planId, List<Integer> bugIds) {
        if (bugIds == null) return;
        for (Integer bugId : bugIds) {
            if (bugId == null || bugId <= 0) continue;
            bugRepository.findById(bugId).ifPresent(b -> {
                if (Integer.valueOf(planId).equals(b.getPlan())) return;
                b.setPlan(planId);
                bugRepository.save(b);
                actionService.create("bug", bugId, "linked2plan", "", String.valueOf(planId));
            });
        }
        actionService.create("productplan", planId, "linkbug", "", String.join(",", bugIds.stream().map(String::valueOf).toList()));
    }

    /** 取消 Bug 与计划的关联，与 PHP productplan unlinkBug 一致 */
    @Transactional
    public void unlinkBug(int planId, int bugId) {
        bugRepository.findById(bugId).filter(b -> Integer.valueOf(planId).equals(b.getPlan())).ifPresent(b -> {
            b.setPlan(0);
            bugRepository.save(b);
            actionService.create("bug", bugId, "unlinkedfromplan", "", String.valueOf(planId));
        });
    }

    /** 批量取消 Bug 与计划的关联，与 PHP productplan batchUnlinkBug 一致 */
    @Transactional
    public void batchUnlinkBug(int planId, List<Integer> bugIds) {
        if (bugIds == null) return;
        for (Integer bugId : bugIds) {
            if (bugId != null && bugId > 0) unlinkBug(planId, bugId);
        }
        actionService.create("productplan", planId, "unlinkbug", "", String.join(",", bugIds.stream().map(String::valueOf).toList()));
    }
}
