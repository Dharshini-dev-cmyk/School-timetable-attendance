import urllib.request, json, sys

BASE = "http://localhost:8081"
results = []

def req(method, path, data=None, token=None, ct="application/json"):
    headers = {}
    if ct: headers["Content-Type"] = ct
    if token: headers["Authorization"] = f"Bearer {token}"
    r = urllib.request.Request(BASE+path, data=data, headers=headers, method=method)
    try:
        resp = urllib.request.urlopen(r)
        return resp.status, resp.read().decode()
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode()

def t(name, method, path, data=None, token=None, exp=None):
    s, r = req(method, path, data, token)
    ok = "PASS" if (exp is None or s == exp) else f"FAIL(exp {exp})"
    results.append((name, s, ok, r[:200]))
    print(f"[{ok}] {name}: HTTP {s}")
    if ok != "PASS": print(f"  Resp: {r[:300]}")

# Login
print("=== LOGIN ===")
s, r = req("POST", "/api/auth/login", json.dumps({"email":"testuser@school.com","password":"Test@1234"}).encode())
print(f"Login: {s}")
token = json.loads(r)["token"] if s == 200 else None
if not token: sys.exit(1)

# Auth tests
print("\n=== AUTH ===")
t("Register valid", "POST", "/api/auth/register", json.dumps({"name":"NU","email":"nu@school.com","password":"Test@1234","role":"STUDENT","phone":"1111111111"}).encode(), exp=201)
t("Register dup", "POST", "/api/auth/register", json.dumps({"name":"NU","email":"nu@school.com","password":"Test@1234","role":"STUDENT","phone":"1111111111"}).encode(), exp=409)
t("Register invalid email", "POST", "/api/auth/register", json.dumps({"name":"NU","email":"bad","password":"Test@1234","role":"STUDENT","phone":"1111111111"}).encode(), exp=400)
t("Register weak pwd", "POST", "/api/auth/register", json.dumps({"name":"NU","email":"w@school.com","password":"weak","role":"STUDENT","phone":"1111111111"}).encode(), exp=400)
t("Login valid", "POST", "/api/auth/login", json.dumps({"email":"testuser@school.com","password":"Test@1234"}).encode(), exp=200)
t("Login wrong pwd", "POST", "/api/auth/login", json.dumps({"email":"testuser@school.com","password":"wrong"}).encode(), exp=404)
t("Login no user", "POST", "/api/auth/login", json.dumps({"email":"nope@school.com","password":"Test@1234"}).encode(), exp=404)
t("Login malformed JSON", "POST", "/api/auth/login", b'{"email\\":"x@y.com","password\\":"Test@1234"}', exp=400)

# Users
print("\n=== USERS ===")
t("Get all users", "GET", "/api/users", token=token, exp=200)
t("Get user by ID", "GET", "/api/users/1", token=token, exp=200)
t("Get user 99999", "GET", "/api/users/99999", token=token, exp=404)
t("Get profile", "GET", "/api/users/profile", token=token, exp=200)
t("Delete user 99999", "DELETE", "/api/users/99999", token=token, exp=404)

# Students
print("\n=== STUDENTS ===")
t("Get all students", "GET", "/api/students", token=token, exp=200)
t("Get student 1", "GET", "/api/students/1", token=token, exp=200)
t("Get student 99999", "GET", "/api/students/99999", token=token, exp=404)
t("Get student by reg", "GET", "/api/students/register/CS2024001", token=token, exp=200)
t("Get student by bad reg", "GET", "/api/students/register/NOEXIST", token=token, exp=404)
t("Create student valid", "POST", "/api/students", json.dumps({"registerNumber":"CS2024005","department":"Physics","year":3,"userId":1}).encode(), token=token, exp=201)
t("Create student dup", "POST", "/api/students", json.dumps({"registerNumber":"CS2024005","department":"Physics","year":3,"userId":1}).encode(), token=token, exp=409)
t("Create student bad user", "POST", "/api/students", json.dumps({"registerNumber":"CS2024006","department":"Physics","year":3,"userId":99999}).encode(), token=token, exp=404)
t("Create student missing", "POST", "/api/students", json.dumps({"department":"Physics","year":3}).encode(), token=token, exp=400)
t("Update student 1", "PUT", "/api/students/1", json.dumps({"registerNumber":"CS2024001","department":"Computer Science","year":3,"userId":3}).encode(), token=token, exp=200)
t("Update student 99999", "PUT", "/api/students/99999", json.dumps({"registerNumber":"X","department":"CS","year":3,"userId":3}).encode(), token=token, exp=404)
t("Delete student 5", "DELETE", "/api/students/5", token=token, exp=204)
t("Delete student 99999", "DELETE", "/api/students/99999", token=token, exp=404)

