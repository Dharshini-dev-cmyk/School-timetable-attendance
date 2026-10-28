-- Verification queries for inserted data
SELECT '=== USERS ===' AS section;
SELECT id, email, name, role FROM users ORDER BY id;

SELECT '=== STUDENTS ===' AS section;
SELECT s.id, s.register_number, s.department, s.year, u.name AS student_name
FROM students s JOIN users u ON s.user_id = u.id ORDER BY s.id;

SELECT '=== TEACHERS ===' AS section;
SELECT t.id, t.employee_id, t.designation, t.subject, u.name AS teacher_name
FROM teachers t JOIN users u ON t.user_id = u.id ORDER BY t.id;

SELECT '=== TIMETABLE ===' AS section;
SELECT id, class_section, day, period, subject, teacher_id, active FROM timetable ORDER BY id;

SELECT '=== ATTENDANCE ===' AS section;
SELECT a.id, a.date, a.status, a.captured_by, s.register_number, u.name AS recorded_by
FROM attendance a JOIN students s ON a.student_id = s.id JOIN users u ON a.recorded_by = u.id ORDER BY a.id;

SELECT '=== HOLIDAYS ===' AS section;
SELECT id, name, date, holiday_type, academic_year, notification_sent FROM holidays ORDER BY id;

SELECT '=== ACADEMIC CALENDAR ===' AS section;
SELECT id, academic_year, start_date, end_date, term1Start, term1End, term2Start, term2End, active FROM academic_calendar ORDER BY id;

SELECT '=== BIOMETRIC DEVICES ===' AS section;
SELECT id, device_name, device_type, location, battery_level, status, last_sync FROM biometric_devices ORDER BY id;

SELECT '=== LEAVE APPLICATIONS ===' AS section;
SELECT l.id, l.leave_type, l.from_date, l.to_date, l.status, l.reason,
       t.employee_id AS teacher, u.name AS approved_by, st.employee_id AS substitute_teacher
FROM leave_applications l
JOIN teachers t ON l.teacher_id = t.id
LEFT JOIN users u ON l.approved_by = u.id
LEFT JOIN teachers st ON l.substitute_teacher_id = st.id
ORDER BY l.id;

SELECT '=== RECORD COUNTS ===' AS section;
SELECT 'users' AS table_name, COUNT(*) AS record_count FROM users
UNION ALL SELECT 'students', COUNT(*) FROM students
UNION ALL SELECT 'teachers', COUNT(*) FROM teachers
UNION ALL SELECT 'timetable', COUNT(*) FROM timetable
UNION ALL SELECT 'attendance', COUNT(*) FROM attendance
UNION ALL SELECT 'holidays', COUNT(*) FROM holidays
UNION ALL SELECT 'academic_calendar', COUNT(*) FROM academic_calendar
UNION ALL SELECT 'biometric_devices', COUNT(*) FROM biometric_devices
UNION ALL SELECT 'leave_applications', COUNT(*) FROM leave_applications;
