package com.examly.springapp.service;

import com.examly.springapp.dto.AuthResponseDTO;
import com.examly.springapp.dto.LoginRequestDTO;
import com.examly.springapp.dto.RegisterRequestDTO;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.AuthenticationFailedException;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.UnauthorisedAccessException;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.security.JwtUtils;
import com.examly.springapp.security.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final LoginAttemptService loginAttemptService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.loginAttemptService = loginAttemptService;
    }

    public User register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateStudentException("This email is already registered");
        }
        User user = new User(
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getRole(),
                dto.getPhone()
        );
        return userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        // --- TEMPORARY DEBUG LOGS ---
        logger.debug("[LOGIN DEBUG] Email received: {}", dto.getEmail());
        logger.debug("[LOGIN DEBUG] Password received (length): {}", dto.getPassword() != null ? dto.getPassword().length() : "null");

        if (loginAttemptService.isLocked(dto.getEmail())) {
            throw new UnauthorisedAccessException(
                    "Account temporarily locked due to too many failed attempts. Try again after " +
                    loginAttemptService.getLockExpiry(dto.getEmail()));
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    logger.debug("[LOGIN DEBUG] User found: false");
                    loginAttemptService.recordFailure(dto.getEmail());
                    return new AuthenticationFailedException("Invalid credentials. Please check your email and password.");
                });

        logger.debug("[LOGIN DEBUG] User found: true");
        logger.debug("[LOGIN DEBUG] Stored password hash: {}", user.getPassword());
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        logger.debug("[LOGIN DEBUG] passwordEncoder.matches() result: {}", matches);

        if (!matches) {
            loginAttemptService.recordFailure(dto.getEmail());
            throw new AuthenticationFailedException("Invalid credentials. Please check your email and password.");
        }

        loginAttemptService.recordSuccess(dto.getEmail());
        String token = jwtUtils.generateTokenWithUserId(user.getEmail(), user.getRole(), user.getId());
        return new AuthResponseDTO(token, user.getRole(), user.getEmail(), user.getName());
    }
}
