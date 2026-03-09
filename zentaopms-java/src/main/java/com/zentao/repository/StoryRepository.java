package com.zentao.repository;

import com.zentao.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Integer>, JpaSpecificationExecutor<Story> {

    List<Story> findByProductAndDeleted(int productId, int deleted);

    List<Story> findByProjectAndDeleted(int projectId, int deleted);

    List<Story> findByTitleContainingAndDeleted(String title, int deleted, org.springframework.data.domain.Pageable pageable);

    List<Story> findByAssignedToAndDeleted(String account, int deleted);

    List<Story> findByProductAndTypeAndDeleted(int productId, String type, int deleted);
}
