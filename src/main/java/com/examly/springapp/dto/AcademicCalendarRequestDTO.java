package com.examly.springapp.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating or updating an academic calendar")
public class AcademicCalendarRequestDTO {

    @NotNull(message = "Academic year is required")
    @Schema(description = "Academic year (e.g., 2026)", example = "2026")
    private Integer academicYear;

    @NotNull(message = "Start date is required")
    @Schema(description = "Academic year start date (YYYY-MM-DD)", example = "2026-07-01")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Schema(description = "Academic year end date (YYYY-MM-DD)", example = "2026-12-31")
    private LocalDate endDate;

    @NotNull(message = "Term 1 start date is required")
    @Schema(description = "Term 1 start date (YYYY-MM-DD)", example = "2026-07-01")
    private LocalDate term1Start;

    @NotNull(message = "Term 1 end date is required")
    @Schema(description = "Term 1 end date (YYYY-MM-DD)", example = "2026-10-31")
    private LocalDate term1End;

    @NotNull(message = "Term 2 start date is required")
    @Schema(description = "Term 2 start date (YYYY-MM-DD)", example = "2026-11-01")
    private LocalDate term2Start;

    @NotNull(message = "Term 2 end date is required")
    @Schema(description = "Term 2 end date (YYYY-MM-DD)", example = "2026-12-31")
    private LocalDate term2End;

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getTerm1Start() { return term1Start; }
    public void setTerm1Start(LocalDate term1Start) { this.term1Start = term1Start; }

    public LocalDate getTerm1End() { return term1End; }
    public void setTerm1End(LocalDate term1End) { this.term1End = term1End; }

    public LocalDate getTerm2Start() { return term2Start; }
    public void setTerm2Start(LocalDate term2Start) { this.term2Start = term2Start; }

    public LocalDate getTerm2End() { return term2End; }
    public void setTerm2End(LocalDate term2End) { this.term2End = term2End; }
}
