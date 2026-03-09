package com.zentao.service;

import com.zentao.entity.Design;
import com.zentao.repository.DesignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesignService {
    private final DesignRepository repo;

    public List<Design> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Design> getById(int id) {
        return repo.findById(id).filter(d -> d.getDeleted() != null && d.getDeleted() == 0);
    }

    @Transactional
    public Design create(Design e) {
        e.setId(null);
        e.setDeleted(0);
        return repo.save(e);
    }

    @Transactional
    public void update(Design e) {
        repo.findById(e.getId()).ifPresent(x -> repo.save(e));
    }

    @Transactional
    public void delete(int id) {
        repo.findById(id).ifPresent(d -> {
            d.setDeleted(1);
            repo.save(d);
        });
    }
}
