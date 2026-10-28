package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for user login")
public class LoginRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Schema(description = "Registered email address", example = "john.doe@school.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "Secure@123")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
