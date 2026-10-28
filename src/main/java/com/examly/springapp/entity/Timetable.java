package com.examly.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
    name = "timetable",
    uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "day", "period"})
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Timetable {

    public enum Day { MON, TUE, WED, THU, FRI, SAT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String classSection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Min(value = 1, message = "Period must be between 1 and 8")
    @Max(value = 8, message = "Period must be between 1 and 8")
    @Column(nullable = false)
    private int period;

    @NotBlank
    @Column(nullable = false)
    private String subject;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "classroom")
    private String classroom;

    @Column(name = "effective_from")
    private java.time.LocalDate effectiveFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Teacher teacher;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    public Timetable() {}

    public Timetable(String classSection, Day day, int period, String subject, Teacher teacher) {
        this.classSection = classSection;
        this.day = day;
        this.period = period;
        this.subject = subject;
        this.teacher = teacher;
    }

    public Timetable(String classSection, Day day, int period, String subject, String classroom, Teacher teacher) {
        this.classSection = classSection;
        this.day = day;
        this.period = period;
        this.subject = subject;
        this.classroom = classroom;
        this.teacher = teacher;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public Day getDay() { return day; }
    public void setDay(Day day) { this.day = day; }

    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public java.time.LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(java.time.LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
