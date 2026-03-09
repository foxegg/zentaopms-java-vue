package com.zentao.repository;

import com.zentao.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByAccountOrderByTimeDesc(String account, org.springframework.data.domain.Pageable pageable);
    List<Score> findAllByOrderByTimeDesc(org.springframework.data.domain.Pageable pageable);
}
