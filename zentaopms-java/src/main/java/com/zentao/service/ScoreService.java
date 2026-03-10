package com.zentao.service;

import com.zentao.entity.Score;
import com.zentao.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository repo;

    /** 与 PHP my/score 一致：分页按时间倒序，返回 Page 便于带 pager */
    public Page<Score> getList(String account, int pageID, int recPerPage) {
        PageRequest pr = PageRequest.of(Math.max(0, pageID - 1), Math.max(1, recPerPage));
        if (account != null && !account.isBlank()) {
            return repo.findByAccountOrderByTimeDesc(account, pr);
        }
        return repo.findAllByOrderByTimeDesc(pr);
    }

    public Optional<Score> getById(long id) {
        return repo.findById(id);
    }
}
