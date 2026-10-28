package com.examly.springapp.controller;

import com.examly.springapp.dto.AcademicCalendarRequestDTO;
import com.examly.springapp.entity.AcademicCalendar;
import com.examly.springapp.service.AcademicCalendarService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AcademicCalendarController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcademicCalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AcademicCalendarService calendarService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private AcademicCalendar calendar;
    private AcademicCalendarRequestDTO dto;

    @BeforeEach
    void setUp() {
        calendar = new AcademicCalendar(
                2025,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2026, 3, 31),
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 10, 31),
                LocalDate.of(2025, 11, 1),
                LocalDate.of(2026, 3, 31)
        );
        calendar.setId(1L);

        dto = new AcademicCalendarRequestDTO();
        dto.setAcademicYear(2025);
        dto.setStartDate(LocalDate.of(2025, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 3, 31));
        dto.setTerm1Start(LocalDate.of(2025, 6, 1));
        dto.setTerm1End(LocalDate.of(2025, 10, 31));
        dto.setTerm2Start(LocalDate.of(2025, 11, 1));
        dto.setTerm2End(LocalDate.of(2026, 3, 31));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCalendar_ValidRequest_Returns201() throws Exception {
        when(calendarService.createCalendar(any(AcademicCalendarRequestDTO.class))).thenReturn(calendar);

        mockMvc.perform(post("/api/academic-calendar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCalendars_Returns200() throws Exception {
        when(calendarService.getAllCalendars()).thenReturn(List.of(calendar));

        mockMvc.perform(get("/api/academic-calendar"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getActiveCalendar_Returns200() throws Exception {
        when(calendarService.getActiveCalendar()).thenReturn(calendar);

        mockMvc.perform(get("/api/academic-calendar/active"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCalendarByYear_Returns200() throws Exception {
        when(calendarService.getCalendarByYear(2025)).thenReturn(calendar);

        mockMvc.perform(get("/api/academic-calendar/year/2025"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCalendar_Returns200() throws Exception {
        when(calendarService.updateCalendar(anyLong(), any(AcademicCalendarRequestDTO.class))).thenReturn(calendar);

        mockMvc.perform(put("/api/academic-calendar/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
