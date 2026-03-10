package com.zentao.repository;

import com.zentao.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Page<Score> findByAccountOrderByTimeDesc(String account, Pageable pageable);
    Page<Score> findAllByOrderByTimeDesc(Pageable pageable);
}
