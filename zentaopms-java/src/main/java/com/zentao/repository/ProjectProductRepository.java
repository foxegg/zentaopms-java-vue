package com.zentao.repository;

import com.zentao.entity.ProjectProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectProductRepository extends JpaRepository<ProjectProduct, Integer> {

    List<ProjectProduct> findByProject(int projectId);

    List<ProjectProduct> findByProduct(int productId);

    @Modifying
    @Query("delete from ProjectProduct p where p.project = :projectId")
    void deleteByProject(@Param("projectId") int projectId);
}
