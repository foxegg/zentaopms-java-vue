package com.zentao.repository;

import com.zentao.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    List<Team> findByRootAndTypeOrderByOrderNumAsc(int root, String type);

    void deleteByRootAndType(int root, String type);
}
