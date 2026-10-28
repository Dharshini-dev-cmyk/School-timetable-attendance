package com.examly.springapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.dto.AcademicCalendarRequestDTO;
import com.examly.springapp.entity.AcademicCalendar;
import com.examly.springapp.service.AcademicCalendarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Academic Calendar", description = "Academic calendar management")
@RestController
@RequestMapping("/api/academic-calendar")
public class AcademicCalendarController {

    private final AcademicCalendarService calendarService;

    public AcademicCalendarController(AcademicCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Operation(summary = "Create a new academic calendar")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Academic calendar created successfully",
            content = @Content(schema = @Schema(implementation = AcademicCalendar.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "409", description = "Academic calendar already exists for this year")
    })
    @PostMapping
    public ResponseEntity<AcademicCalendar> createCalendar(@Valid @RequestBody AcademicCalendarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.createCalendar(dto));
    }

    @Operation(summary = "Get all academic calendars")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all academic calendars",
            content = @Content(schema = @Schema(implementation = AcademicCalendar.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping
    public ResponseEntity<List<AcademicCalendar>> getAllCalendars() {
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }

    @Operation(summary = "Get the active academic calendar")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active academic calendar",
            content = @Content(schema = @Schema(implementation = AcademicCalendar.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "No active academic calendar found")
    })
    @GetMapping("/active")
    public ResponseEntity<AcademicCalendar> getActiveCalendar() {
        return ResponseEntity.ok(calendarService.getActiveCalendar());
    }

    @Operation(summary = "Get academic calendar by year")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Academic calendar for the given year",
            content = @Content(schema = @Schema(implementation = AcademicCalendar.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Academic calendar not found for this year")
    })
    @GetMapping("/year/{year}")
    public ResponseEntity<AcademicCalendar> getCalendarByYear(
            @Parameter(description = "Academic year to search for", example = "2026")
            @PathVariable int year) {
        return ResponseEntity.ok(calendarService.getCalendarByYear(year));
    }

    @Operation(summary = "Update an academic calendar")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Academic calendar updated successfully",
            content = @Content(schema = @Schema(implementation = AcademicCalendar.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Academic calendar not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AcademicCalendar> updateCalendar(
            @Parameter(description = "ID of the academic calendar to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AcademicCalendarRequestDTO dto) {
        return ResponseEntity.ok(calendarService.updateCalendar(id, dto));
    }
}
