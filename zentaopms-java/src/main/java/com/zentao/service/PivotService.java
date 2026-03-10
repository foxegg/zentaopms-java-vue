package com.zentao.service;

import com.zentao.entity.Pivot;
import com.zentao.repository.PivotRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 数据透视服务 - 对应 module/pivot
 */
@Service
@RequiredArgsConstructor
public class PivotService {

    private final PivotRepository pivotRepository;

    public List<Pivot> getList() {
        return pivotRepository.findByDeletedOrderByIdDesc(0);
    }

    public List<Pivot> getByDimension(int dimensionId) {
        return pivotRepository.findByDimensionAndDeletedOrderByIdDesc(dimensionId, 0);
    }

    public Optional<Pivot> getById(int id) {
        return pivotRepository.findByIdAndDeleted(id, 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdDate（当前用户与时间） */
    public Pivot create(Pivot pivot) {
        pivot.setDeleted(0);
        pivot.setCreatedBy(getCurrentAccount());
        if (pivot.getCreatedDate() == null) pivot.setCreatedDate(LocalDateTime.now());
        if (pivot.getVersion() == null || pivot.getVersion().isBlank()) pivot.setVersion("1");
        return pivotRepository.save(pivot);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public Pivot update(Pivot pivot) {
        if (pivot.getEditedDate() == null) pivot.setEditedDate(LocalDateTime.now());
        return pivotRepository.save(pivot);
    }

    @Transactional
    public void delete(int id) {
        pivotRepository.findById(id).ifPresent(p -> {
            p.setDeleted(1);
            pivotRepository.save(p);
        });
    }
}
