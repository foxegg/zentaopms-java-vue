package com.zentao.repository;

import com.zentao.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findByAccountAndDeletedOrderByDateDesc(String account, int deleted);

    List<Todo> findByAssignedToAndDeleted(String assignedTo, int deleted);

    List<Todo> findByDateAndAccountAndDeleted(LocalDate date, String account, int deleted);

    long countByDeleted(int deleted);
}
