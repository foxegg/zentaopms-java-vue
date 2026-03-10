package com.zentao.service;

import com.zentao.entity.Job;
import com.zentao.repository.JobRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Job> getByServer(int serverId) {
        return jobRepository.findByServerAndDeletedOrderByIdDesc(serverId, 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public Job create(Job job) {
        job.setDeleted(0);
        job.setCreatedBy(getCurrentAccount());
        job.setCreatedDate(LocalDateTime.now());
        return jobRepository.save(job);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
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
