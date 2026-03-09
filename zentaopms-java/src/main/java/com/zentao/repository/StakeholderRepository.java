package com.zentao.repository;

import com.zentao.entity.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StakeholderRepository extends JpaRepository<Stakeholder, Integer> {

    List<Stakeholder> findByObjectIDAndObjectTypeAndDeleted(int objectID, String objectType, int deleted);
}
