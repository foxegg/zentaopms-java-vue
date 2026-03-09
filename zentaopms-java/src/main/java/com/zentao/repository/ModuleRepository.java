package com.zentao.repository;

import com.zentao.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

    List<Module> findByRootAndTypeAndDeletedOrderByOrderNumAsc(int rootId, String type, int deleted);

    List<Module> findByRootAndDeletedOrderByOrderNumAsc(int rootId, int deleted);
}
