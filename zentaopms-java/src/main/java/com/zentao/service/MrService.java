package com.zentao.service;

import com.zentao.entity.Mr;
import com.zentao.repository.MrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MrService {
    private final MrRepository repo;

    public List<Mr> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Mr> getById(int id) {
        return repo.findById(id).filter(m -> m.getDeleted() != null && m.getDeleted() == 0);
    }

    @Transactional
    public Mr create(Mr e) {
        e.setId(null);
        e.setDeleted(0);
        return repo.save(e);
    }

    @Transactional
    public void update(Mr e) {
        repo.findById(e.getId()).ifPresent(x -> repo.save(e));
    }

    @Transactional
    public void delete(int id) {
        repo.findById(id).ifPresent(m -> {
            m.setDeleted(1);
            repo.save(m);
        });
    }
}
