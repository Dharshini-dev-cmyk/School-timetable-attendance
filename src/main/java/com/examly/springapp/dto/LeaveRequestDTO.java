package com.examly.springapp.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for applying for leave")
public class LeaveRequestDTO {

    @NotBlank(message = "Leave type is required")
    @Pattern(regexp = "CL|SL|PL|COMP_OFF|ON_DUTY",
            message = "Leave type must be one of: CL, SL, PL, COMP_OFF, ON_DUTY")
    @Schema(description = "Type of leave", example = "CL",
            allowableValues = "CL, SL, PL, COMP_OFF, ON_DUTY")
    private String leaveType;

    @NotNull(message = "From date is required")
    @Schema(description = "Start date of leave (YYYY-MM-DD)", example = "2026-08-10")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    @Schema(description = "End date of leave (YYYY-MM-DD)", example = "2026-08-12")
    private LocalDate toDate;

    @NotBlank(message = "Reason is required")
    @Schema(description = "Reason for the leave application", example = "Personal emergency")
    private String reason;

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
