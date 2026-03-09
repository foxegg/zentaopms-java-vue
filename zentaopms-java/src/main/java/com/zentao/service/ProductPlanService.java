package com.zentao.service;

import com.zentao.entity.PlanStory;
import com.zentao.entity.ProductPlan;
import com.zentao.repository.PlanStoryRepository;
import com.zentao.repository.ProductPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPlanService {

    private final ProductPlanRepository productPlanRepository;
    private final PlanStoryRepository planStoryRepository;
    private final ActionService actionService;

    public Optional<ProductPlan> getById(int id) {
        return productPlanRepository.findById(id).filter(p -> p.getDeleted() == 0);
    }

    public List<ProductPlan> getByProduct(int productId) {
        return productPlanRepository.findByProductAndDeletedOrderByBeginAsc(productId, 0);
    }

    public ProductPlan create(ProductPlan plan) {
        plan.setDeleted(0);
        ProductPlan saved = productPlanRepository.save(plan);
        actionService.create("productplan", saved.getId(), "opened");
        return saved;
    }

    public ProductPlan update(ProductPlan plan) {
        return productPlanRepository.save(plan);
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

    @Transactional
    public void delete(int planId) {
        ProductPlan p = getById(planId).orElseThrow(() -> new RuntimeException("计划不存在"));
        p.setDeleted(1);
        productPlanRepository.save(p);
        actionService.create("productplan", planId, "deleted");
    }

    public List<PlanStory> getStoriesByPlan(int planId) {
        return planStoryRepository.findByPlanOrderByOrderNumAsc(planId);
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
}
