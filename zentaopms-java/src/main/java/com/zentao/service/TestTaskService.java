package com.zentao.service;

import com.zentao.entity.TestRun;
import com.zentao.entity.TestTask;
import com.zentao.repository.TestRunRepository;
import com.zentao.repository.TestTaskRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTaskService {

    private final TestTaskRepository testTaskRepository;
    private final TestRunRepository testRunRepository;
    private final ActionService actionService;

    public Optional<TestTask> getById(int id) {
        return testTaskRepository.findById(id).filter(t -> t.getDeleted() == 0);
    }

    public List<TestTask> getByProduct(int productId) {
        return testTaskRepository.findByProductAndDeletedOrderByIdDesc(productId, 0);
    }

    public List<TestTask> getByProject(int projectId) {
        return testTaskRepository.findByProjectAndDeletedOrderByIdDesc(projectId, 0);
    }

    public List<TestTask> getByExecution(int executionId) {
        return testTaskRepository.findByExecutionAndDeletedOrderByIdDesc(executionId, 0);
    }

    /** 测试单 id→name 下拉用，与 PHP testtask getPairs(productID, executionID) 一致；product≤0 返回空；execution>0 时仅该执行下且属该产品 */
    public Map<Integer, String> getPairs(int productId, int executionId) {
        if (productId <= 0) return Map.of();
        List<TestTask> list = executionId > 0
                ? getByExecution(executionId).stream().filter(t -> productId == t.getProduct()).toList()
                : getByProduct(productId);
        list = list.stream().filter(t -> !"unit".equals(t.getAuto())).toList();
        return list.stream().collect(Collectors.toMap(TestTask::getId, t -> t.getName() != null ? t.getName() : "", (a, b) -> a));
    }

    /** 按测试单 ID 列表返回 id→name，与 PHP testtask getPairsByList(taskIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> taskIdList) {
        if (taskIdList == null || taskIdList.isEmpty()) return Map.of();
        List<TestTask> list = testTaskRepository.findAllById(taskIdList);
        return list.stream()
                .filter(t -> t.getDeleted() == 0)
                .collect(Collectors.toMap(TestTask::getId, t -> t.getName() != null ? t.getName() : "", (a, b) -> a));
    }

    /** 全部测试单列表（未删除），无 product/project/execution 时与 PHP getList 一致 */
    public List<TestTask> getList() {
        return testTaskRepository.findByDeletedOrderByIdDesc(0);
    }

    /** 按负责人分页查询测试单，对应 PHP testtask getByUser(owner). */
    public Page<TestTask> getByOwner(String owner, Pageable pageable) {
        if (owner == null || owner.isEmpty()) return org.springframework.data.domain.Page.empty(pageable);
        return testTaskRepository.findByOwnerAndDeletedOrderByIdDesc(owner, 0, pageable);
    }

    public TestTask create(TestTask task) {
        task.setDeleted(0);
        task.setCreatedBy(getCurrentAccount());
        task.setCreatedDate(LocalDateTime.now());
        TestTask saved = testTaskRepository.save(task);
        actionService.create("testtask", saved.getId(), "Opened");
        return saved;
    }

    public TestTask update(TestTask task) {
        return testTaskRepository.save(task);
    }

    @Transactional
    public TestTask start(int taskId) {
        TestTask t = getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        t.setStatus("doing");
        t.setRealBegan(java.time.LocalDate.now());
        TestTask saved = testTaskRepository.save(t);
        actionService.create("testtask", taskId, "Started");
        return saved;
    }

    @Transactional
    public TestTask close(int taskId) {
        TestTask t = getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        t.setStatus("done");
        t.setRealFinishedDate(LocalDateTime.now());
        TestTask saved = testTaskRepository.save(t);
        actionService.create("testtask", taskId, "Closed");
        return saved;
    }

    @Transactional
    public TestTask block(int taskId) {
        TestTask t = getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        t.setStatus("blocked");
        TestTask saved = testTaskRepository.save(t);
        actionService.create("testtask", taskId, "Blocked");
        return saved;
    }

    @Transactional
    public TestTask activate(int taskId) {
        TestTask t = getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        if ("wait".equals(t.getStatus()) || "blocked".equals(t.getStatus()) || "done".equals(t.getStatus())) {
            t.setStatus("doing");
            if (t.getRealBegan() == null) t.setRealBegan(java.time.LocalDate.now());
        }
        TestTask saved = testTaskRepository.save(t);
        actionService.create("testtask", taskId, "Activated");
        return saved;
    }

    @Transactional
    public void delete(int taskId) {
        TestTask t = getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        t.setDeleted(1);
        testTaskRepository.save(t);
        actionService.create("testtask", taskId, "deleted");
    }

    public List<TestRun> getResults(int taskId) {
        return testRunRepository.findByTaskIdOrderByIdAsc(taskId);
    }

    @Transactional
    public TestRun linkCase(int taskId, int caseId, String assignedTo) {
        getById(taskId).orElseThrow(() -> new RuntimeException("测试单不存在"));
        TestRun run = testRunRepository.findByTaskIdAndCaseId(taskId, caseId).orElse(null);
        if (run != null) return run;
        run = new TestRun();
        run.setTaskId(taskId);
        run.setCaseId(caseId);
        run.setAssignedTo(assignedTo != null ? assignedTo : getCurrentAccount());
        run.setStatus("n/a");
        return testRunRepository.save(run);
    }

    @Transactional
    public void unlinkCase(int taskId, int caseId) {
        testRunRepository.deleteByTaskIdAndCaseId(taskId, caseId);
    }

    @Transactional
    public void batchUnlinkCases(int taskId, List<Integer> caseIds) {
        if (caseIds != null && !caseIds.isEmpty()) {
            for (Integer caseId : caseIds) {
                testRunRepository.deleteByTaskIdAndCaseId(taskId, caseId);
            }
        }
    }

    /** 批量指派测试单中的用例，与 PHP testtask batchAssign 一致 */
    @Transactional
    public void batchAssign(int taskId, String assignedTo, List<Integer> caseIds) {
        if (caseIds == null || assignedTo == null) return;
        for (Integer caseId : caseIds) {
            if (caseId == null || caseId <= 0) continue;
            testRunRepository.findByTaskIdAndCaseId(taskId, caseId).ifPresent(run -> {
                run.setAssignedTo(assignedTo);
                testRunRepository.save(run);
                actionService.create("case", caseId, "Assigned", "", assignedTo);
            });
        }
    }

    @Transactional
    public TestRun runCase(int taskId, int caseId, String result) {
        TestRun run = testRunRepository.findByTaskIdAndCaseId(taskId, caseId)
                .orElseThrow(() -> new RuntimeException("未关联该用例"));
        run.setLastRunResult(result != null ? result : "n/a");
        run.setLastRunner(getCurrentAccount());
        run.setLastRunDate(LocalDateTime.now());
        return testRunRepository.save(run);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
