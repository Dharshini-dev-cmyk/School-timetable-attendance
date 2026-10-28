-- ============================================================
-- School Timetable and Attendance Management System
-- Seed Data Script (with comments)
-- This script inserts additional sample data into the MySQL
-- database running in the Docker container (school_mysql).
-- ============================================================

-- Disable foreign key checks during bulk insert for safety
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. USERS TABLE
--    Roles: STUDENT, TEACHER, PARENT, CLASS_TEACHER,
--           PRINCIPAL, ADMIN
   --    Password hash: $2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W
   --    (BCrypt encoded, decodes to "password")
-- ============================================================

-- Principal user — can manage holidays, academic calendar, analytics, MIS reports
INSERT INTO users (email, name, password, phone, role) VALUES
    ('principal@school.com', 'Dr. Sarah Mitchell', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '5551234567', 'PRINCIPAL');

-- Class Teacher user — can override attendance, view class attendance
INSERT INTO users (email, name, password, phone, role) VALUES
    ('classteacher@school.com', 'Robert Brown', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '5552345678', 'CLASS_TEACHER');

-- Additional Teacher user — teaches Mathematics
INSERT INTO users (email, name, password, phone, role) VALUES
    ('mathsteacher@school.com', 'Emma Davis', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '5553456789', 'TEACHER');

-- Additional Teacher user — teaches Physics
INSERT INTO users (email, name, password, phone, role) VALUES
    ('physicsteacher@school.com', 'James Wilson', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '5554567890', 'TEACHER');

-- Parent user — can view their child's attendance
INSERT INTO users (email, name, password, phone, role) VALUES
    ('parent1@school.com', 'Mrs. Johnson', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '5556789012', 'PARENT');

-- Additional Student user — Computer Science, Year 3
INSERT INTO users (email, name, password, phone, role) VALUES
    ('student3@school.com', 'Charlie Brown', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '6789012345', 'STUDENT');

-- Additional Student user — Electronics, Year 2
INSERT INTO users (email, name, password, phone, role) VALUES
    ('student4@school.com', 'Diana Prince', '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W', '7890123456', 'STUDENT');

-- ============================================================
-- 2. STUDENTS TABLE
--    Links to users via user_id (OneToOne relationship)
--    register_number must be unique
-- ============================================================

-- Student 3 — Computer Science, Year 3, linked to user_id 10 (Charlie Brown)
INSERT INTO students (department, register_number, year, user_id) VALUES
    ('Computer Science', 'CS2024003', 3, 10);

-- Student 4 — Electronics Engineering, Year 2, linked to user_id 11 (Diana Prince)
INSERT INTO students (department, register_number, year, user_id) VALUES
    ('Electronics', 'EE2024004', 2, 11);

-- ============================================================
-- 3. TEACHERS TABLE
--    Links to users via user_id (OneToOne relationship)
--    employee_id must be unique
-- ============================================================

-- Teacher 2 — Mathematics, linked to user_id 7 (Emma Davis)
INSERT INTO teachers (designation, employee_id, subject, user_id) VALUES
    ('Assistant Professor', 'EMP002', 'Mathematics', 7);

-- Teacher 3 — Physics, linked to user_id 8 (James Wilson)
INSERT INTO teachers (designation, employee_id, subject, user_id) VALUES
    ('Senior Lecturer', 'EMP003', 'Physics', 8);

-- Teacher 4 — Mathematics (also a Class Teacher), linked to user_id 6 (Robert Brown)
INSERT INTO teachers (designation, employee_id, subject, user_id) VALUES
    ('Class Teacher', 'EMP004', 'Mathematics', 6);

-- ============================================================
-- 4. TIMETABLE TABLE
--    Unique constraint on (teacher_id, day, period)
--    Days: MON, TUE, WED, THU, FRI, SAT
--    Period: 1-8
-- ============================================================

