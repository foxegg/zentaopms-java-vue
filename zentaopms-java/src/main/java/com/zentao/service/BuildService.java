package com.zentao.service;

import com.zentao.entity.Bug;
import com.zentao.entity.Build;
import com.zentao.entity.Story;
import com.zentao.repository.BugRepository;
import com.zentao.repository.BuildRepository;
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
public class BuildService {

    private final BuildRepository buildRepository;
    private final ActionService actionService;
    private final StoryRepository storyRepository;
    private final BugRepository bugRepository;

    public Optional<Build> getById(int id) {
        return buildRepository.findById(id).filter(b -> b.getDeleted() == 0);
    }

    public List<Build> getByProject(int projectId) {
        return buildRepository.findByProjectAndDeleted(projectId, 0);
    }

    public List<Build> getByExecution(int executionId) {
        return buildRepository.findByExecutionAndDeleted(executionId, 0);
    }

    public List<Build> getByProduct(int productId) {
        return buildRepository.findByProductAndDeleted(productId, 0);
    }

    /** 全部构建列表（未删除），无 project/product/execution 时与 PHP getList 一致 */
    public List<Build> getList() {
        return buildRepository.findByDeletedOrderByIdDesc(0);
    }

    /** 构建 id->name 下拉用，与 PHP build getBuildPairs 一致；product 为 0 时返回空 */
    public Map<Integer, String> getPairs(int productId) {
        if (productId <= 0) return Map.of();
        List<Build> list = getByProduct(productId);
        return list.stream().collect(Collectors.toMap(Build::getId, b -> b.getName() != null ? b.getName() : "", (a, b) -> a));
    }

    /** 按构建 ID 列表返回 id→name，与 PHP build getBuildPairs(..., buildIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> buildIdList) {
        if (buildIdList == null || buildIdList.isEmpty()) return Map.of();
        List<Build> list = buildRepository.findAllById(buildIdList);
        return list.stream()
                .filter(b -> b.getDeleted() == 0)
                .collect(Collectors.toMap(Build::getId, b -> b.getName() != null ? b.getName() : "", (a, b) -> a));
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public Build create(Build build) {
        build.setDeleted(0);
        build.setCreatedBy(getCurrentAccount());
        build.setCreatedDate(LocalDateTime.now());
        return buildRepository.save(build);
    }

    public Build update(Build build) {
        return buildRepository.save(build);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public void delete(int buildId) {
        buildRepository.findById(buildId).ifPresent(b -> {
            b.setDeleted(1);
            buildRepository.save(b);
        });
    }

    @Transactional
    public boolean linkStory(int buildID, List<Integer> storyIds) {
        if (storyIds == null || storyIds.isEmpty()) return false;
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> existing = idsToList(build.getStories());
        List<Integer> toAdd = storyIds.stream().filter(id -> id != null && id > 0 && !existing.contains(String.valueOf(id))).distinct().toList();
        if (toAdd.isEmpty()) return true;
        existing.addAll(toAdd.stream().map(String::valueOf).toList());
        build.setStories(String.join(",", existing));
        buildRepository.save(build);
        for (Integer sid : toAdd) {
            actionService.create("story", sid, "linked2build", "", String.valueOf(buildID));
        }
        actionService.create("build", buildID, "linkstory", "", toAdd.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return true;
    }

    @Transactional
    public boolean unlinkStory(int buildID, int storyID) {
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> list = idsToList(build.getStories());
        list.remove(String.valueOf(storyID));
        build.setStories(String.join(",", list));
        buildRepository.save(build);
        actionService.create("story", storyID, "unlinkedfrombuild", "", String.valueOf(buildID));
        return true;
    }

    @Transactional
    public boolean batchUnlinkStory(int buildID, List<Integer> storyIds) {
        if (storyIds == null || storyIds.isEmpty()) return true;
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> list = idsToList(build.getStories());
        for (Integer id : storyIds) {
            if (id != null) list.remove(String.valueOf(id));
        }
        build.setStories(String.join(",", list));
        buildRepository.save(build);
        for (Integer sid : storyIds) {
            if (sid != null && sid > 0) actionService.create("story", sid, "unlinkedfrombuild", "", String.valueOf(buildID));
        }
        return true;
    }

    @Transactional
    public boolean linkBug(int buildID, List<Integer> bugIds) {
        if (bugIds == null || bugIds.isEmpty()) return false;
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> existing = idsToList(build.getBugs());
        List<Integer> toAdd = bugIds.stream().filter(id -> id != null && id > 0 && !existing.contains(String.valueOf(id))).distinct().toList();
        if (toAdd.isEmpty()) return true;
        existing.addAll(toAdd.stream().map(String::valueOf).toList());
        build.setBugs(String.join(",", existing));
        buildRepository.save(build);
        for (Integer bid : toAdd) {
            actionService.create("bug", bid, "linked2bug", "", String.valueOf(buildID));
        }
        return true;
    }

    @Transactional
    public boolean unlinkBug(int buildID, int bugID) {
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> list = idsToList(build.getBugs());
        list.remove(String.valueOf(bugID));
        build.setBugs(String.join(",", list));
        buildRepository.save(build);
        actionService.create("bug", bugID, "unlinkedfrombuild", "", String.valueOf(buildID));
        return true;
    }

    @Transactional
    public boolean batchUnlinkBug(int buildID, List<Integer> bugIds) {
        if (bugIds == null || bugIds.isEmpty()) return true;
        Build build = getById(buildID).orElse(null);
        if (build == null) return false;
        List<String> list = idsToList(build.getBugs());
        for (Integer id : bugIds) {
            if (id != null) list.remove(String.valueOf(id));
        }
        build.setBugs(String.join(",", list));
        buildRepository.save(build);
        for (Integer bid : bugIds) {
            if (bid != null && bid > 0) actionService.create("bug", bid, "unlinkedfrombuild", "", String.valueOf(buildID));
        }
        return true;
    }

    /** 获取构建关联的需求列表，与 PHP build 查看需求一致 */
    public List<Story> getStoriesByBuild(int buildId) {
        Build b = getById(buildId).orElse(null);
        if (b == null || b.getStories() == null || b.getStories().isBlank()) return List.of();
        List<Integer> ids = idsToList(b.getStories()).stream().filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        return ids.isEmpty() ? List.of() : storyRepository.findByIdIn(ids);
    }

    /** 获取构建关联的缺陷列表，与 PHP build 查看缺陷一致 */
    public List<Bug> getBugsByBuild(int buildId) {
        Build b = getById(buildId).orElse(null);
        if (b == null || b.getBugs() == null || b.getBugs().isBlank()) return List.of();
        List<Integer> ids = idsToList(b.getBugs()).stream().filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        return ids.isEmpty() ? List.of() : bugRepository.findAllById(ids);
    }

    private static List<String> idsToList(String raw) {
        if (raw == null || raw.isBlank()) return new java.util.ArrayList<>();
        return new ArrayList<>(Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }
}
