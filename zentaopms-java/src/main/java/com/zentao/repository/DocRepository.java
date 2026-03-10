package com.zentao.repository;

import com.zentao.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocRepository extends JpaRepository<Doc, Integer> {

    List<Doc> findByLibAndDeletedOrderByOrderNumAsc(int libId, int deleted);

    List<Doc> findByLibAndModuleAndDeletedOrderByOrderNumAsc(int libId, int moduleId, int deleted);

    List<Doc> findByProductAndDeleted(int productId, int deleted);

    List<Doc> findByProjectAndDeleted(int projectId, int deleted);
}
