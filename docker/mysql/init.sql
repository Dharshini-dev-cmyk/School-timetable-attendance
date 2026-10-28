-- ============================================================
-- School Timetable and Attendance Management System
-- MySQL Initialisation Script
-- ============================================================

CREATE DATABASE IF NOT EXISTS school_attendance_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Grant all privileges to root so the Spring Boot app
-- (docker profile) can connect with root/root.
GRANT ALL PRIVILEGES ON school_attendance_db.* TO 'root'@'%';
FLUSH PRIVILEGES;
