package com.zentao.repository;

import com.zentao.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
    List<Entry> findByDeletedOrderByIdAsc(int deleted);
    Optional<Entry> findByKeyStrAndDeleted(String keyStr, int deleted);
}
