package com.zentao.service;

import com.zentao.entity.Bug;
import com.zentao.repository.BugRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BugService {

    private final BugRepository bugRepository;
    private final ActionService actionService;

    public Optional<Bug> getById(int id) {
        return bugRepository.findById(id).filter(b -> b.getDeleted() == 0);
    }

    public List<Bug> getByProduct(int productId) {
        return bugRepository.findByProductAndDeleted(productId, 0);
    }

    public List<Bug> getByProject(int projectId) {
        return bugRepository.findByProjectAndDeleted(projectId, 0);
    }

    public List<Bug> getByAssignedTo(String account) {
        return bugRepository.findByAssignedToAndDeleted(account, 0);
    }

    public Page<Bug> getList(Specification<Bug> spec, Pageable pageable) {
        return bugRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    public Bug create(Bug bug) {
        bug.setDeleted(0);
        bug.setStatus("active");
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", saved.getId(), "Opened");
        return saved;
    }

    public Bug update(Bug bug) {
        return bugRepository.save(bug);
    }

    @Transactional
    public Bug assignTo(int bugId, String assignedTo) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setAssignedTo(assignedTo);
        bug.setAssignedDate(LocalDateTime.now());
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "Assigned", "", assignedTo);
        return saved;
    }

    @Transactional
    public Bug confirm(int bugId, String comment) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setConfirmed(1);
        bug.setStatus("active");
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "bugConfirmed", comment);
        return saved;
    }

    @Transactional
    public Bug resolve(int bugId, String resolution, String resolvedBuild) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setResolution(resolution);
        bug.setResolvedBuild(resolvedBuild != null ? resolvedBuild : "");
        bug.setResolvedBy(getCurrentAccount());
        bug.setResolvedDate(LocalDateTime.now());
        bug.setStatus("resolved");
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "Resolved");
        return saved;
    }

    @Transactional
    public Bug activate(int bugId, String openedBuild) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setStatus("active");
        bug.setActivatedCount((bug.getActivatedCount() != null ? bug.getActivatedCount() : 0) + 1);
        bug.setActivatedDate(LocalDateTime.now());
        if (openedBuild != null) bug.setOpenedBuild(openedBuild);
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "Activated");
        return saved;
    }

    @Transactional
    public Bug close(int bugId, String comment) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setStatus("closed");
        bug.setClosedBy(getCurrentAccount());
        bug.setClosedDate(LocalDateTime.now());
        bug.setAssignedTo("closed");
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "Closed", comment);
        return saved;
    }

    @Transactional
    public void delete(int bugId) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setDeleted(1);
        bugRepository.save(bug);
        actionService.create("bug", bugId, "deleted");
    }

    @Transactional
    public void batchAssignTo(List<Integer> bugIds, String assignedTo) {
        for (Integer id : bugIds) {
            assignTo(id, assignedTo);
        }
    }

    @Transactional
    public void batchResolve(List<Integer> bugIds, String resolution, String resolvedBuild) {
        for (Integer id : bugIds) {
            resolve(id, resolution, resolvedBuild);
        }
    }

    @Transactional
    public void batchClose(List<Integer> bugIds) {
        for (Integer id : bugIds) {
            close(id, null);
        }
    }

    @Transactional
    public void batchChangeModule(List<Integer> bugIds, int moduleId) {
        for (Integer id : bugIds) {
            Bug b = getById(id).orElse(null);
            if (b != null) {
                b.setModule(moduleId);
                bugRepository.save(b);
            }
        }
    }

    @Transactional
    public void batchChangePlan(List<Integer> bugIds, int planId) {
        for (Integer id : bugIds) {
            Bug b = getById(id).orElse(null);
            if (b != null) {
                b.setPlan(planId);
                bugRepository.save(b);
            }
        }
    }

    @Transactional
    public void batchChangeBranch(List<Integer> bugIds, int branchId) {
        for (Integer id : bugIds) {
            Bug b = getById(id).orElse(null);
            if (b != null) {
                b.setBranch(branchId);
                bugRepository.save(b);
            }
        }
    }

    @Transactional
    public void batchConfirm(List<Integer> bugIds) {
        for (Integer id : bugIds) {
            confirm(id, null);
        }
    }

    @Transactional
    public void batchActivate(List<Integer> bugIds, String openedBuild) {
        for (Integer id : bugIds) {
            activate(id, openedBuild);
        }
    }

    @Transactional
    public List<Integer> batchCreate(List<Bug> bugs) {
        if (bugs == null || bugs.isEmpty()) return List.of();
        List<Integer> ids = new java.util.ArrayList<>();
        for (Bug bug : bugs) {
            Bug created = create(bug);
            ids.add(created.getId());
        }
        return ids;
    }

    @Transactional
    public void batchEdit(List<Integer> bugIds, java.util.Map<String, Object> fields) {
        if (bugIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : bugIds) {
            Bug bug = getById(id).orElse(null);
            if (bug == null) continue;
            if (fields.containsKey("product")) bug.setProduct(fields.get("product") instanceof Number n ? n.intValue() : bug.getProduct());
            if (fields.containsKey("module")) bug.setModule(fields.get("module") instanceof Number n ? n.intValue() : bug.getModule());
            if (fields.containsKey("plan")) bug.setPlan(fields.get("plan") instanceof Number n ? n.intValue() : bug.getPlan());
            if (fields.containsKey("story")) bug.setStory(fields.get("story") instanceof Number n ? n.intValue() : bug.getStory());
            if (fields.containsKey("assignedTo")) bug.setAssignedTo(fields.get("assignedTo") != null ? fields.get("assignedTo").toString() : bug.getAssignedTo());
            if (fields.containsKey("severity")) bug.setSeverity(fields.get("severity") instanceof Number n ? n.intValue() : bug.getSeverity());
            if (fields.containsKey("pri")) bug.setPri(fields.get("pri") instanceof Number n ? n.intValue() : bug.getPri());
            if (fields.containsKey("type")) bug.setType(fields.get("type") != null ? fields.get("type").toString() : bug.getType());
            bugRepository.save(bug);
            actionService.create("bug", id, "Edited");
        }
    }

    @Transactional
    public Bug setStory(int bugId, int storyId) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setStory(storyId);
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "linked2story", "", String.valueOf(storyId));
        return saved;
    }

    @Transactional
    public Bug confirmStoryChange(int bugId, int storyId) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        bug.setStory(storyId);
        bug.setStoryVersion((bug.getStoryVersion() != null ? bug.getStoryVersion() : 1) + 1);
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "confirmStoryChange", "", String.valueOf(storyId));
        return saved;
    }

    @Transactional
    public Bug linkBugs(int bugId, List<Integer> relatedBugIds) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        List<String> ids = idsFromRelatedBug(bug.getRelatedBug());
        for (Integer id : relatedBugIds) {
            if (id != null && id > 0 && id != bugId) {
                String sid = String.valueOf(id);
                if (!ids.contains(sid)) ids.add(sid);
            }
        }
        bug.setRelatedBug(String.join(",", ids));
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "linkBugs", "", String.join(",", ids));
        return saved;
    }

    @Transactional
    public Bug unlinkBug(int bugId, int relatedBugId) {
        Bug bug = getById(bugId).orElseThrow(() -> new RuntimeException("Bug不存在"));
        List<String> ids = idsFromRelatedBug(bug.getRelatedBug());
        ids.remove(String.valueOf(relatedBugId));
        bug.setRelatedBug(String.join(",", ids));
        Bug saved = bugRepository.save(bug);
        actionService.create("bug", bugId, "unlinkBug", "", String.valueOf(relatedBugId));
        return saved;
    }

    private static List<String> idsFromRelatedBug(String raw) {
        if (raw == null || raw.isBlank()) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>(java.util.Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }

    /** Bug 报表摘要（按产品/项目统计各状态数量） */
    public java.util.Map<String, Object> getReport(Integer productId, Integer projectId) {
        List<Bug> list = projectId != null && projectId > 0 ? getByProject(projectId)
                : productId != null && productId > 0 ? getByProduct(productId) : List.of();
        long active = list.stream().filter(b -> "active".equals(b.getStatus())).count();
        long resolved = list.stream().filter(b -> "resolved".equals(b.getStatus())).count();
        long closed = list.stream().filter(b -> "closed".equals(b.getStatus())).count();
        return java.util.Map.of("total", list.size(), "active", active, "resolved", resolved, "closed", closed);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
