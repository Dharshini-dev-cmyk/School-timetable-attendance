package com.examly.springapp.controller;

import com.examly.springapp.dto.TimetableRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.Timetable;
import com.examly.springapp.entity.Timetable.Day;
import com.examly.springapp.service.TimetableService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimetableController.class)
@AutoConfigureMockMvc(addFilters = false)
class TimetableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TimetableService timetableService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private Timetable timetable;
    private TimetableRequestDTO dto;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher("EMP001", "Senior Teacher", "Mathematics", null);
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
    @WithMockUser(roles = "PRINCIPAL")
    void createSlot_ValidRequest_Returns201() throws Exception {
        when(timetableService.createSlot(any(TimetableRequestDTO.class))).thenReturn(timetable);

        mockMvc.perform(post("/api/timetable/generate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getClassTimetable_Returns200() throws Exception {
        when(timetableService.getClassTimetable("10A")).thenReturn(List.of(timetable));

        mockMvc.perform(get("/api/timetable/class/10A"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getTeacherTimetable_Returns200() throws Exception {
        when(timetableService.getTeacherTimetable(1L)).thenReturn(List.of(timetable));

        mockMvc.perform(get("/api/timetable/teacher/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getClashes_Returns200() throws Exception {
        when(timetableService.detectClashes()).thenReturn(List.of());

        mockMvc.perform(get("/api/timetable/clashes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void updateSlot_ValidRequest_Returns200() throws Exception {
        when(timetableService.updateSlot(anyLong(), any(TimetableRequestDTO.class))).thenReturn(timetable);

        mockMvc.perform(put("/api/timetable/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void suggestSubstitutes_Returns200() throws Exception {
        when(timetableService.suggestSubstitutes(1L, "MON", 1)).thenReturn(List.of());

        mockMvc.perform(get("/api/timetable/substitutes")
                        .param("absentTeacherId", "1")
                        .param("day", "MON")
                        .param("period", "1"))
                .andExpect(status().isOk());
    }
}