# Teachers
print("\n=== TEACHERS ===")
t("Get all teachers", "GET", "/api/teachers", token=token, exp=200)
t("Get teacher 1", "GET", "/api/teachers/1", token=token, exp=200)
t("Get teacher 99999", "GET", "/api/teachers/99999", token=token, exp=404)
t("Get teacher by emp", "GET", "/api/teachers/employee/EMP001", token=token, exp=200)
t("Get teacher by bad emp", "GET", "/api/teachers/employee/NOEXIST", token=token, exp=404)
t("Create teacher valid", "POST", "/api/teachers", json.dumps({"employeeId":"EMP005","designation":"Lecturer","subject":"Chemistry","userId":1}).encode(), token=token, exp=201)
t("Create teacher dup", "POST", "/api/teachers", json.dumps({"employeeId":"EMP005","designation":"Lecturer","subject":"Chemistry","userId":1}).encode(), token=token, exp=409)
t("Create teacher bad user", "POST", "/api/teachers", json.dumps({"employeeId":"EMP006","designation":"Lecturer","subject":"Chemistry","userId":99999}).encode(), token=token, exp=404)
t("Create teacher missing", "POST", "/api/teachers", json.dumps({"designation":"Lecturer","subject":"Chemistry"}).encode(), token=token, exp=400)
t("Update teacher 1", "PUT", "/api/teachers/1", json.dumps({"employeeId":"EMP001","designation":"Senior Lecturer","subject":"Database Systems","userId":2}).encode(), token=token, exp=200)
t("Update teacher 99999", "PUT", "/api/teachers/99999", json.dumps({"employeeId":"EMP001","designation":"X","subject":"Y","userId":2}).encode(), token=token, exp=404)
t("Delete teacher 4", "DELETE", "/api/teachers/4", token=token, exp=204)
t("Delete teacher 99999", "DELETE", "/api/teachers/99999", token=token, exp=404)

# Timetable
print("\n=== TIMETABLE ===")
t("Get class timetable", "GET", "/api/timetable/class/10-A", token=token, exp=200)
t("Get teacher timetable", "GET", "/api/timetable/teacher/1", token=token, exp=200)
t("Detect clashes", "GET", "/api/timetable/clashes", token=token, exp=200)
t("Create slot valid", "POST", "/api/timetable/generate", json.dumps({"classSection":"10-C","day":"MON","period":1,"subject":"Chemistry","teacherId":1}).encode(), token=token, exp=201)
t("Create slot clash", "POST", "/api/timetable/generate", json.dumps({"classSection":"10-D","day":"MON","period":1,"subject":"Chemistry","teacherId":1}).encode(), token=token, exp=409)
t("Create slot missing", "POST", "/api/timetable/generate", json.dumps({"day":"MON","period":1}).encode(), token=token, exp=400)
t("Create slot bad period", "POST", "/api/timetable/generate", json.dumps({"classSection":"10-E","day":"MON","period":9,"subject":"Chemistry","teacherId":1}).encode(), token=token, exp=400)
t("Suggest substitutes", "GET", "/api/timetable/substitutes?absentTeacherId=1&day=MON&period=1", token=token, exp=200)
t("Update slot 1", "PUT", "/api/timetable/1", json.dumps({"classSection":"10-A","day":"MON","period":1,"subject":"Database Systems","teacherId":1}).encode(), token=token, exp=200)
t("Update slot 99999", "PUT", "/api/timetable/99999", json.dumps({"classSection":"10-A","day":"MON","period":1,"subject":"X","teacherId":1}).encode(), token=token, exp=404)

