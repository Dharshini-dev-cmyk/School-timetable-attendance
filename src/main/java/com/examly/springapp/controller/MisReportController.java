package com.examly.springapp.controller;

import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.Student;
import com.examly.springapp.service.MisReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "MIS Reports", description = "Government compliance and school management reports")
@RestController
@RequestMapping("/api/mis")
public class MisReportController {

    private final MisReportService misReportService;

    public MisReportController(MisReportService misReportService) {
        this.misReportService = misReportService;
    }

    @Operation(summary = "Daily attendance register for a given date")
    @GetMapping("/daily-attendance")
    public ResponseEntity<List<Attendance>> getDailyAttendanceRegister(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(misReportService.getDailyAttendanceRegister(date));
    }

    @Operation(summary = "Below-75% student list with details for regulatory inspection")
    @GetMapping("/below75-students")
    public ResponseEntity<List<Student>> getBelow75StudentList() {
        return ResponseEntity.ok(misReportService.getBelow75StudentList());
    }

    @Operation(summary = "Annual attendance summary per student")
    @GetMapping("/annual-attendance-summary")
    public ResponseEntity<List<Map<String, Object>>> getAnnualAttendanceSummary() {
        return ResponseEntity.ok(misReportService.getAnnualAttendanceSummary());
    }

    @Operation(summary = "Teacher deployment report — period utilisation")
    @GetMapping("/teacher-deployment")
    public ResponseEntity<List<Map<String, Object>>> getTeacherDeploymentReport() {
        return ResponseEntity.ok(misReportService.getTeacherDeploymentReport());
    }

    @Operation(summary = "Annual leave summary per teacher for HR records")
    @GetMapping("/leave-summary/{teacherId}")
    public ResponseEntity<List<LeaveApplication>> getAnnualLeaveSummary(@PathVariable Long teacherId) {
        return ResponseEntity.ok(misReportService.getAnnualLeaveSummary(teacherId));
    }
}
