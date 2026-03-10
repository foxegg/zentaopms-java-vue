package com.zentao.service;

import com.zentao.entity.AiAgent;
import com.zentao.repository.AiAgentRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiAgentService {

    private final AiAgentRepository aiAgentRepository;

    public List<AiAgent> getList() {
        return aiAgentRepository.findByDeletedOrderByIdAsc(0);
    }

    public List<AiAgent> getByModule(String module) {
        return aiAgentRepository.findByModuleAndDeleted(module, 0);
    }

    public Optional<AiAgent> getById(int id) {
        return aiAgentRepository.findByIdAndDeleted(id, 0);
    }

    public AiAgent save(AiAgent a) {
        return aiAgentRepository.save(a);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public AiAgent create(AiAgent a) {
        a.setId(null);
        a.setDeleted(0);
        a.setCreatedBy(getCurrentAccount());
        if (a.getCreatedDate() == null) a.setCreatedDate(LocalDateTime.now());
        if (a.getStatus() == null || a.getStatus().isBlank()) a.setStatus("draft");
        return aiAgentRepository.save(a);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public AiAgent update(AiAgent a) {
        if (a.getEditedDate() == null) a.setEditedDate(LocalDateTime.now());
        return aiAgentRepository.save(a);
    }

    public void delete(int id) {
        aiAgentRepository.findById(id).ifPresent(p -> {
            p.setDeleted(1);
            aiAgentRepository.save(p);
        });
    }

    /** 与 PHP promptpublish/promptunpublish 一致：发布/撤回智能体 */
    public Optional<AiAgent> setStatus(int id, String status) {
        return aiAgentRepository.findById(id).filter(p -> p.getDeleted() == 0).map(p -> {
            p.setStatus(status != null ? status : "draft");
            p.setEditedDate(LocalDateTime.now());
            return aiAgentRepository.save(p);
        });
    }

    public Optional<AiAgent> publish(int id) { return setStatus(id, "active"); }
    public Optional<AiAgent> unpublish(int id) { return setStatus(id, "draft"); }
}
