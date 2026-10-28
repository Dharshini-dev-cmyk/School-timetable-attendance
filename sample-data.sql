-- ============================================================
-- School Timetable and Attendance Management System
-- Sample Data Script (with comments)
-- This script loads initial sample data into the MySQL database.
-- Run this after starting Docker Compose (docker-compose up -d)
-- and before starting the application.
-- ============================================================

-- Disable foreign key checks during bulk insert for safety
SET FOREIGN_KEY_CHECKS = 0;

-- Clear all existing data (in dependency order to avoid FK constraint errors)
TRUNCATE TABLE attendance;
TRUNCATE TABLE timetable;
TRUNCATE TABLE biometric_devices;
TRUNCATE TABLE academic_calendar;
TRUNCATE TABLE holidays;
TRUNCATE TABLE leave_applications;
TRUNCATE TABLE students;
TRUNCATE TABLE teachers;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. USERS TABLE
--    Roles: STUDENT, TEACHER, PARENT, CLASS_TEACHER,
--           PRINCIPAL, ADMIN
   --    Password hash: $2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W
   --    (BCrypt encoded, decodes to "password")
-- ============================================================

-- Admin user — full access to all endpoints
INSERT INTO users (email, name, password, phone, role) VALUES
    ('admin@school.com', 'Admin User', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '1234567890', 'ADMIN'),
    -- Teacher user — can mark attendance, view timetable
    ('teacher@school.com', 'John Smith', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '2345678901', 'TEACHER'),
    -- Student user — can view own attendance
    ('student1@school.com', 'Alice Johnson', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '3456789012', 'STUDENT'),
    -- Student user — can view own attendance
    ('student2@school.com', 'Bob Wilson', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '4567890123', 'STUDENT');

-- ============================================================
-- 2. STUDENTS TABLE
--    Links to users via user_id (OneToOne relationship)
--    register_number must be unique
-- ============================================================

-- Student 1 — Computer Science, Year 2, linked to user_id 3 (Alice Johnson)
INSERT INTO students (department, register_number, year, user_id) VALUES
    ('Computer Science', 'CS2024001', 2, 3),
    -- Student 2 — Mathematics, Year 1, linked to user_id 4 (Bob Wilson)
    ('Mathematics', 'CS2024002', 1, 4);

-- ============================================================
-- 3. TEACHERS TABLE
--    Links to users via user_id (OneToOne relationship)
--    employee_id must be unique
-- ============================================================

-- Teacher 1 — Database Systems, linked to user_id 2 (John Smith)
INSERT INTO teachers (designation, employee_id, subject, user_id) VALUES
    ('Senior Lecturer', 'EMP001', 'Database Systems', 2);

-- ============================================================
-- 4. TIMETABLE TABLE
--    Unique constraint on (teacher_id, day, period)
--    Days: MON, TUE, WED, THU, FRI, SAT
--    Period: 1-8
-- ============================================================

