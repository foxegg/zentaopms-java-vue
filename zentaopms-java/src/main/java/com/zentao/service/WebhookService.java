package com.zentao.service;

import com.zentao.entity.Webhook;
import com.zentao.repository.WebhookRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRepository webhookRepository;

    public Optional<Webhook> getById(int id) {
        return webhookRepository.findById(id).filter(w -> w.getDeleted() == 0);
    }

    public List<Webhook> getList() {
        return webhookRepository.findByDeletedOrderByIdDesc(0);
    }

    public Webhook create(Webhook webhook) {
        webhook.setDeleted(0);
        webhook.setCreatedBy(getCurrentAccount());
        webhook.setCreatedDate(LocalDateTime.now());
        return webhookRepository.save(webhook);
    }

    public Webhook update(Webhook webhook) {
        webhook.setEditedBy(getCurrentAccount());
        webhook.setEditedDate(LocalDateTime.now());
        return webhookRepository.save(webhook);
    }

    public void delete(int webhookId) {
        webhookRepository.findById(webhookId).ifPresent(w -> {
            w.setDeleted(1);
            webhookRepository.save(w);
        });
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
