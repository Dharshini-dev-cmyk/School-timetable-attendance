package com.examly.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_section", length = 50)
    private String classSection;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registerNumber;

    @NotBlank
    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private int year;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Student() {}

    public Student(String registerNumber, String department, int year, User user) {
        this.registerNumber = registerNumber;
        this.department = department;
        this.year = year;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

            public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
