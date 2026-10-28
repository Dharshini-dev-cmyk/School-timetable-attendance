package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for approving a leave application")
public class LeaveApproveRequestDTO {

    @NotNull(message = "Approved by user ID is required")
    @Schema(description = "ID of the user (principal/admin) approving the leave", example = "1")
    private Long approvedByUserId;

    @NotNull(message = "Substitute teacher ID is required")
    @Schema(description = "ID of the substitute teacher assigned during the leave", example = "2")
    private Long substituteTeacherId;

    public Long getApprovedByUserId() { return approvedByUserId; }
    public void setApprovedByUserId(Long approvedByUserId) { this.approvedByUserId = approvedByUserId; }

    public Long getSubstituteTeacherId() { return substituteTeacherId; }
    public void setSubstituteTeacherId(Long substituteTeacherId) { this.substituteTeacherId = substituteTeacherId; }
}
