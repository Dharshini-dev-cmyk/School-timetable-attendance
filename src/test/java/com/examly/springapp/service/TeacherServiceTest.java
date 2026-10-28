package com.examly.springapp.service;

import com.examly.springapp.dto.TeacherRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.TeacherRepository;
import com.examly.springapp.repository.UserRepository;
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
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private User user;
    private TeacherRequestDTO dto;

    @BeforeEach
    void setUp() {
        user = new User("Jane Teacher", "jane@school.com", "pass", "TEACHER", "9876543210");
        user.setId(1L);

        teacher = new Teacher("EMP001", "Senior Teacher", "Mathematics", user);
        teacher.setId(1L);

        dto = new TeacherRequestDTO();
        dto.setEmployeeId("EMP001");
        dto.setDesignation("Senior Teacher");
        dto.setSubject("Mathematics");
        dto.setUserId(1L);
    }

    @Test
    void createTeacher_Success() {
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher result = teacherService.createTeacher(dto);

        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeId());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void createTeacher_DuplicateEmployeeId_ThrowsDuplicateStudentException() {
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(true);

        assertThrows(DuplicateStudentException.class, () -> teacherService.createTeacher(dto));
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void createTeacher_UserNotFound_ThrowsResourceNotFoundException() {
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.createTeacher(dto));
    }

    @Test
    void getAllTeachers_ReturnsList() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> result = teacherService.getAllTeachers();

        assertEquals(1, result.size());
    }

    @Test
    void getTeacherById_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1L);

        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeId());
    }

    @Test
    void getTeacherById_NotFound_ThrowsResourceNotFoundException() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherById(99L));
    }

    @Test
    void updateTeacher_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher result = teacherService.updateTeacher(1L, dto);

        assertNotNull(result);
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void deleteTeacher_Success() {
        when(teacherRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teacherRepository).deleteById(1L);

        assertDoesNotThrow(() -> teacherService.deleteTeacher(1L));
        verify(teacherRepository).deleteById(1L);
    }

    @Test
    void deleteTeacher_NotFound_ThrowsResourceNotFoundException() {
        when(teacherRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(99L));
    }
}
