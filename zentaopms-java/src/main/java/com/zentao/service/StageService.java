package com.zentao.service;

import com.zentao.entity.Stage;
import com.zentao.repository.StageRepository;
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
public class StageService {

    private final StageRepository stageRepository;
    private final ActionService actionService;

    public Optional<Stage> getById(int id) {
        return stageRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<Stage> getByWorkflowGroup(int groupId) {
        return stageRepository.findByWorkflowGroupAndDeletedOrderByOrderNumAsc(groupId, 0);
    }

    public List<Stage> getAll() {
        return stageRepository.findByDeletedOrderByOrderNumAsc(0);
    }

    public Stage create(Stage stage) {
        stage.setDeleted(0);
        stage.setCreatedBy(getCurrentAccount());
        stage.setCreatedDate(LocalDateTime.now());
        Stage saved = stageRepository.save(stage);
        actionService.create("stage", saved.getId(), "Opened");
        return saved;
    }

    public Stage update(Stage stage) {
        stage.setEditedBy(getCurrentAccount());
        stage.setEditedDate(LocalDateTime.now());
        return stageRepository.save(stage);
    }

    @Transactional
    public void delete(int id) {
        Stage s = getById(id).orElseThrow(() -> new RuntimeException("阶段不存在"));
        s.setDeleted(1);
        stageRepository.save(s);
        actionService.create("stage", id, "deleted");
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
