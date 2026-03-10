package com.zentao.service;

import com.zentao.entity.TestReport;
import com.zentao.repository.TestReportRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestReportService {

    private final TestReportRepository testReportRepository;
    private final ActionService actionService;

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

    /** 测试报告 id→title 下拉用，与 PHP testreport getPairs(productID, appendID) 一致；product≤0 返回全部，appendID>0 时保证该 ID 在结果中 */
    public Map<Integer, String> getPairs(int productId, int appendId) {
        List<TestReport> list = productId > 0
                ? getByProduct(productId)
                : testReportRepository.findByDeletedOrderByIdDesc(0);
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(TestReport::getId, r -> r.getTitle() != null ? r.getTitle() : "", (a, b) -> a, LinkedHashMap::new));
        if (appendId > 0 && !map.containsKey(appendId)) {
            getById(appendId).ifPresent(r -> map.put(r.getId(), r.getTitle() != null ? r.getTitle() : ""));
        }
        return map;
    }

    /** 按测试报告 ID 列表返回 id→title，与其它 pairsByList 约定一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> reportIdList) {
        if (reportIdList == null || reportIdList.isEmpty()) return Map.of();
        List<TestReport> list = testReportRepository.findAllById(reportIdList);
        return list.stream()
                .filter(r -> r.getDeleted() == 0)
                .collect(Collectors.toMap(TestReport::getId, r -> r.getTitle() != null ? r.getTitle() : "", (a, b) -> a));
    }

    /** 创建测试报告，与 PHP testreport create 一致：保存后记录 action 'Opened' */
    @org.springframework.transaction.annotation.Transactional
    public TestReport create(TestReport report) {
        report.setDeleted(0);
        report.setCreatedBy(getCurrentAccount());
        report.setCreatedDate(LocalDateTime.now());
        TestReport saved = testReportRepository.save(report);
        actionService.create("testreport", saved.getId(), "Opened");
        return saved;
    }

    public TestReport update(TestReport report) {
        return testReportRepository.save(report);
    }

    /** 删除测试报告（软删除），与其它模块一致：置 deleted=1 并记录 action 'deleted' */
    @org.springframework.transaction.annotation.Transactional
    public void delete(int reportId) {
        testReportRepository.findById(reportId).ifPresent(r -> {
            r.setDeleted(1);
            testReportRepository.save(r);
            actionService.create("testreport", reportId, "deleted", "", "1");
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
