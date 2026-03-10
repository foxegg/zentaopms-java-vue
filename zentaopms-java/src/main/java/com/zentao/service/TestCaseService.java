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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /** 用例 id→title 下拉用，与 PHP testcase getPairsByProduct(productID) 一致；product 为 0 时返回空 */
    public Map<Integer, String> getPairs(int productId) {
        if (productId <= 0) return Map.of();
        List<TestCase> list = getByProduct(productId);
        return list.stream().collect(Collectors.toMap(TestCase::getId, c -> c.getTitle() != null ? c.getTitle() : "", (a, b) -> a));
    }

    /** 按用例 ID 列表返回 id→title，空列表返回空 Map；便于关联用例展示名称 */
    public Map<Integer, String> getPairsByList(List<Integer> caseIdList) {
        if (caseIdList == null || caseIdList.isEmpty()) return Map.of();
        List<TestCase> list = testCaseRepository.findAllById(caseIdList);
        return list.stream()
                .filter(c -> c.getDeleted() == 0)
                .collect(Collectors.toMap(TestCase::getId, c -> c.getTitle() != null ? c.getTitle() : "", (a, b) -> a));
    }

    /** 全部用例列表（未删除），无 product/lib 时与 PHP 列表一致 */
    public List<TestCase> getList() {
        return testCaseRepository.findByDeletedOrderByIdDesc(0);
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

    /** 与 PHP 一致：创建时设置 openedBy、openedDate（当前用户与时间） */
    public TestCase create(TestCase testCase) {
        testCase.setDeleted(0);
        testCase.setOpenedBy(getCurrentAccount());
        testCase.setOpenedDate(LocalDateTime.now());
        return testCaseRepository.save(testCase);
    }

    /** 与 PHP 一致：仅更新可编辑字段，并设置 lastEditedBy、lastEditedDate，避免请求体未传字段覆盖 openedBy 等 */
    @Transactional
    public TestCase update(TestCase testCase) {
        TestCase existing = getById(testCase.getId()).orElseThrow(() -> new RuntimeException("用例不存在"));
        if (testCase.getProduct() != null) existing.setProduct(testCase.getProduct());
        if (testCase.getLib() != null) existing.setLib(testCase.getLib());
        if (testCase.getModule() != null) existing.setModule(testCase.getModule());
        if (testCase.getStory() != null) existing.setStory(testCase.getStory());
        if (testCase.getTitle() != null) existing.setTitle(testCase.getTitle());
        if (testCase.getPrecondition() != null) existing.setPrecondition(testCase.getPrecondition());
        if (testCase.getKeywords() != null) existing.setKeywords(testCase.getKeywords());
        if (testCase.getPri() != null) existing.setPri(testCase.getPri());
        if (testCase.getType() != null) existing.setType(testCase.getType());
        if (testCase.getStatus() != null) existing.setStatus(testCase.getStatus());
        existing.setLastEditedBy(getCurrentAccount());
        existing.setLastEditedDate(LocalDateTime.now());
        return testCaseRepository.save(existing);
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
            if (fields.containsKey("branch")) c.setBranch(fields.get("branch") instanceof Number n ? n.intValue() : c.getBranch());
            if (fields.containsKey("type")) c.setType(fields.get("type") != null ? fields.get("type").toString() : c.getType());
            if (fields.containsKey("pri")) c.setPri(fields.get("pri") instanceof Number n ? n.intValue() : c.getPri());
            testCaseRepository.save(c);
        }
    }

    /** 与 PHP testcase batchChangeCaseModule 一致：moduleId 范围 0..16777215，跳过模块未变更的用例 */
    @org.springframework.transaction.annotation.Transactional
    public void batchChangeModule(java.util.List<Integer> caseIds, int moduleId) {
        if (caseIds == null || caseIds.isEmpty()) return;
        if (moduleId < 0 || moduleId > 16777215) return; // mediumint unsigned
        for (Integer id : caseIds) {
            TestCase c = getById(id).orElse(null);
            if (c == null) continue;
            if (moduleId == c.getModule()) continue;
            c.setModule(moduleId);
            testCaseRepository.save(c);
        }
    }

    /** 批量修改用例分支，与 PHP testcase batchChangeBranch 一致 */
    @org.springframework.transaction.annotation.Transactional
    public void batchChangeBranch(java.util.List<Integer> caseIds, int branchId) {
        if (caseIds == null) return;
        batchEdit(caseIds, java.util.Map.of("branch", branchId));
    }

    /** 批量修改用例类型，与 PHP testcase batchChangeType 一致 */
    @org.springframework.transaction.annotation.Transactional
    public void batchChangeType(java.util.List<Integer> caseIds, String type) {
        if (caseIds == null || type == null) return;
        batchEdit(caseIds, java.util.Map.of("type", type));
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
