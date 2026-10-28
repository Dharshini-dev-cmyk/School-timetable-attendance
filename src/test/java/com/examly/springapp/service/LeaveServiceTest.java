package com.examly.springapp.service;

import com.examly.springapp.dto.LeaveRequestDTO;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.LeaveApplication.LeaveStatus;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.LeaveApplicationRepository;
import com.examly.springapp.repository.TeacherRepository;
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
class LeaveServiceTest {

    @Mock
    private LeaveApplicationRepository leaveRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaveService leaveService;

    private Teacher teacher;
    private Teacher substitute;
    private User principal;
    private LeaveApplication leave;
    private LeaveRequestDTO dto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("EMP001", "Teacher", "Maths", null);
        teacher.setId(1L);

        substitute = new Teacher("EMP002", "Teacher", "Science", null);
        substitute.setId(2L);

        principal = new User("Principal", "principal@school.com", "pass", "PRINCIPAL", "9876543210");
        principal.setId(3L);

        leave = new LeaveApplication(
                LeaveApplication.LeaveType.CL,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                "Personal work",
                teacher
        );
        leave.setId(1L);

        dto = new LeaveRequestDTO();
        dto.setLeaveType("CL");
        dto.setFromDate(LocalDate.now().plusDays(1));
        dto.setToDate(LocalDate.now().plusDays(2));
        dto.setReason("Personal work");
    }

    @Test
    void applyLeave_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(leaveRepository.save(any(LeaveApplication.class))).thenReturn(leave);

        LeaveApplication result = leaveService.applyLeave(1L, dto);

        assertNotNull(result);
        assertEquals(LeaveStatus.PENDING, result.getStatus());
        verify(leaveRepository).save(any(LeaveApplication.class));
    }

    @Test
    void applyLeave_TeacherNotFound_ThrowsResourceNotFoundException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leaveService.applyLeave(1L, dto));
    }

    @Test
    void getPendingLeaves_ReturnsList() {
        when(leaveRepository.findByStatus(LeaveStatus.PENDING)).thenReturn(List.of(leave));

        List<LeaveApplication> result = leaveService.getPendingLeaves();

        assertEquals(1, result.size());
        assertEquals(LeaveStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void approveLeave_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));
        when(userRepository.findById(3L)).thenReturn(Optional.of(principal));
        when(teacherRepository.findById(2L)).thenReturn(Optional.of(substitute));
        when(leaveRepository.save(any(LeaveApplication.class))).thenReturn(leave);

        LeaveApplication result = leaveService.approveLeave(1L, 3L, 2L);

        assertNotNull(result);
        verify(leaveRepository).save(any(LeaveApplication.class));
    }

    @Test
    void rejectLeave_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));
        when(userRepository.findById(3L)).thenReturn(Optional.of(principal));
        when(leaveRepository.save(any(LeaveApplication.class))).thenReturn(leave);

        LeaveApplication result = leaveService.rejectLeave(1L, 3L);

        assertNotNull(result);
        verify(leaveRepository).save(any(LeaveApplication.class));
    }

    @Test
    void approveLeave_NotFound_ThrowsResourceNotFoundException() {
        when(leaveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leaveService.approveLeave(99L, 3L, 2L));
    }
}
