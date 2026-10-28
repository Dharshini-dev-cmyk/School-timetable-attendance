package com.examly.springapp.service;

import com.examly.springapp.dto.HolidayRequestDTO;
import com.examly.springapp.entity.Holiday;
import com.examly.springapp.entity.Holiday.HolidayType;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Transactional
    public Holiday addHoliday(HolidayRequestDTO dto) {
        if (holidayRepository.existsByDate(dto.getDate())) {
            throw new DuplicateStudentException("A holiday is already configured for date: " + dto.getDate());
        }
        Holiday holiday = new Holiday(
                dto.getName(),
                dto.getDate(),
                HolidayType.valueOf(dto.getHolidayType().toUpperCase()),
                dto.getReason(),
                dto.getAcademicYear()
        );
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getHolidaysByYear(int academicYear) {
        return holidayRepository.findByAcademicYear(academicYear);
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public Holiday getHolidayById(Long id) {
        return holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with ID: " + id));
    }

    @Transactional
    public void deleteHoliday(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Holiday not found with ID: " + id);
        }
        holidayRepository.deleteById(id);
    }

    // FR12: Working days calculator — excludes all configured holidays from attendance denominators
    public long countWorkingDays(LocalDate from, LocalDate to, int academicYear) {
        Set<LocalDate> holidayDates = holidayRepository.findByAcademicYear(academicYear)
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        return from.datesUntil(to.plusDays(1))
                .filter(date -> {
                    int dayOfWeek = date.getDayOfWeek().getValue();
                    return dayOfWeek <= 6 && !holidayDates.contains(date); // Mon–Sat, excluding holidays
                })
                .count();
    }
}
