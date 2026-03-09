package com.zentao.service;

import com.zentao.entity.TaskEstimate;
import com.zentao.repository.TaskEstimateRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskEstimateService {

    private final TaskEstimateRepository taskEstimateRepository;

    public Optional<TaskEstimate> getById(int id) {
        return taskEstimateRepository.findById(id);
    }

    public List<TaskEstimate> getByTask(int taskId) {
        return taskEstimateRepository.findByTaskOrderByOrderNumAsc(taskId);
    }

    public TaskEstimate create(TaskEstimate estimate) {
        return taskEstimateRepository.save(estimate);
    }

    public TaskEstimate update(TaskEstimate estimate) {
        return taskEstimateRepository.save(estimate);
    }

    public void delete(int estimateId) {
        taskEstimateRepository.deleteById(estimateId);
    }

    public TaskEstimate recordWorkhour(int taskId, LocalDate date, BigDecimal consumed, BigDecimal left, String work) {
        TaskEstimate e = new TaskEstimate();
        e.setTask(taskId);
        e.setDate(date);
        e.setConsumed(consumed != null ? consumed : BigDecimal.ZERO);
        e.setLeft_(left != null ? left : BigDecimal.ZERO);
        e.setAccount(getCurrentAccount());
        e.setWork(work != null ? work : "");
        return taskEstimateRepository.save(e);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
