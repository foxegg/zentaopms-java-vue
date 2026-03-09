package com.zentao.service;

import com.zentao.entity.Pipeline;
import com.zentao.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Pipeline create(Pipeline p) {
        p.setDeleted(0);
        return repo.save(p);
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
