package com.examly.springapp.exception;

/**
 * Thrown when authentication fails (invalid email or password).
 * Mapped to HTTP 401 Unauthorized by {@link GlobalExceptionHandler}.
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
