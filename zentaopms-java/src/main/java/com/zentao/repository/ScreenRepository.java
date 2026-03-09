package com.zentao.repository;

import com.zentao.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Integer> {
    List<Screen> findByDeletedOrderByIdAsc(int deleted);
}
