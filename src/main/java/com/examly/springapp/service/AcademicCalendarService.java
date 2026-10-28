package com.examly.springapp.service;

import com.examly.springapp.dto.AcademicCalendarRequestDTO;
import com.examly.springapp.entity.AcademicCalendar;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.AcademicCalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AcademicCalendarService {

    private final AcademicCalendarRepository calendarRepository;

    public AcademicCalendarService(AcademicCalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @Transactional
    public AcademicCalendar createCalendar(AcademicCalendarRequestDTO dto) {
        if (calendarRepository.existsByAcademicYear(dto.getAcademicYear())) {
            throw new DuplicateStudentException("Academic calendar already exists for year: " + dto.getAcademicYear());
        }
        AcademicCalendar calendar = new AcademicCalendar(
                dto.getAcademicYear(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getTerm1Start(),
                dto.getTerm1End(),
                dto.getTerm2Start(),
                dto.getTerm2End()
        );
        return calendarRepository.save(calendar);
    }

    public AcademicCalendar getActiveCalendar() {
        return calendarRepository.findByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active academic calendar found"));
    }

    public AcademicCalendar getCalendarByYear(int academicYear) {
        return calendarRepository.findByAcademicYear(academicYear)
                .orElseThrow(() -> new ResourceNotFoundException("Academic calendar not found for year: " + academicYear));
    }

    public List<AcademicCalendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    @Transactional
    public AcademicCalendar updateCalendar(Long id, AcademicCalendarRequestDTO dto) {
        AcademicCalendar calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic calendar not found with ID: " + id));
        calendar.setStartDate(dto.getStartDate());
        calendar.setEndDate(dto.getEndDate());
        calendar.setTerm1Start(dto.getTerm1Start());
        calendar.setTerm1End(dto.getTerm1End());
        calendar.setTerm2Start(dto.getTerm2Start());
        calendar.setTerm2End(dto.getTerm2End());
        return calendarRepository.save(calendar);
    }
}
