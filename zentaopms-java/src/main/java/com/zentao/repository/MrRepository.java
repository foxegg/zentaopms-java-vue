package com.zentao.repository;

import com.zentao.entity.Mr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MrRepository extends JpaRepository<Mr, Integer> {
    List<Mr> findByDeletedOrderByIdAsc(Integer deleted);
}
