package com.zentao.repository;

import com.zentao.entity.Serverroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerroomRepository extends JpaRepository<Serverroom, Integer> {
    List<Serverroom> findByDeletedOrderByIdAsc(int deleted);
}
