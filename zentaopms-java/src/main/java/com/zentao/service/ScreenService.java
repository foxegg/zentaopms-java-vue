package com.zentao.service;

import com.zentao.entity.Screen;
import com.zentao.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreenService {
    private final ScreenRepository repo;

    public List<Screen> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Screen> getById(int id) {
        return repo.findById(id).filter(x -> x.getDeleted() == 0);
    }

    public Screen create(Screen s) {
        s.setDeleted(0);
        return repo.save(s);
    }

    public Screen update(Screen s) {
        return repo.save(s);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(x -> { x.setDeleted(1); repo.save(x); });
    }
}
