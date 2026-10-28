package com.examly.springapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday {

    public enum HolidayType { NATIONAL, REGIONAL, SCHOOL, EXAM }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayType holidayType;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private int academicYear;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean notificationSent = false;

    public Holiday() {}

    public Holiday(String name, LocalDate date, HolidayType holidayType, String reason, int academicYear) {
        this.name = name;
        this.date = date;
        this.holidayType = holidayType;
        this.reason = reason;
        this.academicYear = academicYear;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public HolidayType getHolidayType() { return holidayType; }
    public void setHolidayType(HolidayType holidayType) { this.holidayType = holidayType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }

    public boolean isNotificationSent() { return notificationSent; }
    public void setNotificationSent(boolean notificationSent) { this.notificationSent = notificationSent; }
}
