package com.examly.springapp.controller;

import com.examly.springapp.dto.AuthResponseDTO;
import com.examly.springapp.dto.LoginRequestDTO;
import com.examly.springapp.dto.RegisterRequestDTO;
import com.examly.springapp.entity.User;
import com.examly.springapp.security.JwtUtils;
import com.examly.springapp.security.TokenBlacklistService;
import com.examly.springapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "Authentication", description = "User registration, login, and logout")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService blacklistService;

    public AuthController(AuthService authService, JwtUtils jwtUtils, TokenBlacklistService blacklistService) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.blacklistService = blacklistService;
    }

    @Operation(summary = "Register a new user account")
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @Operation(summary = "Authenticate and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(summary = "Invalidate current JWT token (logout)")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof String token) {
            blacklistService.blacklist(token, jwtUtils.getExpirationFromToken(token));
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully. Please discard your token."));
    }
}
