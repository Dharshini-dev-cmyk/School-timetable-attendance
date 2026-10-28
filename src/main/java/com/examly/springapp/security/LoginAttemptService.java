package com.examly.springapp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    @Value("${app.login.max-attempts:5}")
    private int maxAttempts;

    @Value("${app.login.lockout-duration-minutes:15}")
    private int lockoutMinutes;

    private record AttemptRecord(int count, LocalDateTime lockedUntil) {}

    private final Map<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

    public void recordFailure(String email) {
        AttemptRecord current = attempts.getOrDefault(email, new AttemptRecord(0, null));
        int newCount = current.count() + 1;
        LocalDateTime lockUntil = newCount >= maxAttempts
                ? LocalDateTime.now().plusMinutes(lockoutMinutes)
                : null;
        attempts.put(email, new AttemptRecord(newCount, lockUntil));
    }

    public void recordSuccess(String email) {
        attempts.remove(email);
    }

    public boolean isLocked(String email) {
        AttemptRecord record = attempts.get(email);
        if (record == null || record.lockedUntil() == null) return false;
        if (LocalDateTime.now().isAfter(record.lockedUntil())) {
            attempts.remove(email);
            return false;
        }
        return true;
    }

    public LocalDateTime getLockExpiry(String email) {
        AttemptRecord record = attempts.get(email);
        return record != null ? record.lockedUntil() : null;
    }
}
