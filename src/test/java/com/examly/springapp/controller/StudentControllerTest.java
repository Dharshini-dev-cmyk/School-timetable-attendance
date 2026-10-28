package com.examly.springapp.controller;

import com.examly.springapp.dto.StudentRequestDTO;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.service.StudentService;
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

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private Student student;
    private StudentRequestDTO dto;

    @BeforeEach
    void setUp() {
        User user = new User("John Student", "john@school.com", "pass", "STUDENT", "9876543210");
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
    @WithMockUser(roles = "ADMIN")
    void createStudent_ValidRequest_Returns201() throws Exception {
        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllStudents_Returns200() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getStudentById_Returns200() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getStudentByRegisterNumber_Returns200() throws Exception {
        when(studentService.getStudentByRegisterNumber("REG001")).thenReturn(student);

        mockMvc.perform(get("/api/students/register/REG001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStudent_Returns200() throws Exception {
        when(studentService.updateStudent(anyLong(), any(StudentRequestDTO.class))).thenReturn(student);

        mockMvc.perform(put("/api/students/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent_Returns204() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
