import mysql.connector
import bcrypt

# Database connection
conn = mysql.connector.connect(
    host='127.0.0.1',
    port=3306,
    user='root',
    password='root',
    database='school_attendance_db'
)
cursor = conn.cursor()

# The correct BCrypt hash for "password" (from sample-data.sql, verified)
correct_hash = '$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W'

# Verify the hash matches "password"
print(f"Verifying hash matches 'password': {bcrypt.checkpw(b'password', correct_hash.encode())}")

# Update all users with the wrong hash
old_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
update_query = "UPDATE users SET password = %s WHERE password = %s"
cursor.execute(update_query, (correct_hash, old_hash))
conn.commit()
print(f"Rows updated: {cursor.rowcount}")

# Verify the update
cursor.execute("SELECT email, name, role, password FROM users")
rows = cursor.fetchall()
print("\n=== All users after update ===")
for row in rows:
    print(f"  email={row[0]}, name={row[1]}, role={row[2]}, password={row[3]}")

# Specifically check admin
cursor.execute("SELECT email, name, role, password FROM users WHERE email = 'admin@school.com'")
admin_row = cursor.fetchone()
print(f"\n=== Admin user ===")
print(f"  email={admin_row[0]}, name={admin_row[1]}, role={admin_row[2]}, password={admin_row[3]}")
print(f"  Hash matches 'password': {bcrypt.checkpw(b'password', admin_row[3].encode())}")

cursor.close()
conn.close()
