@echo off
REM ============================================================
REM Local development launcher
REM Activates the 'local' Spring profile (connects to MySQL
REM on localhost:3306 with root credentials).
REM ============================================================
set SPRING_PROFILES_ACTIVE=local
set JWT_SECRET=SchoolTimetableAttendanceSecretKey256BitLongForHS256Algorithm
mvn spring-boot:run
