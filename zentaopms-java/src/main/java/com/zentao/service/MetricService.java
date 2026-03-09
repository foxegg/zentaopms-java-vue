package com.zentao.service;

import com.zentao.entity.Metric;
import com.zentao.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Metric create(Metric metric) {
        metric.setDeleted(0);
        if (metric.getOrderNum() == null) metric.setOrderNum(0);
        return metricRepository.save(metric);
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
