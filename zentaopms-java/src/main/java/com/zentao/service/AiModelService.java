package com.zentao.service;

import com.zentao.entity.AiModel;
import com.zentao.repository.AiModelRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiModelService {

    private final AiModelRepository aiModelRepository;

    public List<AiModel> getList() {
        return aiModelRepository.findByDeletedOrderByIdDesc(0);
    }

    public Optional<AiModel> getById(int id) {
        return aiModelRepository.findByIdAndDeleted(id, 0);
    }

    public AiModel save(AiModel m) {
        return aiModelRepository.save(m);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public AiModel create(AiModel m) {
        m.setId(null);
        m.setDeleted(0);
        m.setCreatedBy(getCurrentAccount());
        if (m.getCreatedDate() == null) m.setCreatedDate(LocalDateTime.now());
        return aiModelRepository.save(m);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public AiModel update(AiModel m) {
        if (m.getEditedDate() == null) m.setEditedDate(LocalDateTime.now());
        return aiModelRepository.save(m);
    }

    public void delete(int id) {
        aiModelRepository.findById(id).ifPresent(m -> {
            m.setDeleted(1);
            aiModelRepository.save(m);
        });
    }

    /** 与 PHP modelenable/modeldisable 一致：启用/禁用模型 */
    public Optional<AiModel> setEnabled(int id, boolean enabled) {
        return getById(id).map(m -> {
            m.setEnabled(enabled ? 1 : 0);
            m.setEditedDate(LocalDateTime.now());
            return aiModelRepository.save(m);
        });
    }

    public Optional<AiModel> enable(int id) { return setEnabled(id, true); }
    public Optional<AiModel> disable(int id) { return setEnabled(id, false); }
}
