package com.zentao.repository;

import com.zentao.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Integer> {

    List<Space> findByOwnerAndDeletedOrderByDefaultDesc(String owner, int deleted);

    Optional<Space> findByIdAndOwnerAndDeleted(int id, String owner, int deleted);
}
