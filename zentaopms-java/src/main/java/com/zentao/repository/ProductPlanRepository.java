package com.zentao.repository;

import com.zentao.entity.ProductPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPlanRepository extends JpaRepository<ProductPlan, Integer> {

    List<ProductPlan> findByProductAndDeletedOrderByBeginAsc(int productId, int deleted);

    List<ProductPlan> findByProductAndBranchAndDeletedOrderByBeginAsc(int productId, String branch, int deleted);

    /** 与 PHP getChildren 一致：父计划下未删除的子计划 */
    List<ProductPlan> findByParentAndDeleted(int parentId, int deleted);
}
