package com.examly.springapp.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    // token -> expiry time
    private final Map<String, Date> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, Date expiry) {
        blacklist.put(token, expiry);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }

    // Purge expired tokens every hour to prevent memory growth
    @Scheduled(fixedRate = 3600000)
    public void purgeExpired() {
        Date now = new Date();
        blacklist.entrySet().removeIf(e -> e.getValue().before(now));
    }
}
