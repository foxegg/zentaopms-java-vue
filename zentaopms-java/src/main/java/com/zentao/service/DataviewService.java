package com.zentao.service;

import com.zentao.entity.Dataview;
import com.zentao.repository.DataviewRepository;
import lombok.RequiredArgsConstructor;
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

    public Dataview create(Dataview d) {
        d.setDeleted(0);
        return repo.save(d);
    }

    public Dataview update(Dataview d) {
        return repo.save(d);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(d -> { d.setDeleted(1); repo.save(d); });
    }
}