-- Timetable for class 10-A (complete weekly schedule with multiple teachers)
INSERT INTO timetable (active, class_section, day, period, subject, classroom, teacher_id) VALUES
    (1, '10-A', 'MON', 1, 'Mathematics', 'Room 101', 2),
    (1, '10-A', 'MON', 2, 'Physics', 'Lab 1', 3),
    (1, '10-A', 'MON', 3, 'Chemistry', 'Lab 2', 4),
    (1, '10-A', 'MON', 4, 'English', 'Room 105', 1),
    (1, '10-A', 'TUE', 1, 'Physics', 'Lab 1', 3),
    (1, '10-A', 'TUE', 2, 'Mathematics', 'Room 101', 2),
    (1, '10-A', 'TUE', 3, 'English', 'Room 105', 1),
    (1, '10-A', 'TUE', 4, 'Chemistry', 'Lab 2', 4),
    (1, '10-A', 'WED', 1, 'English', 'Room 105', 1),
    (1, '10-A', 'WED', 2, 'Chemistry', 'Lab 2', 4),
    (1, '10-A', 'WED', 3, 'Mathematics', 'Room 101', 2),
    (1, '10-A', 'WED', 4, 'Physics', 'Lab 1', 3),
    (1, '10-A', 'THU', 1, 'Computer Science', 'Lab 3', 5),
    (1, '10-A', 'THU', 2, 'Mathematics', 'Room 101', 2),
    (1, '10-A', 'THU', 3, 'Physics', 'Lab 1', 3),
    (1, '10-A', 'THU', 4, 'English', 'Room 105', 1),
    (1, '10-A', 'FRI', 1, 'Chemistry', 'Lab 2', 4),
    (1, '10-A', 'FRI', 2, 'Computer Science', 'Lab 3', 5),
    (1, '10-A', 'FRI', 3, 'Mathematics', 'Room 101', 2),
    (1, '10-A', 'FRI', 4, 'Physics', 'Lab 1', 3);

-- Timetable for class 10-B (complete weekly schedule)
INSERT INTO timetable (active, class_section, day, period, subject, classroom, teacher_id) VALUES
    (1, '10-B', 'MON', 1, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'MON', 2, 'Mathematics', 'Room 202', 2),
    (1, '10-B', 'MON', 3, 'Physics', 'Lab 1', 3),
    (1, '10-B', 'MON', 4, 'English', 'Room 105', 5),
    (1, '10-B', 'TUE', 1, 'Mathematics', 'Room 202', 2),
    (1, '10-B', 'TUE', 2, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'TUE', 3, 'English', 'Room 105', 5),
    (1, '10-B', 'TUE', 4, 'Physics', 'Lab 1', 3),
    (1, '10-B', 'WED', 1, 'Physics', 'Lab 1', 3),
    (1, '10-B', 'WED', 2, 'English', 'Room 105', 5),
    (1, '10-B', 'WED', 3, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'WED', 4, 'Mathematics', 'Room 202', 2),
    (1, '10-B', 'THU', 1, 'English', 'Room 105', 5),
    (1, '10-B', 'THU', 2, 'Physics', 'Lab 1', 3),
    (1, '10-B', 'THU', 3, 'Mathematics', 'Room 202', 2),
    (1, '10-B', 'THU', 4, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'FRI', 1, 'Mathematics', 'Room 202', 2),
    (1, '10-B', 'FRI', 2, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'FRI', 3, 'English', 'Room 105', 5),
    (1, '10-B', 'FRI', 4, 'Physics', 'Lab 1', 3);

-- ============================================================
-- 5. ATTENDANCE TABLE
--    Status: PRESENT, ABSENT, LATE, EXCUSED
--    CaptureMethod: BIOMETRIC, MANUAL
--    student_id links to students table
--    recorded_by links to users table
-- ============================================================

-- Attendance for Student 1 (Alice) on 2026-07-31 — PRESENT, recorded by admin (user 1)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('MANUAL', '2026-07-31', 'PRESENT', NULL, NULL, 1, 1);

-- Attendance for Student 2 (Bob) on 2026-07-31 — ABSENT, recorded by admin (user 1)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('MANUAL', '2026-07-31', 'ABSENT', NULL, NULL, 2, 1);

-- Attendance for Student 3 (Charlie) on 2026-07-31 — PRESENT, recorded by teacher (user 2)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('BIOMETRIC', '2026-07-31', 'PRESENT', NULL, NULL, 3, 2);

-- Attendance for Student 4 (Diana) on 2026-07-31 — LATE, recorded by teacher (user 2)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('BIOMETRIC', '2026-07-31', 'LATE', NULL, NULL, 4, 2);

-- Attendance for Student 1 (Alice) on 2026-08-01 — PRESENT, recorded by class teacher (user 6)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('MANUAL', '2026-08-01', 'PRESENT', NULL, NULL, 1, 6);

-- Attendance for Student 3 (Charlie) on 2026-08-01 — ABSENT, recorded by class teacher (user 6)
INSERT INTO attendance (captured_by, date, status, modification_reason, modified_at, student_id, recorded_by) VALUES
    ('MANUAL', '2026-08-01', 'ABSENT', NULL, NULL, 3, 6);

-- ============================================================
-- 6. HOLIDAYS TABLE
--    date must be unique
--    HolidayType: NATIONAL, REGIONAL, SCHOOL, EXAM
-- ============================================================

-- Holiday: Republic Day (National holiday)
INSERT INTO holidays (academic_year, date, holiday_type, name, reason, notification_sent) VALUES
    (2026, '2026-01-26', 'NATIONAL', 'Republic Day', '26th January marks the Republic Day of India', 1);

