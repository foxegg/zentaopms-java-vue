package com.zentao.repository;

import com.zentao.entity.Instance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstanceRepository extends JpaRepository<Instance, Integer> {

    List<Instance> findByDeletedOrderByIdDesc(int deleted);

    List<Instance> findBySpaceAndDeletedOrderByIdDesc(int spaceId, int deleted);

    Page<Instance> findByDeleted(int deleted, Pageable pageable);

    Optional<Instance> findByIdAndDeleted(int id, int deleted);
}
