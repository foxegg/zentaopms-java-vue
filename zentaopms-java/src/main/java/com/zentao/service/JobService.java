package com.zentao.service;

import com.zentao.entity.Job;
import com.zentao.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Optional<Job> getById(int id) {
        return jobRepository.findById(id).filter(j -> j.getDeleted() == 0);
    }

    public List<Job> getByRepo(int repoId) {
        return jobRepository.findByRepoAndDeletedOrderByIdDesc(repoId, 0);
    }

    public List<Job> getByProduct(int productId) {
        return jobRepository.findByProductAndDeletedOrderByIdDesc(productId, 0);
    }

    public Job create(Job job) {
        job.setDeleted(0);
        return jobRepository.save(job);
    }

    public Job update(Job job) {
        return jobRepository.save(job);
    }

    public void delete(int id) {
        jobRepository.findById(id).ifPresent(j -> {
            j.setDeleted(1);
            jobRepository.save(j);
        });
    }
}
