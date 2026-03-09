package com.zentao.service;

import com.zentao.entity.Stakeholder;
import com.zentao.repository.StakeholderRepository;
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
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;
    private final ActionService actionService;

    public Optional<Stakeholder> getById(int id) {
        return stakeholderRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<Stakeholder> getByObject(int objectID, String objectType) {
        return stakeholderRepository.findByObjectIDAndObjectTypeAndDeleted(objectID, objectType, 0);
    }

    public Stakeholder create(Stakeholder stakeholder) {
        stakeholder.setDeleted(0);
        stakeholder.setCreatedBy(getCurrentAccount());
        stakeholder.setCreatedDate(LocalDateTime.now());
        Stakeholder saved = stakeholderRepository.save(stakeholder);
        actionService.create("stakeholder", saved.getId(), "added");
        return saved;
    }

    public Stakeholder update(Stakeholder stakeholder) {
        stakeholder.setEditedBy(getCurrentAccount());
        stakeholder.setEditedDate(LocalDateTime.now());
        return stakeholderRepository.save(stakeholder);
    }

    @Transactional
    public void delete(int id) {
        Stakeholder s = getById(id).orElseThrow(() -> new RuntimeException("干系人不存在"));
        s.setDeleted(1);
        stakeholderRepository.save(s);
        actionService.create("stakeholder", id, "deleted");
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
