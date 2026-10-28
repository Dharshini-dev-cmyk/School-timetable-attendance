package com.examly.springapp.service;

import com.examly.springapp.dto.AcademicCalendarRequestDTO;
import com.examly.springapp.entity.AcademicCalendar;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.AcademicCalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicCalendarServiceTest {

    @Mock
    private AcademicCalendarRepository calendarRepository;

    @InjectMocks
    private AcademicCalendarService calendarService;

    private AcademicCalendar calendar;
    private AcademicCalendarRequestDTO dto;

    @BeforeEach
    void setUp() {
        calendar = new AcademicCalendar(
                2025,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 3, 31),
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 10, 31),
                LocalDate.of(2025, 11, 1),
                LocalDate.of(2026, 3, 31)
        );
        calendar.setId(1L);

        dto = new AcademicCalendarRequestDTO();
        dto.setAcademicYear(2025);
        dto.setStartDate(LocalDate.of(2025, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 3, 31));
        dto.setTerm1Start(LocalDate.of(2025, 6, 1));
        dto.setTerm1End(LocalDate.of(2025, 10, 31));
        dto.setTerm2Start(LocalDate.of(2025, 11, 1));
        dto.setTerm2End(LocalDate.of(2026, 3, 31));
    }

    @Test
    void createCalendar_Success() {
        when(calendarRepository.existsByAcademicYear(2025)).thenReturn(false);
        when(calendarRepository.save(any(AcademicCalendar.class))).thenReturn(calendar);

        AcademicCalendar result = calendarService.createCalendar(dto);

        assertNotNull(result);
        assertEquals(2025, result.getAcademicYear());
        verify(calendarRepository).save(any(AcademicCalendar.class));
    }

    @Test
    void createCalendar_DuplicateYear_ThrowsDuplicateStudentException() {
        when(calendarRepository.existsByAcademicYear(2025)).thenReturn(true);

        assertThrows(DuplicateStudentException.class, () -> calendarService.createCalendar(dto));
        verify(calendarRepository, never()).save(any());
    }

    @Test
    void getActiveCalendar_Success() {
        when(calendarRepository.findByActiveTrue()).thenReturn(Optional.of(calendar));

        AcademicCalendar result = calendarService.getActiveCalendar();

        assertNotNull(result);
        assertTrue(result.isActive());
    }

    @Test
    void getActiveCalendar_NotFound_ThrowsResourceNotFoundException() {
        when(calendarRepository.findByActiveTrue()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calendarService.getActiveCalendar());
    }

    @Test
    void getCalendarByYear_Success() {
        when(calendarRepository.findByAcademicYear(2025)).thenReturn(Optional.of(calendar));

        AcademicCalendar result = calendarService.getCalendarByYear(2025);

        assertNotNull(result);
        assertEquals(2025, result.getAcademicYear());
    }

    @Test
    void getCalendarByYear_NotFound_ThrowsResourceNotFoundException() {
        when(calendarRepository.findByAcademicYear(9999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calendarService.getCalendarByYear(9999));
    }

    @Test
    void getAllCalendars_ReturnsList() {
        when(calendarRepository.findAll()).thenReturn(List.of(calendar));

        List<AcademicCalendar> result = calendarService.getAllCalendars();

        assertEquals(1, result.size());
    }

    @Test
    void updateCalendar_Success() {
        when(calendarRepository.findById(1L)).thenReturn(Optional.of(calendar));
        when(calendarRepository.save(any(AcademicCalendar.class))).thenReturn(calendar);

        AcademicCalendar result = calendarService.updateCalendar(1L, dto);

        assertNotNull(result);
        verify(calendarRepository).save(any(AcademicCalendar.class));
    }

    @Test
    void updateCalendar_NotFound_ThrowsResourceNotFoundException() {
        when(calendarRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calendarService.updateCalendar(99L, dto));
    }
}
