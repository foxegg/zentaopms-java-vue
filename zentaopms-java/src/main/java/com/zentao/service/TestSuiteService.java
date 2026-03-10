package com.zentao.service;

import com.zentao.entity.SuiteCase;
import com.zentao.entity.TestSuite;
import com.zentao.repository.TestCaseRepository;
import com.zentao.repository.SuiteCaseRepository;
import com.zentao.repository.TestSuiteRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
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
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final SuiteCaseRepository suiteCaseRepository;
    private final TestCaseRepository testCaseRepository;

    public Optional<TestSuite> getById(int id) {
        return testSuiteRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<TestSuite> getByProduct(int productId) {
        return testSuiteRepository.findByProductAndDeletedOrderByOrderNumAsc(productId, 0);
    }

    public List<TestSuite> getByProject(int projectId) {
        return testSuiteRepository.findByProjectAndDeletedOrderByOrderNumAsc(projectId, 0);
    }

    /** 测试套件 id→name 下拉用，与 PHP testsuite getSuitePairs(productID) 一致；product≤0 返回空；仅含 type=public 或 (type=private 且 addedBy=当前用户) */
    public Map<Integer, String> getPairs(int productId) {
        if (productId <= 0) return Map.of();
        String account = getCurrentAccount();
        List<TestSuite> list = getByProduct(productId).stream()
                .filter(s -> "public".equalsIgnoreCase(s.getType())
                        || ("private".equalsIgnoreCase(s.getType()) && account != null && account.equals(s.getAddedBy())))
                .toList();
        return list.stream().collect(Collectors.toMap(TestSuite::getId, s -> s.getName() != null ? s.getName() : "", (a, b) -> a));
    }

    /** 用例库 id→name 下拉用，与 PHP caselib getPairs 一致；type=library、product=0、deleted=0；type=review 仅返回有待审核用例的库（zt_case.reviewedBy 为空） */
    public Map<Integer, String> getCaselibPairs(String type) {
        List<TestSuite> list = testSuiteRepository.findByTypeAndProductAndDeletedOrderByOrderNumAsc("library", 0, 0);
        if ("review".equalsIgnoreCase(type)) {
            List<Integer> libIdsWithUnreviewed = testCaseRepository.findDistinctLibWithUnreviewedCases();
            if (libIdsWithUnreviewed.isEmpty()) return Map.of();
            list = list.stream().filter(s -> libIdsWithUnreviewed.contains(s.getId())).toList();
        }
        return list.stream().collect(Collectors.toMap(TestSuite::getId, s -> s.getName() != null ? s.getName() : "", (a, b) -> a));
    }

    /** 按测试套件 ID 列表返回 id→name，与其它 pairsByList 约定一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> suiteIdList) {
        if (suiteIdList == null || suiteIdList.isEmpty()) return Map.of();
        List<TestSuite> list = testSuiteRepository.findAllById(suiteIdList);
        return list.stream()
                .filter(s -> s.getDeleted() == 0)
                .collect(Collectors.toMap(TestSuite::getId, s -> s.getName() != null ? s.getName() : "", (a, b) -> a));
    }

    /** 全部测试套件列表（未删除），无 product/project 时与 PHP getList 一致 */
    public List<TestSuite> getList() {
        return testSuiteRepository.findByDeletedOrderByOrderNumAsc(0);
    }

    public List<SuiteCase> getCasesBySuite(int suiteId) {
        return suiteCaseRepository.findBySuite(suiteId);
    }

    public TestSuite create(TestSuite suite) {
        suite.setDeleted(0);
        suite.setAddedBy(getCurrentAccount());
        suite.setAddedDate(LocalDateTime.now());
        return testSuiteRepository.save(suite);
    }

    public TestSuite update(TestSuite suite) {
        suite.setLastEditedBy(getCurrentAccount());
        suite.setLastEditedDate(LocalDateTime.now());
        return testSuiteRepository.save(suite);
    }

    @Transactional
    public void addCase(int suiteId, int caseId, int productId) {
        addCase(suiteId, caseId, productId, 1);
    }

    /** 单条添加/更新套件用例（含版本），与 PHP replace 语义一致 */
    @Transactional
    public void addCase(int suiteId, int caseId, int productId, int version) {
        Optional<SuiteCase> existing = suiteCaseRepository.findBySuiteAndCaseId(suiteId, caseId);
        SuiteCase sc = existing.orElseGet(SuiteCase::new);
        sc.setSuite(suiteId);
        sc.setProduct(productId);
        sc.setCaseId(caseId);
        sc.setVersion(version > 0 ? version : 1);
        suiteCaseRepository.save(sc);
    }

    /** 批量关联用例到套件，与 PHP testsuite linkCase(suiteID, cases, versions) 一致 */
    @Transactional
    public void linkCases(int suiteId, int productId, List<Integer> caseIds, Map<Integer, Integer> versions) {
        if (caseIds == null) return;
        for (Integer caseId : caseIds) {
            if (caseId == null || caseId <= 0) continue;
            int ver = (versions != null && versions.containsKey(caseId)) ? versions.get(caseId) : 1;
            addCase(suiteId, caseId, productId, ver);
        }
    }

    @Transactional
    public void removeCase(int suiteId, int caseId) {
        suiteCaseRepository.deleteBySuiteAndCaseId(suiteId, caseId);
    }

    /** 批量移除套件中的用例，与 PHP testsuite batchUnlinkCases 一致 */
    @Transactional
    public void batchUnlinkCases(int suiteId, List<Integer> caseIds) {
        if (caseIds == null) return;
        for (Integer caseId : caseIds) {
            if (caseId != null && caseId > 0) removeCase(suiteId, caseId);
        }
    }

    @Transactional
    public void delete(int suiteId) {
        TestSuite s = getById(suiteId).orElseThrow(() -> new RuntimeException("测试套件不存在"));
        s.setDeleted(1);
        testSuiteRepository.save(s);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
