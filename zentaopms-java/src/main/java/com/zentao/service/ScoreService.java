package com.zentao.service;

import com.zentao.entity.Score;
import com.zentao.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository repo;

    public List<Score> getList(String account, int pageID, int recPerPage) {
        if (account != null && !account.isBlank()) {
            return repo.findByAccountOrderByTimeDesc(account, PageRequest.of(Math.max(0, pageID - 1), recPerPage));
        }
        return repo.findAllByOrderByTimeDesc(PageRequest.of(Math.max(0, pageID - 1), recPerPage));
    }

    public Optional<Score> getById(long id) {
        return repo.findById(id);
    }
}
