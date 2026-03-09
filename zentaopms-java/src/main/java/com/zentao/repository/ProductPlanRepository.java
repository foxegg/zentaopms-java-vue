package com.zentao.repository;

import com.zentao.entity.ProductPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPlanRepository extends JpaRepository<ProductPlan, Integer> {

    List<ProductPlan> findByProductAndDeletedOrderByBeginAsc(int productId, int deleted);
}
