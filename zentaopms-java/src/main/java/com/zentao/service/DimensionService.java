package com.zentao.service;

import com.zentao.entity.Dimension;
import com.zentao.repository.DimensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DimensionService {
    private final DimensionRepository repo;

    public List<Dimension> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Dimension> getById(int id) {
        return repo.findById(id).filter(d -> d.getDeleted() == 0);
    }

    public Dimension create(Dimension d) {
        d.setDeleted(0);
        return repo.save(d);
    }

    public Dimension update(Dimension d) {
        return repo.save(d);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(d -> { d.setDeleted(1); repo.save(d); });
    }
}
