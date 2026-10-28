package com.examly.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "teachers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String employeeId;

    @NotBlank
    @Column(nullable = false)
    private String designation;

    @NotBlank
    @Column(nullable = false)
    private String subject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Teacher() {}

    public Teacher(String employeeId, String designation, String subject, User user) {
        this.employeeId = employeeId;
        this.designation = designation;
        this.subject = subject;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
