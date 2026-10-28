package com.examly.springapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "academic_calendar")
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int academicYear;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate term1Start;

    @NotNull
    @Column(nullable = false)
    private LocalDate term1End;

    @NotNull
    @Column(nullable = false)
    private LocalDate term2Start;

    @NotNull
    @Column(nullable = false)
    private LocalDate term2End;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    public AcademicCalendar() {}

    public AcademicCalendar(int academicYear, LocalDate startDate, LocalDate endDate,
                            LocalDate term1Start, LocalDate term1End,
                            LocalDate term2Start, LocalDate term2End) {
        this.academicYear = academicYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.term1Start = term1Start;
        this.term1End = term1End;
        this.term2Start = term2Start;
        this.term2End = term2End;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
