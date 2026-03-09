package com.zentao.service;

import com.zentao.entity.Build;
import com.zentao.repository.BuildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildService {

    private final BuildRepository buildRepository;
    private final ActionService actionService;

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

    public Build create(Build build) {
        build.setDeleted(0);
        return buildRepository.save(build);
    }

    public Build update(Build build) {
        return buildRepository.save(build);
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

    private static List<String> idsToList(String raw) {
        if (raw == null || raw.isBlank()) return new java.util.ArrayList<>();
        return new ArrayList<>(Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }
}
