package com.zentao.repository;

import com.zentao.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {

    List<Job> findByRepoAndDeletedOrderByIdDesc(int repoId, int deleted);

    List<Job> findByProductAndDeletedOrderByIdDesc(int productId, int deleted);

    List<Job> findByServerAndDeletedOrderByIdDesc(int serverId, int deleted);
}
