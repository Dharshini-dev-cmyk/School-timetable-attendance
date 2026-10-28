package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating or updating a teacher")
public class TeacherRequestDTO {

    @NotBlank(message = "Employee ID is required")
    @Schema(description = "Unique employee ID for the teacher", example = "EMP001")
    private String employeeId;

    @NotBlank(message = "Designation is required")
    @Schema(description = "Teacher's designation", example = "Senior Lecturer")
    private String designation;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Subject taught by the teacher", example = "Database Systems")
    private String subject;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the associated user account", example = "1")
    private Long userId;

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
