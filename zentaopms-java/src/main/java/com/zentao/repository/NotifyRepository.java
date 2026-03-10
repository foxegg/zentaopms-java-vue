package com.zentao.repository;

import com.zentao.entity.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {

    List<Notify> findByStatusOrderByCreatedDateAsc(String status);

    Page<Notify> findByCreatedByOrderByCreatedDateDesc(String account, Pageable p);

    Page<Notify> findByObjectTypeOrderByIdDesc(String objectType, Pageable p);

    Page<Notify> findByObjectTypeAndStatusOrderByIdDesc(String objectType, String status, Pageable p);
}
