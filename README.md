School Timetable and Attendance Management System

📌 Project Overview

The School Timetable and Attendance Management System is a web-based application designed to simplify school administration by managing timetables, student attendance, and student and teacher information in one place.

The system provides an organized way to create and manage class schedules, record attendance, and view attendance reports. It aims to reduce manual work, improve data organization, and make school management more efficient.

✨ Features

- Timetable Management: Create and manage school timetables.
- Attendance Management: Record and maintain student attendance.
- Attendance Reports: View attendance records and reports.
- Student Management: Manage student information.
- Teacher Management: Maintain teacher details.
- Authentication: Login and authentication functionality.
- Database Integration: Store and retrieve application data using MySQL.
- User-Friendly Interface: Access system features through a React-based interface.

🛠️ Technologies Used

Technology| Purpose
HTML5| Page structure
CSS3| Styling and layout
JavaScript| Frontend functionality
React.js| User interface
Java| Backend programming
Spring Boot| Backend APIs and application logic
Maven| Dependency management and build
MySQL| Database management

🏗️ System Architecture

The application follows a frontend–backend architecture:

- Frontend: React.js provides the user interface.
- Backend: Spring Boot handles application logic and API requests.
- Database: MySQL stores student, teacher, timetable, and attendance information.

The frontend communicates with the backend, which processes requests and interacts with the database.

📂 Project Structure

School-Timetable-and-Attendance-Management/
├── frontend/ # React frontend
├── src/ # Spring Boot source code
├── pom.xml # Maven configuration
├── mysql-schema.sql # Database schema
├── docker-compose.yml # Docker configuration
├── .env.example # Example environment variables
├── .gitignore
└── README.md

⚙️ Prerequisites

Before running the project, ensure you have installed:

- Java JDK compatible with the project
- Node.js and npm
- MySQL Server
- Maven, or use the Maven Wrapper if provided
- Git

🚀 Getting Started

1. Clone the Repository

git clone YOUR_GITHUB_REPOSITORY_URL
cd School-Timetable-and-Attendance-Management

Replace "YOUR_GITHUB_REPOSITORY_URL" with your actual GitHub repository URL.

2. Configure the Database

1. Start your MySQL server.
2. Create the required database.
3. Configure the database connection details in your application's configuration or environment variables.
4. Run the database schema or initialization scripts as required by the project.

Important: Never upload real database passwords or secret credentials to GitHub.

3. Run the Backend

From the project root, run:

mvn spring-boot:run

If the project uses a Maven Wrapper, use the appropriate wrapper command for your operating system.

4. Run the Frontend

Open another terminal and run:

cd frontend
npm install
npm run dev

Use the local URL displayed in the terminal to open the frontend. The exact command may differ depending on the frontend configuration.

🔐 Security

- Keep database credentials and secret keys in environment variables.
- Do not commit ".env" files containing real credentials.
- Use ".env.example" to document required configuration variables without exposing their values.

🎯 Project Objective

The main objective of this project is to digitize timetable and attendance management, centralize school-related information, and provide a convenient platform for managing daily academic activities.

👩‍💻 Developed By

Dharshini S

Engineering Student

📄 License

This project was developed for educational and academic purposes.
