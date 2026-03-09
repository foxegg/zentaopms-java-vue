package com.zentao.service;

import com.zentao.entity.Compile;
import com.zentao.repository.CompileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompileService {

    private final CompileRepository compileRepository;

    public Optional<Compile> getById(int id) {
        return compileRepository.findById(id).filter(c -> c.getDeleted() == 0);
    }

    public List<Compile> getByJob(int jobId) {
        return compileRepository.findByJobAndDeletedOrderByCreatedDateDesc(jobId, 0);
    }

    public List<Compile> getAll() {
        return compileRepository.findByDeletedOrderByCreatedDateDesc(0);
    }

    public Compile create(Compile compile) {
        compile.setDeleted(0);
        return compileRepository.save(compile);
    }

    public Compile update(Compile compile) {
        return compileRepository.save(compile);
    }

    public void delete(int id) {
        compileRepository.findById(id).ifPresent(c -> {
            c.setDeleted(1);
            compileRepository.save(c);
        });
    }
}
