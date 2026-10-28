package com.examly.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "leave_applications")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LeaveApplication {

    public enum LeaveType { CL, SL, PL, COMP_OFF, ON_DUTY }
    public enum LeaveStatus { PENDING, APPROVED, REJECTED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @NotNull
    @Column(nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate toDate;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_teacher_id")
    private Teacher substituteTeacher;

    public LeaveApplication() {}

    public LeaveApplication(LeaveType leaveType, LocalDate fromDate, LocalDate toDate, String reason, Teacher teacher) {
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.teacher = teacher;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LeaveType getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public User getApprovedBy() { return approvedBy; }
    public void setApprovedBy(User approvedBy) { this.approvedBy = approvedBy; }

    public Teacher getSubstituteTeacher() { return substituteTeacher; }
    public void setSubstituteTeacher(Teacher substituteTeacher) { this.substituteTeacher = substituteTeacher; }
}
