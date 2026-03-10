package com.zentao.repository;

import com.zentao.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoRepository extends JpaRepository<Repo, Integer> {

    List<Repo> findByDeletedOrderByIdAsc(int deleted);

    List<Repo> findByDeletedAndScmOrderByIdAsc(int deleted, String scm);
}
