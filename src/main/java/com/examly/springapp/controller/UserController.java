package com.examly.springapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.entity.User;
import com.examly.springapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "User profile and management")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current logged-in user profile from JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User profile retrieved",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfileByEmail(authentication.getName()));
    }

    @Operation(summary = "Get user by ID (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID of the user to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all users",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Delete user (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
