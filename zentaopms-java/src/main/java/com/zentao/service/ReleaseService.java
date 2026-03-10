package com.zentao.service;

import com.zentao.entity.Bug;
import com.zentao.entity.Release;
import com.zentao.entity.Story;
import com.zentao.repository.BugRepository;
import com.zentao.repository.BuildRepository;
import com.zentao.repository.ReleaseRepository;
import com.zentao.repository.StoryRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final BuildRepository buildRepository;
    private final ActionService actionService;
    private final StoryRepository storyRepository;
    private final BugRepository bugRepository;

    public Optional<Release> getById(int id) {
        return releaseRepository.findById(id).filter(r -> r.getDeleted() == 0);
    }

    public List<Release> getList() {
        return releaseRepository.findByDeletedOrderByDateDesc(0);
    }

    public List<Release> getByProduct(int productId) {
        return getByProduct(productId, "all", "all");
    }

    /** 按产品、分支、类型筛选发布，与 PHP release getList(productID, branch, type) 一致；branch 为 all 或分支 ID，type 为 all/normal/terminated */
    public List<Release> getByProduct(int productId, String branch, String type) {
        List<Release> list = releaseRepository.findByProductAndDeletedOrderByDateDesc(productId, 0);
        if (branch != null && !branch.isEmpty() && !"all".equalsIgnoreCase(branch)) {
            list = list.stream().filter(r -> {
                String b = r.getBranch() != null ? r.getBranch().trim() : "0";
                if (b.isEmpty()) b = "0";
                for (String s : b.split(",")) {
                    if (s.trim().equals(branch)) return true;
                }
                return false;
            }).toList();
        }
        if (type != null && !type.isEmpty() && !"all".equalsIgnoreCase(type)) {
            list = list.stream().filter(r -> type.equalsIgnoreCase(r.getStatus())).toList();
        }
        return list;
    }

    public List<Release> getByProject(int projectId) {
        return releaseRepository.findByProjectAndDeleted(projectId);
    }

    /** 发布 id->name 下拉用，与 PHP release getPairs 一致；productID 为 0 时返回空 */
    public Map<Integer, String> getPairs(int productId) {
        if (productId <= 0) return Map.of();
        List<Release> list = getByProduct(productId);
        return list.stream().collect(Collectors.toMap(Release::getId, r -> r.getName() != null ? r.getName() : "", (a, b) -> a));
    }

    /** 按发布 ID 列表返回 id→name，与 PHP release getPairs(idList) 一致；空列表时返回全部未删除发布 */
    public Map<Integer, String> getPairsByList(List<Integer> idList) {
        List<Release> list = (idList == null || idList.isEmpty())
                ? getList()
                : releaseRepository.findAllById(idList).stream().filter(r -> r.getDeleted() == 0).toList();
        return list.stream().collect(Collectors.toMap(Release::getId, r -> r.getName() != null ? r.getName() : "", (a, b) -> a));
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public Release create(Release release) {
        release.setDeleted(0);
        release.setCreatedBy(getCurrentAccount());
        release.setCreatedDate(LocalDateTime.now());
        return releaseRepository.save(release);
    }

    public Release update(Release release) {
        return releaseRepository.save(release);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    /** 与 PHP release delete 一致：软删除发布；若有 shadow 则软删除 shadow build；删除与发布同 createdDate 且无 execution 的 build（PHP 为硬删 build，此处用软删以保持数据可追溯） */
    @Transactional
    public void delete(int releaseId) {
        Release r = releaseRepository.findById(releaseId).orElse(null);
        if (r == null) return;
        if (r.getShadow() != null && r.getShadow() > 0) {
            buildRepository.findById(r.getShadow()).ifPresent(b -> {
                b.setDeleted(1);
                buildRepository.save(b);
            });
        }
        List<Integer> buildIds = idsToIntList(r.getBuild());
        for (Integer bid : buildIds) {
            if (bid == null || bid <= 0) continue;
            buildRepository.findById(bid).ifPresent(b -> {
                boolean noExecution = b.getExecution() == null || b.getExecution() == 0;
                boolean sameCreatedDate = java.util.Objects.equals(b.getCreatedDate(), r.getCreatedDate());
                if (noExecution && sameCreatedDate) {
                    b.setDeleted(1);
                    buildRepository.save(b);
                }
            });
        }
        r.setDeleted(1);
        releaseRepository.save(r);
    }

    @Transactional
    public boolean linkStory(int releaseID, List<Integer> storyIds) {
        if (storyIds == null || storyIds.isEmpty()) return false;
        Release release = getById(releaseID).orElse(null);
        if (release == null) return false;
        List<String> existing = idsToList(release.getStories());
        List<Integer> toAdd = storyIds.stream().filter(id -> id != null && id > 0 && !existing.contains(String.valueOf(id))).distinct().toList();
        if (toAdd.isEmpty()) return true;
        existing.addAll(toAdd.stream().map(String::valueOf).toList());
        release.setStories(String.join(",", existing));
        releaseRepository.save(release);
        for (Integer sid : toAdd) {
            actionService.create("story", sid, "linked2release", "", String.valueOf(releaseID));
        }
        return true;
    }

    @Transactional
    public boolean unlinkStory(int releaseID, int storyID) {
        Release release = getById(releaseID).orElse(null);
        if (release == null) return false;
        List<String> list = idsToList(release.getStories());
        list.remove(String.valueOf(storyID));
        release.setStories(String.join(",", list));
        releaseRepository.save(release);
        actionService.create("story", storyID, "unlinkedfromrelease", "", String.valueOf(releaseID));
        return true;
    }

    @Transactional
    public boolean linkBug(int releaseID, List<Integer> bugIds) {
        if (bugIds == null || bugIds.isEmpty()) return false;
        Release release = getById(releaseID).orElse(null);
        if (release == null) return false;
        List<String> existing = idsToList(release.getBugs());
        List<Integer> toAdd = bugIds.stream().filter(id -> id != null && id > 0 && !existing.contains(String.valueOf(id))).distinct().toList();
        if (toAdd.isEmpty()) return true;
        existing.addAll(toAdd.stream().map(String::valueOf).toList());
        release.setBugs(String.join(",", existing));
        releaseRepository.save(release);
        for (Integer bid : toAdd) {
            actionService.create("bug", bid, "linked2release", "", String.valueOf(releaseID));
        }
        return true;
    }

    @Transactional
    public boolean unlinkBug(int releaseID, int bugID) {
        Release release = getById(releaseID).orElse(null);
        if (release == null) return false;
        List<String> list = idsToList(release.getBugs());
        list.remove(String.valueOf(bugID));
        release.setBugs(String.join(",", list));
        releaseRepository.save(release);
        actionService.create("bug", bugID, "unlinkedfromrelease", "", String.valueOf(releaseID));
        return true;
    }

    @Transactional
    public boolean changeStatus(int releaseID, String status) {
        Release release = getById(releaseID).orElse(null);
        if (release == null) return false;
        release.setStatus(status != null ? status : release.getStatus());
        releaseRepository.save(release);
        return true;
    }

    @Transactional
    public boolean batchUnlinkStory(int releaseID, List<Integer> storyIds) {
        if (storyIds == null) return true;
        for (Integer sid : storyIds) {
            unlinkStory(releaseID, sid);
        }
        return true;
    }

    @Transactional
    public boolean batchUnlinkBug(int releaseID, List<Integer> bugIds) {
        if (bugIds == null) return true;
        for (Integer bid : bugIds) {
            unlinkBug(releaseID, bid);
        }
        return true;
    }

    @Transactional
    public boolean publish(int releaseID) {
        return changeStatus(releaseID, "released");
    }

    /** 获取发布关联的需求列表，与 PHP release 查看需求一致 */
    public List<Story> getStoriesByRelease(int releaseId) {
        Release r = getById(releaseId).orElse(null);
        if (r == null || r.getStories() == null || r.getStories().isBlank()) return List.of();
        List<Integer> ids = idsToList(r.getStories()).stream().filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        return ids.isEmpty() ? List.of() : storyRepository.findByIdIn(ids);
    }

    /** 获取发布关联的缺陷列表，与 PHP release 查看缺陷一致 */
    public List<Bug> getBugsByRelease(int releaseId) {
        Release r = getById(releaseId).orElse(null);
        if (r == null || r.getBugs() == null || r.getBugs().isBlank()) return List.of();
        List<Integer> ids = idsToList(r.getBugs()).stream().filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        return ids.isEmpty() ? List.of() : bugRepository.findAllById(ids);
    }

    private static List<String> idsToList(String raw) {
        if (raw == null || raw.isBlank()) return new ArrayList<>();
        return new ArrayList<>(Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }

    private static List<Integer> idsToIntList(String raw) {
        List<String> list = idsToList(raw);
        List<Integer> out = new ArrayList<>();
        for (String s : list) {
            if (s.matches("\\d+")) out.add(Integer.parseInt(s));
        }
        return out;
    }
}
