package com.zentao.service;

import com.zentao.entity.Dataview;
import com.zentao.repository.DataviewRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataviewService {
    private final DataviewRepository repo;

    public List<Dataview> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Dataview> getById(int id) {
        return repo.findById(id).filter(d -> d.getDeleted() == 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy（当前用户） */
    public Dataview create(Dataview d) {
        d.setDeleted(0);
        d.setCreatedBy(getCurrentAccount());
        return repo.save(d);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public Dataview update(Dataview d) {
        return repo.save(d);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(d -> { d.setDeleted(1); repo.save(d); });
    }
}
