package com.examly.springapp.service;

import com.examly.springapp.dto.AttendanceRequestDTO;
import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.Attendance.Status;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.AttendanceOverrideException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.AttendanceRepository;
import com.examly.springapp.repository.StudentRepository;
import com.examly.springapp.repository.UserRepository;
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
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private Student student;
    private User recorder;
    private Attendance attendance;
    private AttendanceRequestDTO dto;

    @BeforeEach
    void setUp() {
        student = new Student("REG001", "Science", 10, null);
        student.setId(1L);

        recorder = new User("Teacher One", "teacher@school.com", "pass", "TEACHER", "9876543210");
        recorder.setId(2L);

        attendance = new Attendance(LocalDate.now(), Status.PRESENT, Attendance.CaptureMethod.MANUAL, student, recorder);
        attendance.setId(1L);

        dto = new AttendanceRequestDTO();
        dto.setStudentId(1L);
        dto.setDate(LocalDate.now());
        dto.setStatus("PRESENT");
        dto.setCapturedBy("MANUAL");
    }

    @Test
    void markAttendance_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(recorder));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.markAttendance(dto, 2L);

        assertNotNull(result);
        assertEquals(Status.PRESENT, result.getStatus());
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    void markAttendance_StudentNotFound_ThrowsResourceNotFoundException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> attendanceService.markAttendance(dto, 2L));
    }

    @Test
    void overrideAttendance_Success() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.overrideAttendance(1L, "ABSENT", "Student was absent");

        assertNotNull(result);
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    void overrideAttendance_BlankReason_ThrowsAttendanceOverrideException() {
        assertThrows(AttendanceOverrideException.class,
                () -> attendanceService.overrideAttendance(1L, "ABSENT", ""));
    }

    @Test
    void overrideAttendance_NullReason_ThrowsAttendanceOverrideException() {
        assertThrows(AttendanceOverrideException.class,
                () -> attendanceService.overrideAttendance(1L, "ABSENT", null));
    }

    @Test
    void getStudentAttendance_ReturnsList() {
        when(attendanceRepository.findByStudentId(1L)).thenReturn(List.of(attendance));

        List<Attendance> result = attendanceService.getStudentAttendance(1L, "teacher@school.com", "TEACHER");

        assertEquals(1, result.size());
    }

    @Test
    void getStudentsBelow75Percent_ReturnsList() {
        when(attendanceRepository.findStudentIdsBelow75Percent()).thenReturn(List.of(1L, 2L));

        List<Long> result = attendanceService.getStudentsBelow75Percent();

        assertEquals(2, result.size());
    }
}
