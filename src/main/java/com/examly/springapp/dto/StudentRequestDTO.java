package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating or updating a student")
public class StudentRequestDTO {

    @NotBlank(message = "Register number is required")
    @Schema(description = "Unique register number for the student", example = "CS2024001")
    private String registerNumber;

    @NotBlank(message = "Department is required")
    @Schema(description = "Student's department", example = "Computer Science")
    private String department;

    @Min(value = 1, message = "Year must be a positive number")
    @Schema(description = "Academic year of study (1-4)", example = "3", minimum = "1")
    private int year;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the associated user account", example = "1")
    private Long userId;

    public String getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