-- Holiday: Gandhi Jayanti (National holiday)
INSERT INTO holidays (academic_year, date, holiday_type, name, reason, notification_sent) VALUES
    (2026, '2026-10-02', 'NATIONAL', 'Gandhi Jayanti', 'Birth anniversary of Mahatma Gandhi', 1);

-- Holiday: Diwali (Regional holiday)
INSERT INTO holidays (academic_year, date, holiday_type, name, reason, notification_sent) VALUES
    (2026, '2026-11-01', 'REGIONAL', 'Diwali', 'Festival of Lights', 0);

-- Holiday: Annual Sports Day (School holiday)
INSERT INTO holidays (academic_year, date, holiday_type, name, reason, notification_sent) VALUES
    (2026, '2026-12-20', 'SCHOOL', 'Annual Sports Day', 'School annual sports competition', 0);

-- ============================================================
-- 7. ACADEMIC CALENDAR TABLE
--    academic_year must be unique
-- ============================================================

-- Academic calendar for 2025 academic year
INSERT INTO academic_calendar (academic_year, active, end_date, start_date, term1end, term1start, term2end, term2start) VALUES
    (2025, 0, '2025-12-31', '2025-07-01', '2025-10-31', '2025-07-01', '2025-12-31', '2025-11-01');

-- ============================================================
-- 8. BIOMETRIC DEVICES TABLE
--    DeviceType: FINGERPRINT, RFID, FACE
--    DeviceStatus: ONLINE, OFFLINE, MAINTENANCE
-- ============================================================

-- Biometric device 2 — RFID scanner at the library entrance
INSERT INTO biometric_devices (battery_level, device_name, device_type, last_sync, location, status) VALUES
    (92, 'BioDevice-02', 'RFID', '2026-07-30 14:30:00', 'Library Entrance', 'ONLINE');

-- Biometric device 3 — Face recognition at the main gate
INSERT INTO biometric_devices (battery_level, device_name, device_type, last_sync, location, status) VALUES
    (78, 'BioDevice-03', 'FACE', '2026-07-30 12:15:00', 'Main Gate', 'ONLINE');

-- Biometric device 4 — Fingerprint scanner at the science lab (maintenance mode)
INSERT INTO biometric_devices (battery_level, device_name, device_type, last_sync, location, status) VALUES
    (15, 'BioDevice-04', 'FINGERPRINT', '2026-07-29 09:00:00', 'Science Lab', 'MAINTENANCE');

-- ============================================================
-- 9. LEAVE APPLICATIONS TABLE
--    LeaveType: CL, SL, PL, COMP_OFF, ON_DUTY
--    LeaveStatus: PENDING, APPROVED, REJECTED
--    teacher_id links to teachers table
--    approved_by links to users table (admin/principal)
--    substitute_teacher_id links to teachers table
-- ============================================================

-- Leave application 1 — Teacher 1 (John Smith) requests CL for 2 days, pending approval
INSERT INTO leave_applications (leave_type, from_date, to_date, reason, status, teacher_id, approved_by, substitute_teacher_id) VALUES
    ('CL', '2026-08-05', '2026-08-06', 'Personal medical appointment', 'PENDING', 1, NULL, NULL);

-- Leave application 2 — Teacher 2 (Emma Davis) requests SL for 1 day, approved by principal
INSERT INTO leave_applications (leave_type, from_date, to_date, reason, status, teacher_id, approved_by, substitute_teacher_id) VALUES
    ('SL', '2026-08-10', '2026-08-10', 'Sick leave due to flu', 'APPROVED', 2, 5, 4);

-- Leave application 3 — Teacher 3 (James Wilson) requests PL for 3 days, approved by admin
INSERT INTO leave_applications (leave_type, from_date, to_date, reason, status, teacher_id, approved_by, substitute_teacher_id) VALUES
    ('PL', '2026-08-15', '2026-08-17', 'Family vacation', 'APPROVED', 3, 1, 2);

-- Leave application 4 — Teacher 1 (John Smith) requests ON_DUTY for exam duty, rejected
INSERT INTO leave_applications (leave_type, from_date, to_date, reason, status, teacher_id, approved_by, substitute_teacher_id) VALUES
    ('ON_DUTY', '2026-08-20', '2026-08-22', 'Unable to attend due to prior commitment', 'REJECTED', 1, 5, NULL);

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- Summary of inserted data:
--   Users:            6 new (5 teachers/admins + 2 students)
--   Students:         2 new
--   Teachers:         3 new
--   Timetable:        5 new
--   Attendance:       6 new
--   Holidays:         4 new
--   AcademicCalendar: 1 new
--   BiometricDevices: 3 new
--   LeaveApplications: 4 new
-- ============================================================
