package com.zentao.service;

import com.zentao.entity.Block;
import com.zentao.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;

    public Optional<Block> getById(int id) {
        return blockRepository.findById(id);
    }

    public List<Block> getByAccount(String account) {
        return blockRepository.findByAccountOrderByIdAsc(account);
    }

    public List<Block> getByAccountAndDashboard(String account, String dashboard) {
        return blockRepository.findByAccountAndDashboardOrderByLeftPosAscTopPosAsc(account, dashboard);
    }

    public Block create(Block block) {
        return blockRepository.save(block);
    }

    public Block update(Block block) {
        return blockRepository.save(block);
    }

    public void delete(int id) {
        blockRepository.deleteById(id);
    }
}
