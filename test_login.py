import requests
import json

url = "http://localhost:8080/api/auth/login"
payload = {
    "email": "admin@school.com",
    "password": "password"
}
headers = {
    "accept": "*/*",
    "Content-Type": "application/json"
}

response = requests.post(url, headers=headers, json=payload)
print(f"Status Code: {response.status_code}")
print(f"Response Body: {json.dumps(response.json(), indent=2)}")
