package com.examly.springapp.controller;

import com.examly.springapp.dto.AttendanceRequestDTO;
import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.Attendance.Status;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.service.AttendanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
@AutoConfigureMockMvc
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private Attendance attendance;

    @BeforeEach
    void setUp() throws Exception {
        // Make the mock JwtAuthFilter pass through the filter chain so security filters remain active
        doAnswer(invocation -> {
            try {
                invocation.getArgument(2, jakarta.servlet.FilterChain.class)
                        .doFilter(invocation.getArgument(0, jakarta.servlet.http.HttpServletRequest.class),
                                  invocation.getArgument(1, jakarta.servlet.http.HttpServletResponse.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());

        Student student = new Student("REG001", "Science", 10, null);
        student.setId(1L);
        User recorder = new User("Teacher", "t@school.com", "pass", "TEACHER", "9876543210");
        recorder.setId(2L);
        attendance = new Attendance(LocalDate.now(), Status.PRESENT, Attendance.CaptureMethod.MANUAL, student, recorder);
        attendance.setId(1L);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void markAttendance_ValidRequest_Returns201() throws Exception {
        AttendanceRequestDTO dto = new AttendanceRequestDTO();
        dto.setStudentId(1L);
        dto.setDate(LocalDate.now());
        dto.setStatus("PRESENT");
        dto.setCapturedBy("MANUAL");

        when(attendanceService.markAttendance(any(AttendanceRequestDTO.class), anyLong()))
                .thenReturn(attendance);

        mockMvc.perform(post("/api/attendance")
                        .with(csrf())
                        .param("recordedByUserId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "teacher@school.com", roles = "TEACHER")
    void getStudentAttendance_Returns200() throws Exception {
        when(attendanceService.getStudentAttendance(anyLong(), anyString(), anyString()))
                .thenReturn(List.of(attendance));

        mockMvc.perform(get("/api/attendance/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getStudentsBelow75_Returns200() throws Exception {
        when(attendanceService.getStudentsBelow75Percent()).thenReturn(List.of(1L, 2L));

        mockMvc.perform(get("/api/attendance/below75"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLASS_TEACHER")
    void overrideAttendance_ValidReason_Returns200() throws Exception {
        when(attendanceService.overrideAttendance(anyLong(), anyString(), anyString()))
                .thenReturn(attendance);

        mockMvc.perform(put("/api/attendance/1/override")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("status", "ABSENT", "reason", "Student was absent"))))
                .andExpect(status().isOk());
    }
}
