package com.examly.springapp.controller;

import com.examly.springapp.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@Tag(name = "Analytics", description = "Attendance trends, workload, and early warning dashboard")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
    @Operation(summary = "Students below 75% attendance — early warning list")
    @GetMapping("/at-risk-students")
    public ResponseEntity<List<Long>> getAtRiskStudents() {
        return ResponseEntity.ok(analyticsService.getAtRiskStudents());
    }
    @Operation(summary = "Chronic absentees — students with 10 or more absences")
    @GetMapping("/chronic-absentees")
    public ResponseEntity<List<Long>> getChronicAbsentees() {
        return ResponseEntity.ok(analyticsService.getChronicAbsentees());
    }
    @Operation(summary = "Teacher leave utilisation — approved leave count per teacher")
    @GetMapping("/teacher-leave-utilisation")
    public ResponseEntity<List<Map<String, Object>>> getTeacherLeaveUtilisation() {
        return ResponseEntity.ok(analyticsService.getTeacherLeaveUtilisation());
    }
    @Operation(summary = "Substitute burden report — most frequently assigned substitutes")
    @GetMapping("/substitute-burden")
    public ResponseEntity<List<Map<String, Object>>> getSubstituteBurden() {
        return ResponseEntity.ok(analyticsService.getSubstituteBurden());
    }
    @Operation(summary = "Teacher workload — timetabled period count per teacher")
    @GetMapping("/teacher-workload")
    public ResponseEntity<List<Map<String, Object>>> getTeacherWorkload() {
        return ResponseEntity.ok(analyticsService.getTeacherWorkload());
    }
}
