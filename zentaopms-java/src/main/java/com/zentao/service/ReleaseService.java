package com.zentao.service;

import com.zentao.entity.Release;
import com.zentao.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ActionService actionService;

    public Optional<Release> getById(int id) {
        return releaseRepository.findById(id).filter(r -> r.getDeleted() == 0);
    }

    public List<Release> getByProduct(int productId) {
        return releaseRepository.findByProductAndDeletedOrderByDateDesc(productId, 0);
    }

    public List<Release> getByProject(int projectId) {
        return releaseRepository.findByProjectAndDeleted(projectId);
    }

    public Release create(Release release) {
        release.setDeleted(0);
        return releaseRepository.save(release);
    }

    public Release update(Release release) {
        return releaseRepository.save(release);
    }

    public void delete(int releaseId) {
        releaseRepository.findById(releaseId).ifPresent(r -> {
            r.setDeleted(1);
            releaseRepository.save(r);
        });
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

    private static List<String> idsToList(String raw) {
        if (raw == null || raw.isBlank()) return new ArrayList<>();
        return new ArrayList<>(Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }
}
