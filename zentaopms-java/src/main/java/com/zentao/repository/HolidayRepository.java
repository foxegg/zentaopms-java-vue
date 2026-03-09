package com.zentao.repository;

import com.zentao.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    List<Holiday> findByYearOrderByBeginAsc(String year);

    @Query("SELECT DISTINCT h.year FROM Holiday h ORDER BY h.year DESC")
    List<String> findDistinctYears();
}
