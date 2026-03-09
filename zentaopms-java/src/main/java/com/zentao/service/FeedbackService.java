package com.zentao.service;

import com.zentao.entity.Feedback;
import com.zentao.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository repo;

    public List<Feedback> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Feedback> getById(int id) {
        return repo.findById(id).filter(f -> f.getDeleted() != null && f.getDeleted() == 0);
    }

    @Transactional
    public Feedback create(Feedback e) {
        e.setId(null);
        e.setDeleted(0);
        return repo.save(e);
    }

    @Transactional
    public void update(Feedback e) {
        repo.findById(e.getId()).ifPresent(x -> repo.save(e));
    }

    @Transactional
    public void delete(int id) {
        repo.findById(id).ifPresent(f -> {
            f.setDeleted(1);
            repo.save(f);
        });
    }
}
