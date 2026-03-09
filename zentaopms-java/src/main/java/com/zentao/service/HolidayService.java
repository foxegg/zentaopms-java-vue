package com.zentao.service;

import com.zentao.entity.Holiday;
import com.zentao.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public List<Holiday> getList(String year) {
        if (year == null || year.isBlank()) {
            year = String.valueOf(java.time.Year.now().getValue());
        }
        return holidayRepository.findByYearOrderByBeginAsc(year);
    }

    public List<String> getYearPairs() {
        return holidayRepository.findDistinctYears();
    }

    public Optional<Holiday> getById(int id) {
        return holidayRepository.findById(id);
    }

    public Holiday create(Holiday holiday) {
        if (holiday.getBegin() != null) {
            holiday.setYear(String.valueOf(holiday.getBegin().getYear()));
        }
        if (holiday.getYear() == null) holiday.setYear("");
        if (holiday.getType() == null) holiday.setType("holiday");
        if (holiday.getName() == null) holiday.setName("");
        return holidayRepository.save(holiday);
    }

    public Holiday update(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public void delete(int id) {
        holidayRepository.deleteById(id);
    }
}
