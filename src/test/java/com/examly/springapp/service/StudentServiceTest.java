package com.examly.springapp.service;

import com.examly.springapp.dto.StudentRequestDTO;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.StudentRepository;
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
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private User user;
    private StudentRequestDTO dto;

    @BeforeEach
    void setUp() {
        user = new User("John Student", "john@school.com", "pass", "STUDENT", "9876543210");
        user.setId(1L);

        student = new Student("REG001", "Science", 10, user);
        student.setId(1L);

        dto = new StudentRequestDTO();
        dto.setRegisterNumber("REG001");
        dto.setDepartment("Science");
        dto.setYear(10);
        dto.setUserId(1L);
    }

    @Test
    void createStudent_Success() {
        when(studentRepository.existsByRegisterNumber("REG001")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(dto);

        assertNotNull(result);
        assertEquals("REG001", result.getRegisterNumber());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_DuplicateRegisterNumber_ThrowsDuplicateStudentException() {
        when(studentRepository.existsByRegisterNumber("REG001")).thenReturn(true);

        assertThrows(DuplicateStudentException.class, () -> studentService.createStudent(dto));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void createStudent_UserNotFound_ThrowsResourceNotFoundException() {
        when(studentRepository.existsByRegisterNumber("REG001")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(dto));
    }

    @Test
    void getAllStudents_ReturnsList() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
    }

    @Test
    void getStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getStudentById_NotFound_ThrowsResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void updateStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateStudent(1L, dto);

        assertNotNull(result);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void deleteStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudent_NotFound_ThrowsResourceNotFoundException() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(99L));
    }
}
