package com.examly.springapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body containing JWT authentication token and user details")
public class AuthResponseDTO {

    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "User role", example = "STUDENT")
    private String role;

    @Schema(description = "User email", example = "john.doe@school.com")
    private String email;

    @Schema(description = "User full name", example = "John Doe")
    private String name;

    public AuthResponseDTO(String token, String role, String email, String name) {
        this.token = token;
        this.role = role;
        this.email = email;
        this.name = name;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getName() { return name; }
}
