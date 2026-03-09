package com.zentao.repository;

import com.zentao.entity.DocLib;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocLibRepository extends JpaRepository<DocLib, Integer> {

    List<DocLib> findByProductAndDeletedOrderByOrderNumAsc(int productId, int deleted);

    List<DocLib> findByProjectAndDeletedOrderByOrderNumAsc(int projectId, int deleted);

    List<DocLib> findByParentAndDeletedOrderByOrderNumAsc(int parentId, int deleted);

    List<DocLib> findByDeletedOrderByOrderNumAsc(int deleted);
}
