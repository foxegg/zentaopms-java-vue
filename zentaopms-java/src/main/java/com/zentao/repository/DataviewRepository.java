package com.zentao.repository;

import com.zentao.entity.Dataview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataviewRepository extends JpaRepository<Dataview, Integer> {
    List<Dataview> findByDeletedOrderByIdAsc(int deleted);
}
