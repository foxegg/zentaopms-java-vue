package com.zentao.service;

import com.zentao.entity.Metric;
import com.zentao.repository.MetricRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MetricRepository metricRepository;

    public List<Metric> getList() {
        return metricRepository.findByDeletedOrderByOrderNumAscIdAsc(0);
    }

    public Optional<Metric> getById(int id) {
        return metricRepository.findById(id).filter(m -> m.getDeleted() == 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy（当前用户） */
    @Transactional
    public Metric create(Metric metric) {
        metric.setDeleted(0);
        if (metric.getOrderNum() == null) metric.setOrderNum(0);
        metric.setCreatedBy(getCurrentAccount());
        metric.setCreatedDate(LocalDateTime.now());
        return metricRepository.save(metric);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    @Transactional
    public Metric update(Metric metric) {
        return metricRepository.save(metric);
    }

    @Transactional
    public void delete(int id) {
        metricRepository.findById(id).ifPresent(m -> {
            m.setDeleted(1);
            metricRepository.save(m);
        });
    }
}
