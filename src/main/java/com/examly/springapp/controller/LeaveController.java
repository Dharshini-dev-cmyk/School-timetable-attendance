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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.dto.LeaveApproveRequestDTO;
import com.examly.springapp.dto.LeaveRejectRequestDTO;
import com.examly.springapp.dto.LeaveRequestDTO;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.service.LeaveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Leave", description = "Leave application, approval, and rejection")
@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @Operation(summary = "Apply for leave")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Leave application submitted successfully",
            content = @Content(schema = @Schema(implementation = LeaveApplication.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PostMapping
    public ResponseEntity<LeaveApplication> applyLeave(
            @Parameter(description = "ID of the teacher applying for leave", example = "1")
            @RequestParam Long teacherId,
            @Valid @RequestBody LeaveRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.applyLeave(teacherId, dto));
    }

    @Operation(summary = "Get all pending leave applications")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of pending leave applications",
            content = @Content(schema = @Schema(implementation = LeaveApplication.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — PRINCIPAL or ADMIN role required")
    })
    @GetMapping("/pending")
    public ResponseEntity<List<LeaveApplication>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }

    @Operation(summary = "Approve a leave application")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Leave application approved successfully",
            content = @Content(schema = @Schema(implementation = LeaveApplication.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "Leave application, user, or substitute teacher not found")
    })
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveApplication> approveLeave(
            @Parameter(description = "ID of the leave application to approve", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody LeaveApproveRequestDTO dto) {
        return ResponseEntity.ok(leaveService.approveLeave(id, dto.getApprovedByUserId(), dto.getSubstituteTeacherId()));
    }

    @Operation(summary = "Reject a leave application")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Leave application rejected successfully",
            content = @Content(schema = @Schema(implementation = LeaveApplication.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "Leave application or user not found")
    })
    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveApplication> rejectLeave(
            @Parameter(description = "ID of the leave application to reject", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody LeaveRejectRequestDTO dto) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, dto.getRejectedByUserId()));
    }

    @Operation(summary = "Get leave applications for a specific teacher")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of leave applications for the teacher",
            content = @Content(schema = @Schema(implementation = LeaveApplication.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — authentication required")
    })
    @GetMapping("/balance/{teacherId}")
    public ResponseEntity<List<LeaveApplication>> getTeacherLeaves(
            @Parameter(description = "ID of the teacher", example = "1")
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(leaveService.getTeacherLeaves(teacherId));
    }
}
