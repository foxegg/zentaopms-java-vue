package com.zentao.repository;

import com.zentao.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

    List<Project> findByDeletedOrderByIdDesc(int deleted);

    List<Project> findByProjectAndTypeInAndDeleted(Integer project, List<String> type, int deleted);
}
