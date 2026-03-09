package com.zentao.service;

import com.zentao.entity.WeeklyReport;
import com.zentao.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    public Optional<WeeklyReport> getById(int id) {
        return weeklyReportRepository.findById(id);
    }

    public List<WeeklyReport> getByProject(int projectId) {
        return weeklyReportRepository.findByProjectOrderByWeekStartDesc(projectId);
    }

    public WeeklyReport create(WeeklyReport report) {
        return weeklyReportRepository.save(report);
    }

    public WeeklyReport update(WeeklyReport report) {
        return weeklyReportRepository.save(report);
    }

    public void delete(int id) {
        weeklyReportRepository.deleteById(id);
    }
}
