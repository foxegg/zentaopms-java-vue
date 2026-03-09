package com.zentao.repository;

import com.zentao.entity.Cron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CronRepository extends JpaRepository<Cron, Integer> {

    List<Cron> findAllByOrderByIdAsc();
}
