import bcrypt

stored_hash = b'$2a$10$HJtdMtWeMxHkKdXNKVgMY.O2LqWu.2EC1Jt5PvAfmvaLSP7Dq4z1W'
password = b'password'

result = bcrypt.checkpw(password, stored_hash)
print(f'BCrypt hash: {stored_hash.decode()}')
print(f'Password: {password.decode()}')
print(f'Does hash match "password"? {result}')
print()

# Also generate a correct hash for 'password'
correct_hash = bcrypt.hashpw(password, bcrypt.gensalt(rounds=10))
print(f'Correct BCrypt hash for "password": {correct_hash.decode()}')
print(f'Verify correct hash: {bcrypt.checkpw(password, correct_hash)}')
