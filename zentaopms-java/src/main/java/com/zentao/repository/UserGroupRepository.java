package com.zentao.repository;

import com.zentao.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    List<UserGroup> findByAccount(String account);

    List<UserGroup> findByGroupId(Integer groupId);

    void deleteByGroupIdAndAccount(Integer groupId, String account);

    boolean existsByGroupIdAndAccount(Integer groupId, String account);
}
