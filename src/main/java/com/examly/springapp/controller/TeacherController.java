package com.examly.springapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.dto.TeacherRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Teacher", description = "Teacher registration, retrieval, and management")
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Register a new teacher (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Teacher registered successfully",
            content = @Content(schema = @Schema(implementation = Teacher.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
        @ApiResponse(responseCode = "409", description = "Teacher with employee ID already exists")
    })
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody TeacherRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(dto));
    }

    @Operation(summary = "Get all teachers (Admin/Principal)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all teachers",
            content = @Content(schema = @Schema(implementation = Teacher.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required")
    })
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @Operation(summary = "Get teacher by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Teacher found",
            content = @Content(schema = @Schema(implementation = Teacher.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(
            @Parameter(description = "ID of the teacher to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @Operation(summary = "Get teacher by employee ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Teacher found",
            content = @Content(schema = @Schema(implementation = Teacher.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Teacher> getTeacherByEmployeeId(
            @Parameter(description = "Employee ID of the teacher", example = "EMP001")
            @PathVariable String employeeId) {
        return ResponseEntity.ok(teacherService.getTeacherByEmployeeId(employeeId));
    }

    @Operation(summary = "Update teacher details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Teacher updated successfully",
            content = @Content(schema = @Schema(implementation = Teacher.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @Parameter(description = "ID of the teacher to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequestDTO dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @Operation(summary = "Delete teacher (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Teacher deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(
            @Parameter(description = "ID of the teacher to delete", example = "1")
            @PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
