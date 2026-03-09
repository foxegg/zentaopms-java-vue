package com.zentao.repository;

import com.zentao.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Integer> {

    List<Release> findByProductAndDeleted(int productId, int deleted);

    List<Release> findByProductAndDeletedOrderByDateDesc(int productId, int deleted);

    @Query(value = "SELECT * FROM zt_release WHERE deleted = 0 AND FIND_IN_SET(:projectId, project) > 0 ORDER BY date DESC, id DESC", nativeQuery = true)
    List<Release> findByProjectAndDeleted(@Param("projectId") int projectId);
}
