package com.zentao.service;

import com.zentao.entity.Kanban;
import com.zentao.entity.KanbanCard;
import com.zentao.entity.KanbanSpace;
import com.zentao.repository.KanbanCardRepository;
import com.zentao.repository.KanbanRepository;
import com.zentao.repository.KanbanSpaceRepository;
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
public class KanbanService {

    private final KanbanSpaceRepository spaceRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanCardRepository cardRepository;

    public Optional<KanbanSpace> getSpaceById(int id) {
        return spaceRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public List<KanbanSpace> getSpaceList() {
        return spaceRepository.findByDeletedOrderByOrderNumAsc(0);
    }

    public KanbanSpace createSpace(KanbanSpace space) {
        space.setDeleted(0);
        space.setStatus("active");
        space.setCreatedBy(getCurrentAccount());
        space.setCreatedDate(LocalDateTime.now());
        return spaceRepository.save(space);
    }

    public KanbanSpace updateSpace(KanbanSpace space) {
        space.setLastEditedBy(getCurrentAccount());
        space.setLastEditedDate(LocalDateTime.now());
        return spaceRepository.save(space);
    }

    @Transactional
    public void deleteSpace(int id) {
        KanbanSpace s = getSpaceById(id).orElseThrow(() -> new RuntimeException("看板空间不存在"));
        s.setDeleted(1);
        spaceRepository.save(s);
    }

    public Optional<Kanban> getKanbanById(int id) {
        return kanbanRepository.findById(id).filter(k -> k.getDeleted() == 0);
    }

    public List<Kanban> getKanbansBySpace(int spaceId) {
        return kanbanRepository.findBySpaceAndDeletedOrderByOrderNumAsc(spaceId, 0);
    }

    public Kanban createKanban(Kanban kanban) {
        kanban.setDeleted(0);
        kanban.setStatus("active");
        kanban.setCreatedBy(getCurrentAccount());
        kanban.setCreatedDate(LocalDateTime.now());
        return kanbanRepository.save(kanban);
    }

    public Kanban updateKanban(Kanban kanban) {
        kanban.setLastEditedBy(getCurrentAccount());
        kanban.setLastEditedDate(LocalDateTime.now());
        return kanbanRepository.save(kanban);
    }

    @Transactional
    public void deleteKanban(int id) {
        Kanban k = getKanbanById(id).orElseThrow(() -> new RuntimeException("看板不存在"));
        k.setDeleted(1);
        kanbanRepository.save(k);
    }

    public Optional<KanbanCard> getCardById(int id) {
        return cardRepository.findById(id).filter(c -> c.getDeleted() == 0);
    }

    public List<KanbanCard> getCardsByKanban(int kanbanId) {
        return cardRepository.findByKanbanAndDeletedOrderByOrderNumAsc(kanbanId, 0);
    }

    public KanbanCard createCard(KanbanCard card) {
        card.setDeleted(0);
        card.setCreatedBy(getCurrentAccount());
        card.setCreatedDate(LocalDateTime.now());
        return cardRepository.save(card);
    }

    public KanbanCard updateCard(KanbanCard card) {
        card.setLastEditedBy(getCurrentAccount());
        card.setLastEditedDate(LocalDateTime.now());
        return cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(int id) {
        KanbanCard c = getCardById(id).orElseThrow(() -> new RuntimeException("卡片不存在"));
        c.setDeleted(1);
        cardRepository.save(c);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
