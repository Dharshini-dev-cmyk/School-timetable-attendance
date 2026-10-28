package com.examly.springapp.controller;

import com.examly.springapp.dto.AttendanceOverrideRequestDTO;
import com.examly.springapp.dto.AttendanceRequestDTO;
import com.examly.springapp.entity.Attendance;
import com.examly.springapp.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Attendance", description = "Attendance marking, override, and reporting")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Operation(summary = "Submit attendance record")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Attendance recorded successfully",
            content = @Content(schema = @Schema(implementation = Attendance.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "Student or user not found")
    })
    @PostMapping
    public ResponseEntity<Attendance> markAttendance(
            @Valid @RequestBody AttendanceRequestDTO dto,
            @Parameter(description = "ID of the user recording the attendance", example = "1")
            @RequestParam Long recordedByUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.markAttendance(dto, recordedByUserId));
    }

    @Operation(summary = "Get class attendance for a specific date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance records retrieved",
            content = @Content(schema = @Schema(implementation = Attendance.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required")
    })
    @GetMapping("/class/{id}/date/{date}")
    public ResponseEntity<List<Attendance>> getClassAttendanceByDate(
            @Parameter(description = "ID of the class (class section ID)", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Date in YYYY-MM-DD format", example = "2026-07-30")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getClassAttendanceByDate(date));
    }

    @Operation(summary = "Get student attendance history (scoped for STUDENT role)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance history retrieved",
            content = @Content(schema = @Schema(implementation = Attendance.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — students can only view their own attendance"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/student/{id}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(
            @Parameter(description = "ID of the student", example = "1")
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return ResponseEntity.ok(attendanceService.getStudentAttendance(id, email, role));
    }

    @Operation(summary = "Override attendance with mandatory reason")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance overridden successfully",
            content = @Content(schema = @Schema(implementation = Attendance.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or reason is missing"),
        @ApiResponse(responseCode = "404", description = "Attendance record not found")
    })
    @PutMapping("/{id}/override")
    public ResponseEntity<Attendance> overrideAttendance(
            @Parameter(description = "ID of the attendance record to override", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AttendanceOverrideRequestDTO dto) {
        return ResponseEntity.ok(attendanceService.overrideAttendance(id, dto.getStatus(), dto.getReason()));
    }

    @Operation(summary = "List students below 75% attendance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of student IDs below 75% attendance"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping("/below75")
    public ResponseEntity<List<Long>> getStudentsBelow75() {
        return ResponseEntity.ok(attendanceService.getStudentsBelow75Percent());
    }
}
