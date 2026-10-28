package com.examly.springapp.controller;

import com.examly.springapp.entity.User;
import com.examly.springapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private com.examly.springapp.security.JwtUtils jwtUtils;

    @MockBean
    private com.examly.springapp.security.JwtAuthFilter jwtAuthFilter;

    private User user;

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

        user = new User("Admin User", "admin@school.com", "pass", "ADMIN", "9876543210");
        user.setId(1L);
    }

    @Test
    @WithMockUser(username = "admin@school.com", roles = "ADMIN")
    void getProfile_Returns200() throws Exception {
        when(userService.getProfileByEmail("admin@school.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@school.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_Returns200() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_Returns200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_Returns204() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
