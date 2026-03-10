package com.zentao.service;

import com.zentao.entity.Notify;
import com.zentao.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 通知队列服务 - 对应 module/notify
 */
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;

    public List<Notify> getByStatus(String status) {
        return notifyRepository.findByStatusOrderByCreatedDateAsc(status != null ? status : "wait");
    }

    public Page<Notify> getPageByCreatedBy(String account, Pageable pageable) {
        return notifyRepository.findByCreatedByOrderByCreatedDateDesc(account, pageable);
    }

    public Optional<Notify> getById(int id) {
        return notifyRepository.findById(id);
    }

    /** 与 PHP mail getQueue 一致：objectType=mail，可选 status，分页 id_desc */
    public Page<Notify> getMailQueue(String status, Pageable pageable) {
        if (status != null && !status.isBlank() && !"all".equalsIgnoreCase(status)) {
            return notifyRepository.findByObjectTypeAndStatusOrderByIdDesc("mail", status, pageable);
        }
        return notifyRepository.findByObjectTypeOrderByIdDesc("mail", pageable);
    }

    @Transactional
    public void delete(int id) {
        notifyRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids != null) ids.forEach(notifyRepository::deleteById);
    }
}
