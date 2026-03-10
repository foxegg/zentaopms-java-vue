package com.zentao.repository;

import com.zentao.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {

    List<Mark> findByAccountOrderByIdDesc(String account);

    List<Mark> findByAccountAndObjectTypeOrderByIdDesc(String account, String objectType);

    Optional<Mark> findById(Integer id);

    List<Mark> findByObjectTypeAndObjectIDAndAccountAndMark(String objectType, int objectID, String account, String mark);
}
