@echo off
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -proot -e "SELECT email, name, role, password FROM school_attendance_db.users;" 2>&1