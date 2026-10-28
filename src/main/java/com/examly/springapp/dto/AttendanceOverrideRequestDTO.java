package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for overriding an existing attendance record")
public class AttendanceOverrideRequestDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PRESENT|ABSENT|LATE|EXCUSED",
            message = "Status must be one of: PRESENT, ABSENT, LATE, EXCUSED")
    @Schema(description = "New attendance status", example = "PRESENT",
            allowableValues = "PRESENT, ABSENT, LATE, EXCUSED")
    private String status;

    @NotBlank(message = "Reason is required for attendance modification")
    @Schema(description = "Mandatory reason for the override", example = "Medical certificate provided")
    private String reason;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
