SELECT '=== RECORD COUNTS ===' AS section;
SELECT 'users' AS table_name, COUNT(*) AS count FROM users
UNION ALL SELECT 'students', COUNT(*) FROM students
UNION ALL SELECT 'teachers', COUNT(*) FROM teachers
UNION ALL SELECT 'timetable', COUNT(*) FROM timetable
UNION ALL SELECT 'attendance', COUNT(*) FROM attendance
UNION ALL SELECT 'holidays', COUNT(*) FROM holidays
UNION ALL SELECT 'academic_calendar', COUNT(*) FROM academic_calendar
UNION ALL SELECT 'biometric_devices', COUNT(*) FROM biometric_devices
UNION ALL SELECT 'leave_applications', COUNT(*) FROM leave_applications;
