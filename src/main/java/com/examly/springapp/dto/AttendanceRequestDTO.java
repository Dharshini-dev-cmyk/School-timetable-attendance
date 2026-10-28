package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Schema(description = "Request body for submitting an attendance record")
public class AttendanceRequestDTO {

    @NotNull(message = "Student ID is required")
    @Schema(description = "ID of the student whose attendance is being recorded", example = "1")
    private Long studentId;

    @NotNull(message = "Date is required")
    @Schema(description = "Date of the attendance record (YYYY-MM-DD)", example = "2026-08-02")
    private LocalDate date;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PRESENT|ABSENT|LATE|EXCUSED",
            message = "Status must be one of: PRESENT, ABSENT, LATE, EXCUSED")
    @Schema(description = "Attendance status", example = "PRESENT",
            allowableValues = "PRESENT, ABSENT, LATE, EXCUSED")
    private String status;

    @Pattern(regexp = "BIOMETRIC|MANUAL",
            message = "Captured by must be one of: BIOMETRIC, MANUAL")
    @Schema(description = "How the attendance was captured", example = "MANUAL",
            allowableValues = "BIOMETRIC, MANUAL")
    private String capturedBy;

    @Schema(description = "Reason for any modification", example = "Corrected after review")
    private String modificationReason;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCapturedBy() { return capturedBy; }
    public void setCapturedBy(String capturedBy) { this.capturedBy = capturedBy; }

    public String getModificationReason() { return modificationReason; }
    public void setModificationReason(String modificationReason) { this.modificationReason = modificationReason; }
}
