package com.zentao.service;

import com.zentao.entity.Pipeline;
import com.zentao.repository.PipelineRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PipelineService {
    private final PipelineRepository repo;

    public List<Pipeline> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public List<Pipeline> getListByType(String type) {
        return repo.findByTypeAndDeletedOrderByIdAsc(type, 0);
    }

    public Optional<Pipeline> getById(int id) {
        return repo.findById(id).filter(x -> x.getDeleted() == 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public Pipeline create(Pipeline p) {
        p.setDeleted(0);
        p.setCreatedBy(getCurrentAccount());
        p.setCreatedDate(LocalDateTime.now());
        return repo.save(p);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public Pipeline update(Pipeline p) {
        return repo.save(p);
    }

    public void delete(int id) {
        Optional<Pipeline> opt = repo.findById(id);
        if (opt.isPresent()) {
            Pipeline x = opt.get();
            x.setDeleted(1);
            repo.save(x);
        }
    }
}
