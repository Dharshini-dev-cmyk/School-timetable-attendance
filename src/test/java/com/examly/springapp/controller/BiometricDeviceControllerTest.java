package com.examly.springapp.controller;

import com.examly.springapp.entity.BiometricDevice;
import com.examly.springapp.entity.BiometricDevice.DeviceStatus;
import com.examly.springapp.entity.BiometricDevice.DeviceType;
import com.examly.springapp.repository.BiometricDeviceRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BiometricDeviceController.class)
@AutoConfigureMockMvc(addFilters = false)
class BiometricDeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BiometricDeviceRepository deviceRepository;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private BiometricDevice device;

    @BeforeEach
    void setUp() {
        device = new BiometricDevice("Gate Scanner", "Main Gate", DeviceType.RFID,
                LocalDateTime.now(), 85, DeviceStatus.ONLINE);
        device.setId(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addDevice_Returns201() throws Exception {
        when(deviceRepository.save(any(BiometricDevice.class))).thenReturn(device);

        mockMvc.perform(post("/api/biometric-devices")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDevices_Returns200() throws Exception {
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        mockMvc.perform(get("/api/biometric-devices"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDeviceById_Returns200() throws Exception {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        mockMvc.perform(get("/api/biometric-devices/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDevicesByStatus_Returns200() throws Exception {
        when(deviceRepository.findByStatus(DeviceStatus.ONLINE)).thenReturn(List.of(device));

        mockMvc.perform(get("/api/biometric-devices/status/ONLINE"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDevice_Returns200() throws Exception {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(BiometricDevice.class))).thenReturn(device);

        mockMvc.perform(put("/api/biometric-devices/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDevice_Returns204() throws Exception {
        when(deviceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deviceRepository).deleteById(1L);

        mockMvc.perform(delete("/api/biometric-devices/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
