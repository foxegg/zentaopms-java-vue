package com.zentao.service;

import com.zentao.entity.Design;
import com.zentao.repository.DesignRepository;
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
public class DesignService {
    private final DesignRepository repo;
    private final ActionService actionService;

    public List<Design> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Design> getById(int id) {
        return repo.findById(id).filter(d -> d.getDeleted() != null && d.getDeleted() == 0);
    }

    /** 创建设计，与 PHP design create 一致：设置 createdBy/createdDate，保存后记录 action 'created' */
    @Transactional
    public Design create(Design e) {
        e.setId(null);
        e.setDeleted(0);
        e.setCreatedBy(getCurrentAccount());
        e.setCreatedDate(LocalDateTime.now());
        Design saved = repo.save(e);
        actionService.create("design", saved.getId(), "created");
        return saved;
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    @Transactional
    public void update(Design e) {
        repo.findById(e.getId()).ifPresent(x -> repo.save(e));
    }

    /** 删除设计（软删除），与 PHP design delete 一致：置 deleted=1 并记录 action 'deleted' */
    @Transactional
    public void delete(int id) {
        repo.findById(id).ifPresent(d -> {
            d.setDeleted(1);
            repo.save(d);
            actionService.create("design", id, "deleted", "", "1");
        });
    }
}
