package com.zentao.repository;

import com.zentao.entity.UserQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQueryRepository extends JpaRepository<UserQuery, Integer> {

    List<UserQuery> findByAccountAndModuleOrderByIdDesc(String account, String module);

    List<UserQuery> findByAccountOrderByIdDesc(String account);
}
