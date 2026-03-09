package com.zentao.service;

import com.zentao.entity.SuiteCase;
import com.zentao.entity.TestSuite;
import com.zentao.repository.SuiteCaseRepository;
import com.zentao.repository.TestSuiteRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final SuiteCaseRepository suiteCaseRepository;

    public Optional<TestSuite> getById(int id) {
        return testSuiteRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<TestSuite> getByProduct(int productId) {
        return testSuiteRepository.findByProductAndDeletedOrderByOrderNumAsc(productId, 0);
    }

    public List<TestSuite> getByProject(int projectId) {
        return testSuiteRepository.findByProjectAndDeletedOrderByOrderNumAsc(projectId, 0);
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
        SuiteCase sc = new SuiteCase();
        sc.setSuite(suiteId);
        sc.setProduct(productId);
        sc.setCaseId(caseId);
        suiteCaseRepository.save(sc);
    }

    @Transactional
    public void removeCase(int suiteId, int caseId) {
        suiteCaseRepository.deleteBySuiteAndCaseId(suiteId, caseId);
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
