package com.zentao.repository;

import com.zentao.entity.Acl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AclRepository extends JpaRepository<Acl, Integer> {

    List<Acl> findByObjectTypeAndObjectID(String objectType, int objectID);

    List<Acl> findByObjectTypeAndObjectIDAndType(String objectType, int objectID, String type);

    void deleteByObjectTypeAndObjectID(String objectType, int objectID);

    void deleteByObjectTypeAndObjectIDAndAccount(String objectType, int objectID, String account);
}
