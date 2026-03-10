package com.zentao.service;

import com.zentao.entity.Mark;
import com.zentao.repository.MarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 标记服务 - 对应 module/mark
 */
@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    public List<Mark> getByAccount(String account) {
        return markRepository.findByAccountOrderByIdDesc(account != null ? account : "");
    }

    public List<Mark> getByAccountAndObjectType(String account, String objectType) {
        if (objectType == null || objectType.isBlank())
            return getByAccount(account);
        return markRepository.findByAccountAndObjectTypeOrderByIdDesc(account != null ? account : "", objectType);
    }

    public Optional<Mark> getById(int id) {
        return markRepository.findById(id);
    }

    public boolean hasMark(String objectType, int objectID, String account, String mark) {
        return !markRepository.findByObjectTypeAndObjectIDAndAccountAndMark(
                objectType != null ? objectType : "", objectID, account != null ? account : "", mark != null ? mark : "view").isEmpty();
    }

    public Mark create(Mark mark) {
        if (mark.getDate() == null) mark.setDate(LocalDateTime.now());
        if (mark.getVersion() == null || mark.getVersion().isBlank()) mark.setVersion("1");
        return markRepository.save(mark);
    }

    public Mark update(Mark mark) {
        return markRepository.save(mark);
    }

    @Transactional
    public void delete(int id) {
        markRepository.deleteById(id);
    }
}
