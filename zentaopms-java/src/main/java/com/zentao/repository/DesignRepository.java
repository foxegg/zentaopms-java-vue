package com.zentao.repository;

import com.zentao.entity.Design;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Integer> {
    List<Design> findByDeletedOrderByIdAsc(Integer deleted);
}
