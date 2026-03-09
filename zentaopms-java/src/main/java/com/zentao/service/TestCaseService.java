package com.zentao.service;

import com.zentao.entity.TestCase;
import com.zentao.entity.TestRun;
import com.zentao.repository.TestCaseRepository;
import com.zentao.repository.TestRunRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestRunRepository testRunRepository;

    public Optional<TestCase> getById(int id) {
        return testCaseRepository.findById(id).filter(c -> c.getDeleted() == 0);
    }

    public List<TestCase> getByProduct(int productId) {
        return testCaseRepository.findByProductAndDeleted(productId, 0);
    }

    public List<TestCase> getByLib(int libId) {
        return testCaseRepository.findByLibAndDeleted(libId, 0);
    }

    /** 按创建人分页查询用例，对应 PHP testcase getByOpenedBy. */
    public Page<TestCase> getByOpenedBy(String account, Pageable pageable) {
        if (account == null || account.isEmpty()) return Page.empty(pageable);
        return testCaseRepository.findByOpenedByAndDeletedOrderByIdDesc(account, 0, pageable);
    }

    /** 按指派给某人分页查询测试执行( run )，对应 PHP testcase getByAssignedTo（返回 run 列表）. */
    public Page<TestRun> getRunsByAssignedTo(String account, Pageable pageable) {
        if (account == null || account.isEmpty()) return Page.empty(pageable);
        return testRunRepository.findByAssignedToOrderByIdDesc(account, pageable);
    }

    public TestCase create(TestCase testCase) {
        testCase.setDeleted(0);
        return testCaseRepository.save(testCase);
    }

    public TestCase update(TestCase testCase) {
        return testCaseRepository.save(testCase);
    }

    public void delete(int caseId) {
        testCaseRepository.findById(caseId).ifPresent(c -> {
            c.setDeleted(1);
            testCaseRepository.save(c);
        });
    }

    public java.util.List<Integer> batchCreate(java.util.List<TestCase> cases) {
        if (cases == null || cases.isEmpty()) return java.util.List.of();
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        for (TestCase c : cases) {
            TestCase created = create(c);
            ids.add(created.getId());
        }
        return ids;
    }

    @org.springframework.transaction.annotation.Transactional
    public void batchEdit(java.util.List<Integer> caseIds, java.util.Map<String, Object> fields) {
        if (caseIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : caseIds) {
            TestCase c = getById(id).orElse(null);
            if (c == null) continue;
            if (fields.containsKey("title")) c.setTitle(fields.get("title") != null ? fields.get("title").toString() : c.getTitle());
            if (fields.containsKey("module")) c.setModule(fields.get("module") instanceof Number n ? n.intValue() : c.getModule());
            if (fields.containsKey("type")) c.setType(fields.get("type") != null ? fields.get("type").toString() : c.getType());
            if (fields.containsKey("pri")) c.setPri(fields.get("pri") instanceof Number n ? n.intValue() : c.getPri());
            testCaseRepository.save(c);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void batchDelete(java.util.List<Integer> caseIds) {
        if (caseIds != null) for (Integer id : caseIds) delete(id);
    }

    @org.springframework.transaction.annotation.Transactional
    public TestCase review(int caseId, String result) {
        TestCase c = getById(caseId).orElseThrow(() -> new RuntimeException("用例不存在"));
        c.setStatus(result != null ? result : "reviewed");
        c.setReviewedBy(getCurrentAccount());
        c.setReviewedDate(java.time.LocalDate.now());
        return testCaseRepository.save(c);
    }

    @org.springframework.transaction.annotation.Transactional
    public void batchReview(java.util.List<Integer> caseIds, String result) {
        if (caseIds != null) for (Integer id : caseIds) review(id, result);
    }

    @org.springframework.transaction.annotation.Transactional
    public TestCase confirmStoryChange(int caseId, int storyId) {
        TestCase c = getById(caseId).orElseThrow(() -> new RuntimeException("用例不存在"));
        c.setStory(storyId);
        c.setStoryVersion((c.getStoryVersion() != null ? c.getStoryVersion() : 1) + 1);
        return testCaseRepository.save(c);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }
}
