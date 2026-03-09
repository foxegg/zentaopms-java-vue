package com.zentao.service;

import com.zentao.entity.TestReport;
import com.zentao.repository.TestReportRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestReportService {

    private final TestReportRepository testReportRepository;

    public Optional<TestReport> getById(int id) {
        return testReportRepository.findById(id).filter(r -> r.getDeleted() == 0);
    }

    public List<TestReport> getByProject(int projectId) {
        return testReportRepository.findByProjectAndDeletedOrderByIdDesc(projectId, 0);
    }

    public List<TestReport> getByProduct(int productId) {
        return testReportRepository.findByProductAndDeletedOrderByIdDesc(productId, 0);
    }

    public List<TestReport> getByExecution(int executionId) {
        return testReportRepository.findByExecutionAndDeletedOrderByIdDesc(executionId, 0);
    }

    public TestReport create(TestReport report) {
        report.setDeleted(0);
        report.setCreatedBy(getCurrentAccount());
        report.setCreatedDate(LocalDateTime.now());
        return testReportRepository.save(report);
    }

    public TestReport update(TestReport report) {
        return testReportRepository.save(report);
    }

    public void delete(int reportId) {
        testReportRepository.findById(reportId).ifPresent(r -> {
            r.setDeleted(1);
            testReportRepository.save(r);
        });
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
