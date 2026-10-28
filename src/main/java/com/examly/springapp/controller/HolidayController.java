package com.examly.springapp.controller;

import com.examly.springapp.dto.HolidayRequestDTO;
import com.examly.springapp.entity.Holiday;
import com.examly.springapp.service.HolidayService;
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
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Holiday", description = "Holiday management and working-day calculations")
@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @Operation(summary = "Add a holiday (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Holiday added successfully",
            content = @Content(schema = @Schema(implementation = Holiday.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "409", description = "A holiday already exists for this date")
    })
    @PostMapping
    public ResponseEntity<Holiday> addHoliday(@Valid @RequestBody HolidayRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(holidayService.addHoliday(dto));
    }

    @Operation(summary = "Get all holidays")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of all holidays",
            content = @Content(schema = @Schema(implementation = Holiday.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }

    @Operation(summary = "Get holidays by academic year")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of holidays for the given year",
            content = @Content(schema = @Schema(implementation = Holiday.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping("/year/{year}")
    public ResponseEntity<List<Holiday>> getHolidaysByYear(
            @Parameter(description = "Academic year to filter by", example = "2026")
            @PathVariable int year) {
        return ResponseEntity.ok(holidayService.getHolidaysByYear(year));
    }

    @Operation(summary = "Get holiday by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Holiday found",
            content = @Content(schema = @Schema(implementation = Holiday.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Holiday> getHolidayById(
            @Parameter(description = "ID of the holiday to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(holidayService.getHolidayById(id));
    }

    @Operation(summary = "Delete holiday (Admin only)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Holiday deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHoliday(
            @Parameter(description = "ID of the holiday to delete", example = "1")
            @PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count working days excluding holidays")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Working day count",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Invalid date range or year"),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping("/working-days")
    public ResponseEntity<Map<String, Long>> countWorkingDays(
            @Parameter(description = "Start date in YYYY-MM-DD format", example = "2026-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date in YYYY-MM-DD format", example = "2026-07-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Academic year to filter holidays", example = "2026")
            @RequestParam int year) {
        long workingDays = holidayService.countWorkingDays(from, to, year);
        return ResponseEntity.ok(Map.of("workingDays", workingDays));
    }
}
