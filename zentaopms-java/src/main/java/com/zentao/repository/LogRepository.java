package com.zentao.repository;

import com.zentao.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {

    Page<Log> findAllByOrderByDateDesc(Pageable pageable);

    List<Log> findByObjectTypeAndObjectIDOrderByDateDesc(String objectType, int objectID, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Log l WHERE l.date < :before")
    void deleteByDateBefore(LocalDateTime before);
}