# Attendance
print("\n=== ATTENDANCE ===")
t("Get class attendance", "GET", "/api/attendance/class/1/date/2026-07-30", token=token, exp=200)
t("Get student attendance", "GET", "/api/attendance/student/1", token=token, exp=200)
t("Get student 99999 attendance", "GET", "/api/attendance/student/99999", token=token, exp=404)
t("Get below75", "GET", "/api/attendance/below75", token=token, exp=200)
t("Mark attendance valid", "POST", "/api/attendance?recordedByUserId=1", json.dumps({"studentId":1,"date":"2026-08-02","status":"PRESENT","capturedBy":"MANUAL"}).encode(), token=token, exp=201)
t("Mark attendance invalid status", "POST", "/api/attendance?recordedByUserId=1", json.dumps({"studentId":1,"date":"2026-08-02","status":"INVALID"}).encode(), token=token, exp=500)
t("Mark attendance bad student", "POST", "/api/attendance?recordedByUserId=1", json.dumps({"studentId":99999,"date":"2026-08-02","status":"PRESENT"}).encode(), token=token, exp=404)
t("Mark attendance bad user", "POST", "/api/attendance?recordedByUserId=99999", json.dumps({"studentId":1,"date":"2026-08-02","status":"PRESENT"}).encode(), token=token, exp=404)
t("Override attendance valid", "PUT", "/api/attendance/1/override", json.dumps({"status":"PRESENT","reason":"Medical certificate"}).encode(), token=token, exp=200)
t("Override attendance no reason", "PUT", "/api/attendance/1/override", json.dumps({"status":"PRESENT","reason":""}).encode(), token=token, exp=400)
t("Override attendance 99999", "PUT", "/api/attendance/99999/override", json.dumps({"status":"PRESENT","reason":"Test"}).encode(), token=token, exp=404)

# Holidays
print("\n=== HOLIDAYS ===")
t("Get all holidays", "GET", "/api/holidays", token=token, exp=200)
t("Get holiday 1", "GET", "/api/holidays/1", token=token, exp=200)
t("Get holiday 99999", "GET", "/api/holidays/99999", token=token, exp=404)
t("Get holidays by year", "GET", "/api/holidays/year/2026", token=token, exp=200)
t("Get holidays by year 1999", "GET", "/api/holidays/year/1999", token=token, exp=200)
t("Count working days", "GET", "/api/holidays/working-days?from=2026-07-01&to=2026-07-31&year=2026", token=token, exp=200)
t("Add holiday valid", "POST", "/api/holidays", json.dumps({"name":"Test Holiday","date":"2026-09-15","holidayType":"NATIONAL","reason":"Test","academicYear":2026}).encode(), token=token, exp=201)
t("Add holiday dup date", "POST", "/api/holidays", json.dumps({"name":"Test2","date":"2026-09-15","holidayType":"NATIONAL","reason":"T","academicYear":2026}).encode(), token=token, exp=409)
t("Add holiday missing", "POST", "/api/holidays", json.dumps({"date":"2026-09-15","holidayType":"NATIONAL"}).encode(), token=token, exp=400)
t("Delete holiday 2", "DELETE", "/api/holidays/2", token=token, exp=204)
t("Delete holiday 99999", "DELETE", "/api/holidays/99999", token=token, exp=404)

# Academic Calendar
print("\n=== ACADEMIC CALENDAR ===")
t("Get all calendars", "GET", "/api/academic-calendar", token=token, exp=200)
t("Get active calendar", "GET", "/api/academic-calendar/active", token=token, exp=200)
t("Get calendar by year", "GET", "/api/academic-calendar/year/2026", token=token, exp=200)
t("Get calendar 1999", "GET", "/api/academic-calendar/year/1999", token=token, exp=404)
t("Create calendar valid", "POST", "/api/academic-calendar", json.dumps({"academicYear":2027,"startDate":"2027-07-01","endDate":"2027-12-31","term1Start":"2027-07-01","term1End":"2027-10-31","term2Start":"2027-11-01","term2End":"2027-12-31"}).encode(), token=token, exp=201)
t("Create calendar dup", "POST", "/api/academic-calendar", json.dumps({"academicYear":2026,"startDate":"2026-07-01","endDate":"2026-12-31","term1Start":"2026-07-01","term1End":"2026-10-31","term2Start":"2026-11-01","term2End":"2026-12-31"}).encode(), token=token, exp=409)
t("Create calendar missing", "POST", "/api/academic-calendar", json.dumps({"academicYear":2028}).encode(), token=token, exp=400)
t("Update calendar 1", "PUT", "/api/academic-calendar/1", json.dumps({"academicYear":2026,"startDate":"2026-07-01","endDate":"2026-12-31","term1Start":"2026-07-01","term1End":"2026-10-31","term2Start":"2026-11-01","term2End":"2026-12-31"}).encode(), token=token, exp=200)
t("Update calendar 99999", "PUT", "/api/academic-calendar/99999", json.dumps({"academicYear":2026,"startDate":"2026-07-01","endDate":"2026-12-31","term1Start":"2026-07-01","term1End":"2026-10-31","term2Start":"2026-11-01","term2End":"2026-12-31"}).encode(), token=token, exp=404)

