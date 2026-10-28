package com.examly.springapp.config;
import com.examly.springapp.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000))
                .frameOptions(frame -> frame.sameOrigin())
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; frame-ancestors 'self'"))
                .referrerPolicy(ref -> ref
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/timetable/generate").hasAnyRole("PRINCIPAL", "ADMIN")
                .requestMatchers("/api/timetable/clashes").hasAnyRole("PRINCIPAL", "ADMIN")
                .requestMatchers("/api/leave/*/approve", "/api/leave/*/reject").hasAnyRole("PRINCIPAL", "ADMIN")
                .requestMatchers("/api/attendance/*/override").hasAnyRole("CLASS_TEACHER", "PRINCIPAL", "ADMIN")
                .requestMatchers("/api/attendance/below75").hasAnyRole("PRINCIPAL", "ADMIN")
                // Holiday and Calendar
                .requestMatchers("/api/holidays/**").hasAnyRole("PRINCIPAL", "ADMIN")
                .requestMatchers("/api/academic-calendar/**").hasAnyRole("PRINCIPAL", "ADMIN")
                // Analytics and MIS
                .requestMatchers("/api/analytics/**").hasAnyRole("PRINCIPAL", "ADMIN")
                .requestMatchers("/api/mis/**").hasAnyRole("PRINCIPAL", "ADMIN")
                // User management
                .requestMatchers("/api/users").hasAnyRole("ADMIN", "PRINCIPAL")
                .requestMatchers("/api/users/profile").authenticated()
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "PRINCIPAL")
                // Student management
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/students").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/students/**").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "PRINCIPAL", "TEACHER", "CLASS_TEACHER")
                // Teacher management
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/teachers").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/teachers/**").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/teachers/**").hasAnyRole("ADMIN", "PRINCIPAL", "TEACHER", "CLASS_TEACHER")
                // Biometric devices
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/biometric-devices").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/biometric-devices/**").hasAnyRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/biometric-devices/**").hasAnyRole("ADMIN", "PRINCIPAL")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
