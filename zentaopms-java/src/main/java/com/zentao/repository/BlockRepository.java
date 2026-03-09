package com.zentao.repository;

import com.zentao.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Integer> {

    List<Block> findByAccountOrderByIdAsc(String account);

    List<Block> findByAccountAndDashboardOrderByLeftPosAscTopPosAsc(String account, String dashboard);
}
