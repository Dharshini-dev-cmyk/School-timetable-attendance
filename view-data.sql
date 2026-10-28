-- ============================================================
-- School Timetable and Attendance Management System
-- Data Retrieval Script for MySQL Workbench
-- ============================================================
-- Run this script in MySQL Workbench after connecting to the
-- Docker MySQL container (port 3307) to view all your data.
-- ============================================================

-- ============================================================
-- 1. USERS — All system users with their roles
-- ============================================================
SELECT '=== USERS ===' AS section;
SELECT
    id,
    email,
    name,
    phone,
    role,
    CASE
        WHEN role = 'ADMIN' THEN 'Admin'
        WHEN role = 'TEACHER' THEN 'Teacher'
        WHEN role = 'STUDENT' THEN 'Student'
        WHEN role = 'PARENT' THEN 'Parent'
        WHEN role = 'CLASS_TEACHER' THEN 'Class Teacher'
        WHEN role = 'PRINCIPAL' THEN 'Principal'
        ELSE role
    END AS role_description
FROM users
ORDER BY id;

-- ============================================================
-- 2. STUDENTS — All students with their user details
-- ============================================================
SELECT '=== STUDENTS ===' AS section;
SELECT
    s.id,
    s.register_number,
    s.department,
    s.year,
    u.name AS student_name,
    u.email,
    u.phone
FROM students s
JOIN users u ON s.user_id = u.id
ORDER BY s.id;

-- ============================================================
-- 3. TEACHERS — All teachers with their user details
-- ============================================================
SELECT '=== TEACHERS ===' AS section;
SELECT
    t.id,
    t.employee_id,
    t.designation,
    t.subject,
    u.name AS teacher_name,
    u.email,
    u.phone
FROM teachers t
JOIN users u ON t.user_id = u.id
ORDER BY t.id;

-- ============================================================
-- 4. TIMETABLE — Full class schedule
-- ============================================================
SELECT '=== TIMETABLE ===' AS section;
SELECT
    t.id,
    t.class_section,
    t.day,
    t.period,
    t.subject,
    t.active,
    u.name AS teacher_name
FROM timetable t
JOIN teachers te ON t.teacher_id = te.id
JOIN users u ON te.user_id = u.id
ORDER BY t.class_section, t.day, t.period;

-- ============================================================
-- 5. ATTENDANCE — All attendance records with student details
-- ============================================================
SELECT '=== ATTENDANCE ===' AS section;
SELECT
    a.id,
    a.date,
    a.status,
    a.captured_by,
    s.register_number,
    u.name AS student_name,
    ru.name AS recorded_by_name
FROM attendance a
JOIN students s ON a.student_id = s.id
JOIN users u ON s.user_id = u.id
LEFT JOIN users ru ON a.recorded_by = ru.id
ORDER BY a.date, a.id;

-- ============================================================
-- 6. HOLIDAYS — All holidays
-- ============================================================
SELECT '=== HOLIDAYS ===' AS section;
SELECT
    id,
    name,
    date,
    holiday_type,
    academic_year,
    reason,
    notification_sent
FROM holidays
ORDER BY date;

-- ============================================================
-- 7. ACADEMIC CALENDAR — Academic year details
-- ============================================================
SELECT '=== ACADEMIC CALENDAR ===' AS section;
SELECT
    id,
    academic_year,
    start_date,
    end_date,
    term1start,
    term1end,
    term2start,
    term2end,
    active
FROM academic_calendar
ORDER BY academic_year;

-- ============================================================
-- 8. BIOMETRIC DEVICES — All biometric devices
-- ============================================================
SELECT '=== BIOMETRIC DEVICES ===' AS section;
SELECT
    id,
    device_name,
    device_type,
    location,
    battery_level,
    status,
    last_sync
FROM biometric_devices
ORDER BY id;

-- ============================================================
-- 9. LEAVE APPLICATIONS — All leave applications with details
-- ============================================================
SELECT '=== LEAVE APPLICATIONS ===' AS section;
SELECT
    l.id,
    l.leave_type,
    l.from_date,
    l.to_date,
    l.status,
    l.reason,
    t.employee_id AS teacher_employee_id,
    tu.name AS teacher_name,
    a.name AS approved_by_name,
    st.employee_id AS substitute_teacher_id
FROM leave_applications l
JOIN teachers t ON l.teacher_id = t.id
JOIN users tu ON t.user_id = tu.id
LEFT JOIN users a ON l.approved_by = a.id
LEFT JOIN teachers st ON l.substitute_teacher_id = st.id
ORDER BY l.id;

-- ============================================================
-- 10. RECORD COUNTS — Summary of all tables
-- ============================================================
SELECT '=== RECORD COUNTS ===' AS section;
SELECT 'users' AS table_name, COUNT(*) AS record_count FROM users
UNION ALL SELECT 'students', COUNT(*) FROM students
UNION ALL SELECT 'teachers', COUNT(*) FROM teachers
UNION ALL SELECT 'timetable', COUNT(*) FROM timetable
UNION ALL SELECT 'attendance', COUNT(*) FROM attendance
UNION ALL SELECT 'holidays', COUNT(*) FROM holidays
UNION ALL SELECT 'academic_calendar', COUNT(*) FROM academic_calendar
UNION ALL SELECT 'biometric_devices', COUNT(*) FROM biometric_devices
UNION ALL SELECT 'leave_applications', COUNT(*) FROM leave_applications
ORDER BY table_name;
