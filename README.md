# SAMS — Student Attendance Management System

A Java-based desktop application for tracking and reporting student attendance in educational institutions. Built as coursework for the Object-Oriented Programming module at IJSE.

---

## Project Overview

SAMS provides two user roles — **Admin** and **Lecturer** — with tools to manage courses, students, lecturers, class schedules, and attendance records. The system generates filterable attendance reports to support administrative decision-making.

The application follows a **Layered (N-Tier) Architecture**:
- **Presentation Layer** — JavaFX screens and FXML layouts
- **Service Layer** — Business logic and validation
- **Data Access Layer** — JDBC-based DAO classes
- **Data Layer** — MySQL relational database

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| JavaFX | Desktop UI framework |
| FXML | Declarative UI layout |
| Scene Builder | Visual FXML editor |
| JDBC | Database connectivity |
| MySQL 8 | Relational database |
| MySQL Workbench | Database management |
| Apache NetBeans | IDE |
| Maven | Build and dependency management |
| Git + GitHub | Version control |

---

## Setup Instructions

### Prerequisites
- Java installed
- MySQL 8.x installed and running
- Apache NetBeans IDE (with Maven support)
- Scene Builder (optional, for viewing FXML files visually)

### Step 1 — Set up the database

1. Open **MySQL Workbench** and connect to your local MySQL server
2. Go to **File → Open SQL Script** and select `sams_database.sql` from the project root
3. Click the **⚡ Execute** button to run the script
4. Verify that `sams_db` appears in the schema list with all 8 tables

### Step 2 — Configure the database connection

1. Open `src/main/java/com/sams/util/DBConnection.java`
2. Update the following constants to match your MySQL setup:

```java
private static final String USERNAME = "root";
private static final String PASSWORD = "1234";
```

### Step 3 — Build and run the project

1. Open the project in **Apache NetBeans**
2. Right-click the project → **Clean and Build**
3. Right-click the project → **Properties → Actions → Run project**
4. Set Execute Goals to: `javafx:run`
5. Click the green **Run** button (▶)

The login screen will appear.

---

## Login Credentials

### Admin Account
| Field | Value |
|---|---|
| Username | `admin` |
| Password | `admin123` |

### Lecturer Accounts
| Username | Password | Name |
|---|---|---|
| `lec_silva` | `silva123` | Dr. Nuwan Silva |
| `lec_perera` | `perera123` | Ms. Dilini Perera |
| `lec_fernando` | `fernando123` | Mr. Kasun Fernando |

---

## Features

### Admin Role
- **Course Management** — Add, view, update, and delete courses
- **Student Management** — Register and manage student profiles with course enrollment
- **Lecturer Management** — Add and manage lecturer profiles and login accounts
- **Class Scheduling** — Schedule class sessions with subject, lecturer, date, time, and venue
- **Attendance Marking** — View and mark student attendance per session
- **Attendance Reports** — Generate filterable attendance reports by student, subject, or date range

### Lecturer Role
- **Attendance Marking** — Mark and update student attendance for their sessions
- **Attendance Reports** — View attendance reports

---

## Database Schema

The MySQL database (`sams_db`) contains 8 tables:

| Table | Description |
|---|---|
| `users` | Login credentials and roles (ADMIN/LECTURER) |
| `courses` | Course programmes offered by the institution |
| `subjects` | Subjects belonging to each course |
| `students` | Student profiles with course enrollment |
| `lecturers` | Lecturer profiles linked to user accounts |
| `lecturer_subjects` | Many-to-many: lecturers assigned to subjects |
| `class_sessions` | Scheduled class sessions |
| `attendance` | Per-student per-session attendance records |

Two database views are also included:
- `vw_attendance_summary` — aggregated attendance stats per student per subject
- `vw_attendance_detail` — full attendance detail used by the reports module

---

## By

**Chanmi Tharinsa**
IJSE — Object-Oriented Programming Coursework
2026
