package com.zentao.service;

import com.zentao.entity.Space;
import com.zentao.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 空间模块服务 - 对应 module/space
 */
@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public List<Space> getByOwner(String account) {
        return spaceRepository.findByOwnerAndDeletedOrderByDefaultDesc(account != null ? account : "", 0);
    }

    public Optional<Space> getByIdAndOwner(int id, String owner) {
        return spaceRepository.findByIdAndOwnerAndDeleted(id, owner != null ? owner : "", 0);
    }

    public Optional<Space> getById(int id) {
        return spaceRepository.findById(id).filter(s -> s.getDeleted() == 0);
    }

    public Space create(Space space) {
        space.setDeleted(0);
        if (space.getCreatedAt() == null) space.setCreatedAt(LocalDateTime.now());
        return spaceRepository.save(space);
    }

    public Space update(Space space) {
        return spaceRepository.save(space);
    }

    @Transactional
    public void delete(int id) {
        spaceRepository.findById(id).ifPresent(s -> {
            s.setDeleted(1);
            spaceRepository.save(s);
        });
    }
}
