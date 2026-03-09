package com.zentao.service;

import com.zentao.entity.Entry;
import com.zentao.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository repo;

    public List<Entry> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Entry> getById(int id) {
        return repo.findById(id).filter(e -> e.getDeleted() == 0);
    }

    public Entry create(Entry e) {
        e.setDeleted(0);
        return repo.save(e);
    }

    public Entry update(Entry e) {
        return repo.save(e);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(e -> { e.setDeleted(1); repo.save(e); });
    }
}
