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

import com.examly.springapp.dto.StudentRequestDTO;
import com.examly.springapp.entity.Student;
import com.examly.springapp.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Student", description = "Student registration, retrieval, and management")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Register a new student (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student registered successfully",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
        @ApiResponse(responseCode = "409", description = "Student with register number already exists")
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    @Operation(summary = "Get all students (Admin/Principal)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all students",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required")
    })
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(summary = "Get student by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID of the student to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Get student by register number")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/register/{registerNumber}")
    public ResponseEntity<Student> getStudentByRegisterNumber(
            @Parameter(description = "Register number of the student", example = "CS2024001")
            @PathVariable String registerNumber) {
        return ResponseEntity.ok(studentService.getStudentByRegisterNumber(registerNumber));
    }

    @Operation(summary = "Update student details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student updated successfully",
            content = @Content(schema = @Schema(implementation = Student.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID of the student to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @Operation(summary = "Delete student (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID of the student to delete", example = "1")
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
