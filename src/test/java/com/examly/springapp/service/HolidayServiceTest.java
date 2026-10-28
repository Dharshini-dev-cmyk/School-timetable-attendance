package com.examly.springapp.service;

import com.examly.springapp.dto.HolidayRequestDTO;
import com.examly.springapp.entity.Holiday;
import com.examly.springapp.entity.Holiday.HolidayType;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.HolidayRepository;
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
class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayService holidayService;

    private Holiday holiday;
    private HolidayRequestDTO dto;

    @BeforeEach
    void setUp() {
        holiday = new Holiday("Republic Day", LocalDate.of(2025, 1, 26), HolidayType.NATIONAL, "National holiday", 2025);
        holiday.setId(1L);

        dto = new HolidayRequestDTO();
        dto.setName("Republic Day");
        dto.setDate(LocalDate.of(2025, 1, 26));
        dto.setHolidayType("NATIONAL");
        dto.setReason("National holiday");
        dto.setAcademicYear(2025);
    }

    @Test
    void addHoliday_Success() {
        when(holidayRepository.existsByDate(dto.getDate())).thenReturn(false);
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);

        Holiday result = holidayService.addHoliday(dto);

        assertNotNull(result);
        assertEquals("Republic Day", result.getName());
        verify(holidayRepository).save(any(Holiday.class));
    }

    @Test
    void addHoliday_DuplicateDate_ThrowsDuplicateStudentException() {
        when(holidayRepository.existsByDate(dto.getDate())).thenReturn(true);

        assertThrows(DuplicateStudentException.class, () -> holidayService.addHoliday(dto));
        verify(holidayRepository, never()).save(any());
    }

    @Test
    void getHolidaysByYear_ReturnsList() {
        when(holidayRepository.findByAcademicYear(2025)).thenReturn(List.of(holiday));

        List<Holiday> result = holidayService.getHolidaysByYear(2025);

        assertEquals(1, result.size());
    }

    @Test
    void getHolidayById_NotFound_ThrowsResourceNotFoundException() {
        when(holidayRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> holidayService.getHolidayById(99L));
    }

    @Test
    void deleteHoliday_Success() {
        when(holidayRepository.existsById(1L)).thenReturn(true);
        doNothing().when(holidayRepository).deleteById(1L);

        assertDoesNotThrow(() -> holidayService.deleteHoliday(1L));
        verify(holidayRepository).deleteById(1L);
    }

    @Test
    void countWorkingDays_ExcludesHolidaysAndSundays() {
        // Week of Mon 2025-01-20 to Sat 2025-01-25 = 6 days, minus 1 holiday = 5
        Holiday midWeekHoliday = new Holiday("Test", LocalDate.of(2025, 1, 22), HolidayType.SCHOOL, "Test", 2025);
        when(holidayRepository.findByAcademicYear(2025)).thenReturn(List.of(midWeekHoliday));

        long result = holidayService.countWorkingDays(
                LocalDate.of(2025, 1, 20),
                LocalDate.of(2025, 1, 25),
                2025
        );

        assertEquals(5, result);
    }
}
