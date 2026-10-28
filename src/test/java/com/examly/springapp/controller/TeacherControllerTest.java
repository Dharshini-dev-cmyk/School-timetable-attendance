package com.examly.springapp.controller;

import com.examly.springapp.dto.TeacherRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.User;
import com.examly.springapp.service.TeacherService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private Teacher teacher;
    private TeacherRequestDTO dto;

    @BeforeEach
    void setUp() {
        User user = new User("Jane Teacher", "jane@school.com", "pass", "TEACHER", "9876543210");
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
    @WithMockUser(roles = "ADMIN")
    void createTeacher_ValidRequest_Returns201() throws Exception {
        when(teacherService.createTeacher(any(TeacherRequestDTO.class))).thenReturn(teacher);

        mockMvc.perform(post("/api/teachers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTeachers_Returns200() throws Exception {
        when(teacherService.getAllTeachers()).thenReturn(List.of(teacher));

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getTeacherById_Returns200() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getTeacherByEmployeeId_Returns200() throws Exception {
        when(teacherService.getTeacherByEmployeeId("EMP001")).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/employee/EMP001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTeacher_Returns200() throws Exception {
        when(teacherService.updateTeacher(anyLong(), any(TeacherRequestDTO.class))).thenReturn(teacher);

        mockMvc.perform(put("/api/teachers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTeacher_Returns204() throws Exception {
        doNothing().when(teacherService).deleteTeacher(1L);

        mockMvc.perform(delete("/api/teachers/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
