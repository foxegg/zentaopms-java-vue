package com.zentao.repository;

import com.zentao.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {

    List<Notify> findByStatusOrderByCreatedDateAsc(String status);

    List<Notify> findByCreatedByOrderByCreatedDateDesc(String account, org.springframework.data.domain.Pageable p);
}
