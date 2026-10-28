import bcrypt

# Verify the existing (correct) hash
stored_hash = b'$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W'
password = b'password'

result = bcrypt.checkpw(password, stored_hash)
print(f'=== Verification of existing hash ===')
print(f'Stored hash: {stored_hash.decode()}')
print(f'Password: {password.decode()}')
print(f'Does hash match "password"? {result}')
print()

# Generate a correct $2a$ hash for "password"
correct_hash = bcrypt.hashpw(password, bcrypt.gensalt(rounds=10, prefix=b'2a'))
print(f'=== Correct BCrypt hash for "password" ===')
print(f'Hash: {correct_hash.decode()}')
print(f'Verify correct hash: {bcrypt.checkpw(password, correct_hash)}')
print()

# Also generate a $2b$ hash (Spring Security BCryptPasswordEncoder supports both)
correct_hash_b = bcrypt.hashpw(password, bcrypt.gensalt(rounds=10, prefix=b'2b'))
print(f'=== Correct BCrypt hash ($2b$) for "password" ===')
print(f'Hash: {correct_hash_b.decode()}')
print(f'Verify correct hash: {bcrypt.checkpw(password, correct_hash_b)}')
