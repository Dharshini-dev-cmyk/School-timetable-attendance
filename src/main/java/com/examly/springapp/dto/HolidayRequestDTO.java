package com.examly.springapp.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for creating a holiday")
public class HolidayRequestDTO {

    @NotBlank(message = "Holiday name is required")
    @Schema(description = "Name of the holiday", example = "Independence Day")
    private String name;

    @NotNull(message = "Date is required")
    @Schema(description = "Date of the holiday (YYYY-MM-DD)", example = "2026-08-15")
    private LocalDate date;

    @NotBlank(message = "Holiday type is required")
    @Pattern(regexp = "NATIONAL|REGIONAL|SCHOOL|EXAM",
            message = "Holiday type must be one of: NATIONAL, REGIONAL, SCHOOL, EXAM")
    @Schema(description = "Type of holiday", example = "NATIONAL",
            allowableValues = "NATIONAL, REGIONAL, SCHOOL, EXAM")
    private String holidayType;

    @Schema(description = "Reason or description for the holiday", example = "Republic Day celebration")
    private String reason;

    @NotNull(message = "Academic year is required")
    @Schema(description = "Academic year this holiday belongs to", example = "2026")
    private Integer academicYear;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getHolidayType() { return holidayType; }
    public void setHolidayType(String holidayType) { this.holidayType = holidayType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
}
