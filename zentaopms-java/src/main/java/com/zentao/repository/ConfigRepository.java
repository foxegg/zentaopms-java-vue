package com.zentao.repository;

import com.zentao.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Integer> {

    Optional<Config> findByOwnerAndModuleAndSectionAndKey_(String owner, String module, String section, String key);

    List<Config> findByOwnerAndModuleAndSection(String owner, String module, String section);
}
