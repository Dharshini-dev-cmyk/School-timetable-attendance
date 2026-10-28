package com.examly.springapp.controller;

import com.examly.springapp.dto.LeaveRequestDTO;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.LeaveApplication.LeaveStatus;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.service.LeaveService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveController.class)
@AutoConfigureMockMvc(addFilters = false)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private LeaveApplication leave;
    private LeaveRequestDTO dto;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher("EMP001", "Teacher", "Maths", null);
        teacher.setId(1L);

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
    @WithMockUser(roles = "TEACHER")
    void applyLeave_ValidRequest_Returns201() throws Exception {
        when(leaveService.applyLeave(anyLong(), any(LeaveRequestDTO.class))).thenReturn(leave);

        mockMvc.perform(post("/api/leave")
                        .with(csrf())
                        .param("teacherId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getPendingLeaves_Returns200() throws Exception {
        when(leaveService.getPendingLeaves()).thenReturn(List.of(leave));

        mockMvc.perform(get("/api/leave/pending"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void approveLeave_Returns200() throws Exception {
        when(leaveService.approveLeave(anyLong(), anyLong(), anyLong())).thenReturn(leave);

        mockMvc.perform(put("/api/leave/1/approve")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("approvedByUserId", 3L, "substituteTeacherId", 2L))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void rejectLeave_Returns200() throws Exception {
        when(leaveService.rejectLeave(anyLong(), anyLong())).thenReturn(leave);

        mockMvc.perform(put("/api/leave/1/reject")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("rejectedByUserId", 3L))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getTeacherLeaves_Returns200() throws Exception {
        when(leaveService.getTeacherLeaves(1L)).thenReturn(List.of(leave));

        mockMvc.perform(get("/api/leave/balance/1"))
                .andExpect(status().isOk());
    }
}
