package com.examly.springapp.controller;

import com.examly.springapp.dto.AuthResponseDTO;
import com.examly.springapp.dto.LoginRequestDTO;
import com.examly.springapp.dto.RegisterRequestDTO;
import com.examly.springapp.entity.User;
import com.examly.springapp.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.TokenBlacklistService tokenBlacklistService;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    @Test
    void register_ValidRequest_Returns201() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@school.com");
        dto.setPassword("Pass@1234");
        dto.setRole("TEACHER");
        dto.setPhone("9876543210");

        User user = new User("John Doe", "john@school.com", "encoded", "TEACHER", "9876543210");
        user.setId(1L);

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@school.com"));
    }

    @Test
    void login_ValidCredentials_Returns200WithToken() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("john@school.com");
        dto.setPassword("Pass@1234");

        AuthResponseDTO response = new AuthResponseDTO("jwt-token", "TEACHER", "john@school.com", "John Doe");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.role").value("TEACHER"));
    }

    @Test
    @WithMockUser
    void logout_AuthenticatedUser_Returns200() throws Exception {
        mockMvc.perform(post("/api/auth/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully. Please discard your token."));
    }
}
