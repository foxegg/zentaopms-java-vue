package com.zentao.service;

import com.zentao.entity.Chart;
import com.zentao.repository.ChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final ChartRepository repo;

    public List<Chart> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Chart> getById(int id) {
        return repo.findById(id).filter(x -> x.getDeleted() == 0);
    }

    public Chart create(Chart c) {
        c.setDeleted(0);
        return repo.save(c);
    }

    public Chart update(Chart c) {
        return repo.save(c);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(x -> { x.setDeleted(1); repo.save(x); });
    }
}
