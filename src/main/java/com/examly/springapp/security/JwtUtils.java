package com.examly.springapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-student-ms}")
    private long studentExpiryMs;

    @Value("${app.jwt.expiration-teacher-ms}")
    private long teacherExpiryMs;

    @Value("${app.jwt.expiration-admin-ms}")
    private long adminExpiryMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private long resolveExpiry(String role) {
        if (role == null) return adminExpiryMs;
        return switch (role.toUpperCase()) {
            case "STUDENT", "PARENT" -> studentExpiryMs;
            case "TEACHER", "CLASS_TEACHER" -> teacherExpiryMs;
            default -> adminExpiryMs; // PRINCIPAL, ADMIN
        };
    }

    public String generateToken(String email, String role) {
        return buildToken(email, role, null);
    }

    public String generateTokenWithUserId(String email, String role, Long userId) {
        return buildToken(email, role, userId);
    }

    private String buildToken(String email, String role, Long userId) {
        var builder = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + resolveExpiry(role)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);
        if (userId != null) builder.claim("userId", userId);
        return builder.compact();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) parseClaims(token).get("role");
    }

    public Date getExpirationFromToken(String token) {
        return parseClaims(token).getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
