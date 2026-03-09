package com.zentao.repository;

import com.zentao.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {

    List<File> findByObjectTypeAndObjectIDAndDeleted(String objectType, int objectId, int deleted);
}
