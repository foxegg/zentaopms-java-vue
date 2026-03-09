package com.zentao.repository;

import com.zentao.entity.Extension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExtensionRepository extends JpaRepository<Extension, Integer> {

    List<Extension> findByStatusOrderByInstalledTimeDesc(String status);

    Optional<Extension> findByCode(String code);
}
