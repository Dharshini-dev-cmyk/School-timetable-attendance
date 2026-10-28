import bcrypt

# Hash currently stored in the database
db_hash = b'$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'

# Hash from sample-data.sql (known to match "password")
seed_hash = b'$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W'

# Test common passwords against the database hash
passwords_to_try = [b'password', b'admin', b'admin123', b'root', b'school', b'123456', b'admin@school.com']

print("=== Testing database hash against common passwords ===")
print(f"Database hash: {db_hash.decode()}")
for pwd in passwords_to_try:
    result = bcrypt.checkpw(pwd, db_hash)
    print(f"  '{pwd.decode()}' -> {result}")

print()
print("=== Testing seed hash against 'password' ===")
print(f"Seed hash: {seed_hash.decode()}")
result = bcrypt.checkpw(b'password', seed_hash)
print(f"  'password' -> {result}")

print()
print("=== Are the two hashes the same? ===")
print(f"  Same hash? {db_hash == seed_hash}")
