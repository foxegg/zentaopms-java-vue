package com.zentao.service;

import com.zentao.entity.Team;
import com.zentao.repository.TeamRepository;
import com.zentao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目/执行团队成员 - 对应 zt_team
 */
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final ExecutionService executionService;

    /** 项目团队：root=projectID, type=project */
    public List<Map<String, Object>> getByProject(int projectID) {
        if (projectService.getById(projectID).isEmpty()) return List.of();
        List<Team> list = teamRepository.findByRootAndTypeOrderByOrderNumAsc(projectID, "project");
        return toMemberList(list);
    }

    /** 执行团队：root=executionID, type=execution */
    public List<Map<String, Object>> getByExecution(int executionID) {
        if (executionService.getById(executionID).isEmpty()) return List.of();
        List<Team> list = teamRepository.findByRootAndTypeOrderByOrderNumAsc(executionID, "execution");
        return toMemberList(list);
    }

    private List<Map<String, Object>> toMemberList(List<Team> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Team t : list) {
            Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", t.getId());
            m.put("root", t.getRoot());
            m.put("type", t.getType());
            m.put("account", t.getAccount());
            m.put("role", t.getRole());
            m.put("position", t.getPosition());
            m.put("days", t.getDays());
            m.put("hours", t.getHours());
            m.put("join", t.getJoinDate() != null ? t.getJoinDate().toString() : null);
            m.put("estimate", t.getEstimate());
            m.put("consumed", t.getConsumed());
            m.put("left", t.getLeftHours());
            userRepository.findByAccountAndDeleted(t.getAccount(), 0).ifPresent(u -> {
                m.put("userID", u.getId());
                m.put("realname", u.getRealname());
            });
            result.add(m);
        }
        return result;
    }

    @Transactional
    public void manageMembersProject(int projectID, List<Map<String, Object>> members) {
        if (projectService.getById(projectID).isEmpty()) return;
        teamRepository.deleteByRootAndType(projectID, "project");
        if (members != null) {
            int order = 0;
            for (Map<String, Object> m : members) {
                String account = m.get("account") != null ? m.get("account").toString().trim() : "";
                if (account.isEmpty()) continue;
                Team t = new Team();
                t.setRoot(projectID);
                t.setType("project");
                t.setAccount(account);
                t.setRole(getStr(m, "role"));
                t.setDays(getInt(m, "days"));
                t.setHours(getDecimal(m, "hours"));
                t.setJoinDate(LocalDate.now());
                t.setOrderNum(order++);
                teamRepository.save(t);
            }
        }
    }

    @Transactional
    public void manageMembersExecution(int executionID, List<Map<String, Object>> members) {
        if (executionService.getById(executionID).isEmpty()) return;
        teamRepository.deleteByRootAndType(executionID, "execution");
        if (members != null) {
            int order = 0;
            for (Map<String, Object> m : members) {
                String account = m.get("account") != null ? m.get("account").toString().trim() : "";
                if (account.isEmpty()) continue;
                Team t = new Team();
                t.setRoot(executionID);
                t.setType("execution");
                t.setAccount(account);
                t.setRole(getStr(m, "role"));
                t.setDays(getInt(m, "days"));
                t.setHours(getDecimal(m, "hours"));
                t.setJoinDate(LocalDate.now());
                t.setOrderNum(order++);
                teamRepository.save(t);
            }
        }
    }

    private static String getStr(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : "";
    }

    private static int getInt(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number n) return n.intValue();
        if (v != null) try { return Integer.parseInt(v.toString()); } catch (NumberFormatException ignored) {}
        return 0;
    }

    private static BigDecimal getDecimal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof BigDecimal d) return d;
        if (v instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        if (v != null) try { return new BigDecimal(v.toString()); } catch (NumberFormatException ignored) {}
        return BigDecimal.ZERO;
    }
}
