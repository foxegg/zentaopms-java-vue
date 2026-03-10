package com.zentao.repository;

import com.zentao.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findByProject(int projectId);

    /** 与 PHP group getList/getPairs 的 vision 过滤一致 */
    List<Group> findByProjectAndVision(int projectId, String vision);
}
