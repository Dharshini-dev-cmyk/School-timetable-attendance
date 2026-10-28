package com.examly.springapp.service;

import com.examly.springapp.dto.TimetableRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.Timetable;
import com.examly.springapp.entity.Timetable.Day;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.TimetableClashException;
import com.examly.springapp.repository.TeacherRepository;
import com.examly.springapp.repository.TimetableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    @Mock
    private TimetableRepository timetableRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TimetableService timetableService;

    private Teacher teacher;
    private Timetable timetable;
    private TimetableRequestDTO dto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("EMP001", "Senior Teacher", "Mathematics", null);
        teacher.setId(1L);

        timetable = new Timetable("10A", Day.MON, 1, "Mathematics", teacher);
        timetable.setId(1L);

        dto = new TimetableRequestDTO();
        dto.setClassSection("10A");
        dto.setDay("MON");
        dto.setPeriod(1);
        dto.setSubject("Mathematics");
        dto.setTeacherId(1L);
    }

    @Test
    void createSlot_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(timetableRepository.existsByTeacherIdAndDayAndPeriodAndActiveTrue(1L, Day.MON, 1)).thenReturn(false);
        when(timetableRepository.save(any(Timetable.class))).thenReturn(timetable);

        Timetable result = timetableService.createSlot(dto);

        assertNotNull(result);
        assertEquals("10A", result.getClassSection());
        verify(timetableRepository).save(any(Timetable.class));
    }

    @Test
    void createSlot_ClashDetected_ThrowsTimetableClashException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(timetableRepository.existsByTeacherIdAndDayAndPeriodAndActiveTrue(1L, Day.MON, 1)).thenReturn(true);

        assertThrows(TimetableClashException.class, () -> timetableService.createSlot(dto));
        verify(timetableRepository, never()).save(any());
    }

    @Test
    void createSlot_TeacherNotFound_ThrowsResourceNotFoundException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> timetableService.createSlot(dto));
    }

    @Test
    void getClassTimetable_ReturnsList() {
        when(timetableRepository.findByClassSectionAndActiveTrue("10A")).thenReturn(List.of(timetable));

        List<Timetable> result = timetableService.getClassTimetable("10A");

        assertEquals(1, result.size());
        assertEquals("10A", result.get(0).getClassSection());
    }

    @Test
    void getTeacherTimetable_ReturnsList() {
        when(timetableRepository.findByTeacherIdAndActiveTrue(1L)).thenReturn(List.of(timetable));

        List<Timetable> result = timetableService.getTeacherTimetable(1L);

        assertEquals(1, result.size());
    }

    @Test
    void detectClashes_ReturnsList() {
        when(timetableRepository.findAllClashes()).thenReturn(List.of(timetable));

        List<Timetable> result = timetableService.detectClashes();

        assertEquals(1, result.size());
    }
}
