package com.examly.springapp.controller;

import com.examly.springapp.dto.HolidayRequestDTO;
import com.examly.springapp.entity.Holiday;
import com.examly.springapp.entity.Holiday.HolidayType;
import com.examly.springapp.service.HolidayService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidayController.class)
@AutoConfigureMockMvc(addFilters = false)
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private Holiday holiday;
    private HolidayRequestDTO dto;

    @BeforeEach
    void setUp() {
        holiday = new Holiday("Republic Day", LocalDate.of(2025, 1, 26), HolidayType.NATIONAL, "National holiday", 2025);
        holiday.setId(1L);

        dto = new HolidayRequestDTO();
        dto.setName("Republic Day");
        dto.setDate(LocalDate.of(2025, 1, 26));
        dto.setHolidayType("NATIONAL");
        dto.setReason("National holiday");
        dto.setAcademicYear(2025);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addHoliday_ValidRequest_Returns201() throws Exception {
        when(holidayService.addHoliday(any(HolidayRequestDTO.class))).thenReturn(holiday);

        mockMvc.perform(post("/api/holidays")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getAllHolidays_Returns200() throws Exception {
        when(holidayService.getAllHolidays()).thenReturn(List.of(holiday));

        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getHolidaysByYear_Returns200() throws Exception {
        when(holidayService.getHolidaysByYear(2025)).thenReturn(List.of(holiday));

        mockMvc.perform(get("/api/holidays/year/2025"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void getHolidayById_Returns200() throws Exception {
        when(holidayService.getHolidayById(1L)).thenReturn(holiday);

        mockMvc.perform(get("/api/holidays/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteHoliday_Returns204() throws Exception {
        doNothing().when(holidayService).deleteHoliday(1L);

        mockMvc.perform(delete("/api/holidays/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "PRINCIPAL")
    void countWorkingDays_Returns200() throws Exception {
        when(holidayService.countWorkingDays(any(LocalDate.class), any(LocalDate.class), anyInt())).thenReturn(20L);

        mockMvc.perform(get("/api/holidays/working-days")
                        .param("from", "2025-01-01")
                        .param("to", "2025-01-31")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workingDays").value(20));
    }
}
