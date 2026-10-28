package com.examly.springapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    public enum Status { PRESENT, ABSENT, LATE, EXCUSED }
    public enum CaptureMethod { BIOMETRIC, MANUAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    private CaptureMethod capturedBy;

    @Column(columnDefinition = "TEXT")
    private String modificationReason;

    @Column(name = "modified_at")
    private java.time.LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    public Attendance() {}

    public Attendance(LocalDate date, Status status, CaptureMethod capturedBy, Student student, User recordedBy) {
        this.date = date;
        this.status = status;
        this.capturedBy = capturedBy;
        this.student = student;
        this.recordedBy = recordedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public CaptureMethod getCapturedBy() { return capturedBy; }
    public void setCapturedBy(CaptureMethod capturedBy) { this.capturedBy = capturedBy; }

    public String getModificationReason() { return modificationReason; }
    public void setModificationReason(String modificationReason) { this.modificationReason = modificationReason; }

    public java.time.LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(java.time.LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }
}
