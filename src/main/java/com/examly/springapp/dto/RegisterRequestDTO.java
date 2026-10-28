package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for user registration")
public class RegisterRequestDTO {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must not contain numbers or special characters")
    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Schema(description = "Email address (must be unique)", example = "john.doe@school.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must meet security requirements")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must meet security requirements"
    )
    @Schema(description = "Password (min 8 chars, must include upper, lower, digit, and special char)",
            example = "Secure@123", minLength = 8)
    private String password;

    @NotBlank(message = "Role is required")
    @Schema(description = "User role", example = "STUDENT",
            allowableValues = "STUDENT, TEACHER, PARENT, CLASS_TEACHER, PRINCIPAL, ADMIN")
    private String role;

    @Pattern(regexp = "^\\d{10}$", message = "Phone Number must be exactly 10 digits long")
    @Schema(description = "10-digit phone number", example = "9876543210")
    private String phone;

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