-- Timetable for class 10-A (complete weekly schedule)
INSERT INTO timetable (active, class_section, day, period, subject, classroom, teacher_id) VALUES
    (1, '10-A', 'MON', 1, 'Database Systems', 'Room 101', 1),
    (1, '10-A', 'MON', 2, 'Operating Systems', 'Room 101', 1),
    (1, '10-A', 'MON', 3, 'Computer Networks', 'Lab 1', 1),
    (1, '10-A', 'MON', 4, 'Mathematics', 'Room 201', 1),
    (1, '10-A', 'TUE', 1, 'Database Systems', 'Lab 2', 1),
    (1, '10-A', 'TUE', 2, 'Mathematics', 'Room 201', 1),
    (1, '10-A', 'TUE', 3, 'Operating Systems', 'Room 101', 1),
    (1, '10-A', 'TUE', 4, 'Computer Networks', 'Lab 1', 1),
    (1, '10-A', 'WED', 1, 'Mathematics', 'Room 201', 1),
    (1, '10-A', 'WED', 2, 'Database Systems', 'Room 101', 1),
    (1, '10-A', 'WED', 3, 'Operating Systems', 'Lab 2', 1),
    (1, '10-A', 'WED', 4, 'Computer Networks', 'Room 101', 1),
    (1, '10-A', 'THU', 1, 'Computer Networks', 'Lab 1', 1),
    (1, '10-A', 'THU', 2, 'Mathematics', 'Room 201', 1),
    (1, '10-A', 'THU', 3, 'Database Systems', 'Room 101', 1),
    (1, '10-A', 'THU', 4, 'Operating Systems', 'Room 101', 1),
    (1, '10-A', 'FRI', 1, 'Operating Systems', 'Lab 2', 1),
    (1, '10-A', 'FRI', 2, 'Computer Networks', 'Room 101', 1),
    (1, '10-A', 'FRI', 3, 'Mathematics', 'Room 201', 1),
    (1, '10-A', 'FRI', 4, 'Database Systems', 'Lab 1', 1),
    -- Timetable for class 10-B
    (1, '10-B', 'MON', 1, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'MON', 2, 'Physics', 'Lab 3', 1),
    (1, '10-B', 'MON', 3, 'Chemistry', 'Lab 4', 1),
    (1, '10-B', 'MON', 4, 'English', 'Room 105', 1),
    (1, '10-B', 'TUE', 1, 'Physics', 'Lab 3', 1),
    (1, '10-B', 'TUE', 2, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'TUE', 3, 'English', 'Room 105', 1),
    (1, '10-B', 'TUE', 4, 'Chemistry', 'Lab 4', 1),
    (1, '10-B', 'WED', 1, 'English', 'Room 105', 1),
    (1, '10-B', 'WED', 2, 'Chemistry', 'Lab 4', 1),
    (1, '10-B', 'WED', 3, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'WED', 4, 'Physics', 'Lab 3', 1);

-- ============================================================
-- 5. ATTENDANCE TABLE
--    Status: PRESENT, ABSENT, LATE, EXCUSED
--    CaptureMethod: BIOMETRIC, MANUAL
--    student_id links to students table
--    recorded_by links to users table
-- ============================================================

-- Attendance records for 2026-07-30
-- Student 1 (Alice) — PRESENT
-- Student 2 (Bob) — PRESENT
-- Student 1 (Alice) — ABSENT (duplicate date, different record)
INSERT INTO attendance (captured_by, date, status, student_id) VALUES
    ('MANUAL', '2026-07-30', 'PRESENT', 1),
    ('MANUAL', '2026-07-30', 'PRESENT', 2),
    ('MANUAL', '2026-07-30', 'ABSENT', 1);

-- ============================================================
-- 6. HOLIDAYS TABLE
--    date must be unique
--    HolidayType: NATIONAL, REGIONAL, SCHOOL, EXAM
-- ============================================================

-- Holiday: Independence Day (National holiday)
INSERT INTO holidays (academic_year, date, holiday_type, name, reason) VALUES
    (2026, '2026-08-15', 'NATIONAL', 'Independence Day', 'Republic Day celebration');

-- ============================================================
-- 7. ACADEMIC CALENDAR TABLE
--    academic_year must be unique
-- ============================================================

-- Academic calendar for 2026 academic year (active)
INSERT INTO academic_calendar (academic_year, active, end_date, start_date, term1end, term1start, term2end, term2start) VALUES
    (2026, 1, '2026-12-31', '2026-07-01', '2026-10-31', '2026-07-01', '2026-12-31', '2026-11-01');

-- ============================================================
-- 8. BIOMETRIC DEVICES TABLE
--    DeviceType: FINGERPRINT, RFID, FACE
--    DeviceStatus: ONLINE, OFFLINE, MAINTENANCE
-- ============================================================

-- Biometric device 1 — Fingerprint scanner at the main gate
INSERT INTO biometric_devices (battery_level, device_name, device_type, last_sync, location, status) VALUES
    (85, 'BioDevice-01', 'FINGERPRINT', '2026-07-30 10:00:00', 'Main Gate', 'ONLINE');

-- ============================================================
-- Success message
-- ============================================================
SELECT 'Sample data inserted successfully!' AS Status;
