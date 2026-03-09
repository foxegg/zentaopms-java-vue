package com.zentao.repository;

import com.zentao.entity.Compile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompileRepository extends JpaRepository<Compile, Integer> {

    List<Compile> findByJobAndDeletedOrderByCreatedDateDesc(int jobId, int deleted);

    List<Compile> findByDeletedOrderByCreatedDateDesc(int deleted);
}
