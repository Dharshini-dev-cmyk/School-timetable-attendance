package com.examly.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Represents a user account in the school system")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must not contain numbers or special characters")
    @Column(nullable = false)
    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @Email(message = "Please enter a valid email address")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email address (unique)", example = "john.doe@school.com")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    @Schema(description = "Encrypted password (never returned in API responses)", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "User role", example = "STUDENT",
            allowableValues = {"STUDENT", "TEACHER", "PARENT", "CLASS_TEACHER", "PRINCIPAL", "ADMIN"})
    private String role;

    @Pattern(regexp = "^\\d{10}$", message = "Phone Number must be exactly 10 digits long")
    @Column(length = 10)
    @Schema(description = "10-digit phone number", example = "9876543210")
    private String phone;

    public User() {}

    public User(String name, String email, String password, String role, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
