package com.zentao.service;

import com.zentao.entity.Cron;
import com.zentao.repository.CronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CronService {

    private final CronRepository cronRepository;

    public Optional<Cron> getById(int id) {
        return cronRepository.findById(id);
    }

    public List<Cron> getAll() {
        return cronRepository.findAllByOrderByIdAsc();
    }

    public Cron create(Cron cron) {
        return cronRepository.save(cron);
    }

    public Cron update(Cron cron) {
        return cronRepository.save(cron);
    }

    public void delete(int id) {
        cronRepository.deleteById(id);
    }

    public Cron toggle(int id, String status) {
        Cron c = cronRepository.findById(id).orElseThrow(() -> new RuntimeException("定时任务不存在"));
        if (status != null && !status.isEmpty()) {
            c.setStatus(status);
        } else {
            c.setStatus("stop".equals(c.getStatus()) ? "normal" : "stop");
        }
        return cronRepository.save(c);
    }

    /** 立即执行一次（占位：仅更新 lastTime，实际执行需接入调度器） */
    public Cron run(int id) {
        Cron c = cronRepository.findById(id).orElseThrow(() -> new RuntimeException("定时任务不存在"));
        c.setLastTime(java.time.LocalDateTime.now());
        return cronRepository.save(c);
    }
}
