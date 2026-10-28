package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for creating or updating a timetable slot")
public class TimetableRequestDTO {

    @NotBlank(message = "Class section is required")
    @Schema(description = "Class section identifier", example = "10-A")
    private String classSection;

    @NotBlank(message = "Day is required")
    @Pattern(regexp = "MON|TUE|WED|THU|FRI|SAT",
            message = "Day must be one of: MON, TUE, WED, THU, FRI, SAT")
    @Schema(description = "Day of the week", example = "MON",
            allowableValues = "MON, TUE, WED, THU, FRI, SAT")
    private String day;

    @Min(value = 1, message = "Period must be between 1 and 8")
    @Max(value = 8, message = "Period must be between 1 and 8")
    @Schema(description = "Period number (1-8)", example = "1", minimum = "1", maximum = "8")
    private int period;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Subject name", example = "Database Systems")
    private String subject;

    @Schema(description = "Classroom/room name", example = "Room 101")
    private String classroom;

    @NotNull(message = "Teacher ID is required")
    @Schema(description = "ID of the teacher assigned to this slot", example = "1")
    private Long teacherId;

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
