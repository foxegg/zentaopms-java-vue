package com.zentao.service;

import com.zentao.entity.Instance;
import com.zentao.repository.InstanceRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 应用实例服务 - 对应 module/instance
 */
@Service
@RequiredArgsConstructor
public class InstanceService {

    private final InstanceRepository instanceRepository;

    public List<Instance> getList() {
        return instanceRepository.findByDeletedOrderByIdDesc(0);
    }

    public List<Instance> getBySpace(int spaceId) {
        return instanceRepository.findBySpaceAndDeletedOrderByIdDesc(spaceId, 0);
    }

    public Page<Instance> getList(Pageable pageable) {
        return instanceRepository.findByDeleted(0, pageable);
    }

    public Optional<Instance> getById(int id) {
        return instanceRepository.findByIdAndDeleted(id, 0);
    }

    /** 与 PHP 一致：创建时设置 createdBy、createdAt（当前用户与时间） */
    public Instance create(Instance instance) {
        instance.setDeleted(0);
        instance.setCreatedBy(getCurrentAccount());
        if (instance.getCreatedAt() == null) instance.setCreatedAt(LocalDateTime.now());
        return instanceRepository.save(instance);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public Instance update(Instance instance) {
        return instanceRepository.save(instance);
    }

    @Transactional
    public void delete(int id) {
        instanceRepository.findById(id).ifPresent(i -> {
            i.setDeleted(1);
            instanceRepository.save(i);
        });
    }
}
