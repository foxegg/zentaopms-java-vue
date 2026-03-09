package com.zentao.repository;

import com.zentao.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Integer> {

    List<WeeklyReport> findByProjectOrderByWeekStartDesc(int projectId);

    Optional<WeeklyReport> findByProjectAndWeekStart(int projectId, java.time.LocalDate weekStart);
}
