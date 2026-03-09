package com.zentao.repository;

import com.zentao.entity.GroupPriv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupPrivRepository extends JpaRepository<GroupPriv, Integer> {

    List<GroupPriv> findByGroupId(Integer groupId);

    void deleteByGroupId(Integer groupId);
}
