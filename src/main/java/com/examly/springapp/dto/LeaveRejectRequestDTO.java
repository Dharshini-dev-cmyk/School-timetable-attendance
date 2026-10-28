package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for rejecting a leave application")
public class LeaveRejectRequestDTO {

    @NotNull(message = "Rejected by user ID is required")
    @Schema(description = "ID of the user (principal/admin) rejecting the leave", example = "1")
    private Long rejectedByUserId;

    public Long getRejectedByUserId() { return rejectedByUserId; }
    public void setRejectedByUserId(Long rejectedByUserId) { this.rejectedByUserId = rejectedByUserId; }
}
