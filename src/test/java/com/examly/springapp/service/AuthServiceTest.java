package com.examly.springapp.service;

import com.examly.springapp.dto.LoginRequestDTO;
import com.examly.springapp.dto.RegisterRequestDTO;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.AuthenticationFailedException;
import com.examly.springapp.exception.UnauthorisedAccessException;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.security.JwtUtils;
import com.examly.springapp.security.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerDTO;
    private LoginRequestDTO loginDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterRequestDTO();
        registerDTO.setName("John Doe");
        registerDTO.setEmail("john@school.com");
        registerDTO.setPassword("Pass@1234");
        registerDTO.setRole("TEACHER");
        registerDTO.setPhone("9876543210");

        loginDTO = new LoginRequestDTO();
        loginDTO.setEmail("john@school.com");
        loginDTO.setPassword("Pass@1234");

        user = new User("John Doe", "john@school.com", "encodedPass", "TEACHER", "9876543210");
        user.setId(1L);
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authService.register(registerDTO);

        assertNotNull(result);
        assertEquals("john@school.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsDuplicateStudentException() {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateStudentException.class, () -> authService.register(registerDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_Success() {
        when(loginAttemptService.isLocked(loginDTO.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateTokenWithUserId(anyString(), anyString(), anyLong())).thenReturn("jwt-token");

        var result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals("TEACHER", result.getRole());
        verify(loginAttemptService).recordSuccess(loginDTO.getEmail());
    }

    @Test
    void login_AccountLocked_ThrowsUnauthorisedAccessException() {
        when(loginAttemptService.isLocked(loginDTO.getEmail())).thenReturn(true);

        assertThrows(UnauthorisedAccessException.class, () -> authService.login(loginDTO));
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void login_InvalidEmail_ThrowsAuthenticationFailedException() {
        when(loginAttemptService.isLocked(loginDTO.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> authService.login(loginDTO));
        verify(loginAttemptService).recordFailure(loginDTO.getEmail());
    }

    @Test
    void login_WrongPassword_ThrowsAuthenticationFailedException() {
        when(loginAttemptService.isLocked(loginDTO.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationFailedException.class, () -> authService.login(loginDTO));
        verify(loginAttemptService).recordFailure(loginDTO.getEmail());
    }
}
