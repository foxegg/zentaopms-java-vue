package com.zentao.repository;

import com.zentao.entity.UserView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserViewRepository extends JpaRepository<UserView, Integer> {

    Optional<UserView> findByAccount(String account);
}
