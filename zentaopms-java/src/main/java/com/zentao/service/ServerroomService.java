package com.zentao.service;

import com.zentao.entity.Serverroom;
import com.zentao.repository.ServerroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServerroomService {
    private final ServerroomRepository repo;

    public List<Serverroom> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Serverroom> getById(int id) {
        return repo.findById(id).filter(x -> x.getDeleted() == 0);
    }

    public Serverroom create(Serverroom s) {
        s.setDeleted(0);
        return repo.save(s);
    }

    public Serverroom update(Serverroom s) {
        return repo.save(s);
    }

    public void delete(int id) {
        repo.findById(id).ifPresent(x -> { x.setDeleted(1); repo.save(x); });
    }
}
