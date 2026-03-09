package com.zentao.repository;

import com.zentao.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Integer> {
    List<Host> findByDeletedOrderByIdAsc(Integer deleted);
}
