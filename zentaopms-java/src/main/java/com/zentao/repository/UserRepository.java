package com.zentao.repository;

import com.zentao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByAccountAndDeleted(String account, int deleted);

    Optional<User> findByAccount(String account);

    boolean existsByAccount(String account);

    List<User> findByDeptAndDeleted(int deptId, int deleted);
}