# Leave
print("\n=== LEAVE ===")
t("Get pending leaves", "GET", "/api/leave/pending", token=token, exp=200)
t("Get teacher leaves 1", "GET", "/api/leave/balance/1", token=token, exp=200)
t("Get teacher leaves 99999", "GET", "/api/leave/balance/99999", token=token, exp=200)
t("Apply leave valid", "POST", "/api/leave?teacherId=1", json.dumps({"leaveType":"CL","fromDate":"2026-08-10","toDate":"2026-08-12","reason":"Personal"}).encode(), token=token, exp=201)
t("Apply leave bad teacher", "POST", "/api/leave?teacherId=99999", json.dumps({"leaveType":"CL","fromDate":"2026-08-10","toDate":"2026-08-12","reason":"Personal"}).encode(), token=token, exp=404)
t("Apply leave missing", "POST", "/api/leave?teacherId=1", json.dumps({"fromDate":"2026-08-10","toDate":"2026-08-12"}).encode(), token=token, exp=400)
t("Approve leave 1", "PUT", "/api/leave/1/approve", json.dumps({"approvedByUserId":1,"substituteTeacherId":2}).encode(), token=token, exp=200)
t("Approve leave 99999", "PUT", "/api/leave/99999/approve", json.dumps({"approvedByUserId":1,"substituteTeacherId":2}).encode(), token=token, exp=404)
t("Reject leave 1", "PUT", "/api/leave/1/reject", json.dumps({"rejectedByUserId":1}).encode(), token=token, exp=200)
t("Reject leave 99999", "PUT", "/api/leave/99999/reject", json.dumps({"rejectedByUserId":1}).encode(), token=token, exp=404)

# Analytics
print("\n=== ANALYTICS ===")
t("At-risk students", "GET", "/api/analytics/at-risk-students", token=token, exp=200)
t("Chronic absentees", "GET", "/api/analytics/chronic-absentees", token=token, exp=200)
t("Teacher leave utilisation", "GET", "/api/analytics/teacher-leave-utilisation", token=token, exp=200)
t("Substitute burden", "GET", "/api/analytics/substitute-burden", token=token, exp=200)
t("Teacher workload", "GET", "/api/analytics/teacher-workload", token=token, exp=200)

# MIS
print("\n=== MIS ===")
t("Daily attendance", "GET", "/api/mis/daily-attendance?date=2026-07-30", token=token, exp=200)
t("Below 75 students", "GET", "/api/mis/below75-students", token=token, exp=200)
t("Annual attendance summary", "GET", "/api/mis/annual-attendance-summary", token=token, exp=200)
t("Teacher deployment", "GET", "/api/mis/teacher-deployment", token=token, exp=200)
t("Leave summary 1", "GET", "/api/mis/leave-summary/1", token=token, exp=200)

# Swagger
print("\n=== SWAGGER ===")
t("API Docs", "GET", "/v3/api-docs", exp=200)
t("Swagger UI", "GET", "/swagger-ui.html", exp=200)
t("Swagger UI HTML", "GET", "/swagger-ui/index.html", exp=200)

# No token tests
print("\n=== NO TOKEN ===")
t("No token /api/users", "GET", "/api/users", exp=403)
t("No token /api/students", "GET", "/api/students", exp=403)
t("No token /api/holidays", "GET", "/api/holidays", exp=403)
t("No token /api/analytics/at-risk-students", "GET", "/api/analytics/at-risk-students", exp=403)

# Invalid token
print("\n=== INVALID TOKEN ===")
t("Invalid token /api/users", "GET", "/api/users", token="invalid.token.here", exp=403)

# Logout (at the end)
print("\n=== LOGOUT ===")
t("Logout", "POST", "/api/auth/logout", b'{}', token=token, exp=200)

# Summary
print("\n" + "=" * 60)
print("SUMMARY")
print("=" * 60)
passed = sum(1 for r in results if r[2] == "PASS")
failed = sum(1 for r in results if r[2] != "PASS")
print(f"Total: {len(results)}, Passed: {passed}, Failed: {failed}")
for name, status, ok, resp in results:
    if ok != "PASS":
        print(f"  FAIL: {name}: HTTP {status} - {resp[:200]}")
