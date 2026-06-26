-- ============================================================
--  SAMS — Student Attendance Management System
--  Database Schema + Sample Data
--  MySQL 8.x compatible
-- ============================================================

DROP DATABASE IF EXISTS sams_db;
CREATE DATABASE sams_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sams_db;


CREATE TABLE users (
    user_id     INT          AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,          
    role        ENUM('ADMIN','LECTURER') NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    is_active   TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE courses (
    course_id   INT          AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20)  NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    duration_years TINYINT UNSIGNED NOT NULL DEFAULT 3,
    description TEXT,
    is_active   TINYINT(1)  NOT NULL DEFAULT 1,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE subjects (
    subject_id   INT          AUTO_INCREMENT PRIMARY KEY,
    course_id    INT          NOT NULL,
    subject_code VARCHAR(20)  NOT NULL UNIQUE,
    subject_name VARCHAR(150) NOT NULL,
    credits      TINYINT UNSIGNED NOT NULL DEFAULT 3,
    semester     TINYINT UNSIGNED NOT NULL DEFAULT 1,
    is_active    TINYINT(1)  NOT NULL DEFAULT 1,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subject_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE students (
    student_id  INT          AUTO_INCREMENT PRIMARY KEY,
    reg_number  VARCHAR(20)  NOT NULL UNIQUE,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    phone       VARCHAR(20),
    course_id   INT          NOT NULL,
    enrolled_year YEAR       NOT NULL,
    is_active   TINYINT(1)  NOT NULL DEFAULT 1,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_student_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE lecturers (
    lecturer_id   INT          AUTO_INCREMENT PRIMARY KEY,
    user_id       INT          NOT NULL UNIQUE,   
    employee_code VARCHAR(20)  NOT NULL UNIQUE,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(20),
    department    VARCHAR(100),
    is_active     TINYINT(1)  NOT NULL DEFAULT 1,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_lecturer_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE lecturer_subjects (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    lecturer_id INT NOT NULL,
    subject_id  INT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY uq_lecturer_subject (lecturer_id, subject_id),

    CONSTRAINT fk_ls_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    CONSTRAINT fk_ls_subject
        FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE class_sessions (
    session_id    INT          AUTO_INCREMENT PRIMARY KEY,
    subject_id    INT          NOT NULL,
    lecturer_id   INT          NOT NULL,
    session_date  DATE         NOT NULL,
    start_time    TIME         NOT NULL,
    end_time      TIME         NOT NULL,
    venue         VARCHAR(100),
    notes         TEXT,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cs_subject
        FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_cs_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE attendance (
    attendance_id INT       AUTO_INCREMENT PRIMARY KEY,
    session_id    INT       NOT NULL,
    student_id    INT       NOT NULL,
    status        ENUM('PRESENT','ABSENT','LATE') NOT NULL DEFAULT 'ABSENT',
    marked_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks       VARCHAR(255),

    UNIQUE KEY uq_session_student (session_id, student_id),

    CONSTRAINT fk_att_session
        FOREIGN KEY (session_id) REFERENCES class_sessions(session_id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    CONSTRAINT fk_att_student
        FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO users (username, password, role, full_name, email) VALUES
('admin',       'admin123',    'ADMIN',    'System Administrator', 'admin@sams.lk'),
('lec_silva',   'silva123',    'LECTURER', 'Dr. Nuwan Silva',      'n.silva@sams.lk'),
('lec_perera',  'perera123',   'LECTURER', 'Ms. Dilini Perera',    'd.perera@sams.lk'),
('lec_fernando','fernando123', 'LECTURER', 'Mr. Kasun Fernando',   'k.fernando@sams.lk');


INSERT INTO courses (course_code, course_name, duration_years, description) VALUES
('BSC-CS',  'BSc (Hons) Computer Science',            3, 'Undergraduate degree in Computer Science'),
('BSC-IT',  'BSc (Hons) Information Technology',      3, 'Undergraduate degree in Information Technology'),
('HND-SE',  'HND in Software Engineering',            2, 'Higher National Diploma in Software Engineering');


INSERT INTO subjects (course_id, subject_code, subject_name, credits, semester) VALUES
(1, 'CS101', 'Introduction to Programming',       3, 1),
(1, 'CS102', 'Object-Oriented Programming',       3, 2),
(1, 'CS103', 'Data Structures and Algorithms',    3, 3),
(1, 'CS104', 'Database Management Systems',       3, 3),
(1, 'CS105', 'Software Engineering Principles',   3, 4);


INSERT INTO subjects (course_id, subject_code, subject_name, credits, semester) VALUES
(2, 'IT101', 'Computer Networks',                 3, 1),
(2, 'IT102', 'Web Development Fundamentals',      3, 2),
(2, 'IT103', 'Cybersecurity Basics',              3, 3);


INSERT INTO subjects (course_id, subject_code, subject_name, credits, semester) VALUES
(3, 'SE101', 'Programming Fundamentals',          3, 1),
(3, 'SE102', 'Agile Development',                 3, 2);


INSERT INTO lecturers (user_id, employee_code, first_name, last_name, email, phone, department) VALUES
(2, 'EMP001', 'Nuwan',  'Silva',    'n.silva@sams.lk',    '0771234567', 'Computer Science'),
(3, 'EMP002', 'Dilini', 'Perera',   'd.perera@sams.lk',   '0779876543', 'Information Technology'),
(4, 'EMP003', 'Kasun',  'Fernando', 'k.fernando@sams.lk', '0765551234', 'Software Engineering');


INSERT INTO lecturer_subjects (lecturer_id, subject_id) VALUES
(1, 1), 
(1, 2), 
(1, 3), 
(2, 6), 
(2, 7), 
(3, 9), 
(3, 10);


INSERT INTO students (reg_number, first_name, last_name, email, phone, course_id, enrolled_year) VALUES
('CS2024001', 'Ashan',    'Jayawardena', 'ashan.j@student.sams.lk',   '0701234001', 1, 2026),
('CS2024002', 'Nethmi',   'Rathnayake',  'nethmi.r@student.sams.lk',  '0701234002', 1, 2026),
('CS2024003', 'Pasindu',  'Gunawardena', 'pasindu.g@student.sams.lk', '0701234003', 1, 2026),
('CS2024004', 'Sachini',  'Wickramasinghe','sachini.w@student.sams.lk','0701234004', 1, 2026),
('CS2024005', 'Tharaka',  'Bandara',     'tharaka.b@student.sams.lk', '0701234005', 1, 2026),
('IT2024001', 'Haritha',  'Madusanka',   'haritha.m@student.sams.lk', '0701234006', 2, 2026),
('IT2024002', 'Kavindi',  'Seneviratne', 'kavindi.s@student.sams.lk', '0701234007', 2, 2026),
('IT2024003', 'Lahiru',   'Dissanayake', 'lahiru.d@student.sams.lk',  '0701234008', 2, 2026),
('SE2024001', 'Malith',   'Ranasinghe',  'malith.r@student.sams.lk',  '0701234009', 3, 2026),
('SE2024002', 'Nimesha',  'Weerasinghe', 'nimesha.w@student.sams.lk', '0701234010', 3, 2026);


INSERT INTO class_sessions (subject_id, lecturer_id, session_date, start_time, end_time, venue) VALUES
(2, 1, '2026-05-15', '09:00:00', '11:00:00', 'Lab A'),
(2, 1, '2026-05-08', '09:00:00', '11:00:00', 'Lab A'),
(2, 1, '2026-05-01', '09:00:00', '11:00:00', 'Lab A'),
(3, 1, '2026-05-06', '13:00:00', '15:00:00', 'Room 201'),
(3, 1, '2026-05-05', '13:00:00', '15:00:00', 'Room 201'),
(6, 2, '2026-05-19', '10:00:00', '12:00:00', 'Room 301'),
(6, 2, '2026-05-18', '10:00:00', '12:00:00', 'Room 301'),
(9, 3, '2026-05-04', '14:00:00', '16:00:00', 'Lab B'),
(9, 3, '2026-05-11', '14:00:00', '16:00:00', 'Lab B');


INSERT INTO attendance (session_id, student_id, status) VALUES
(1, 1, 'PRESENT'),
(1, 2, 'PRESENT'),
(1, 3, 'ABSENT'),
(1, 4, 'LATE'),
(1, 5, 'PRESENT');


INSERT INTO attendance (session_id, student_id, status) VALUES
(2, 1, 'PRESENT'),
(2, 2, 'LATE'),
(2, 3, 'PRESENT'),
(2, 4, 'PRESENT'),
(2, 5, 'ABSENT');


INSERT INTO attendance (session_id, student_id, status) VALUES
(3, 1, 'ABSENT'),
(3, 2, 'PRESENT'),
(3, 3, 'PRESENT'),
(3, 4, 'PRESENT'),
(3, 5, 'PRESENT');


INSERT INTO attendance (session_id, student_id, status) VALUES
(4, 1, 'PRESENT'),
(4, 2, 'PRESENT'),
(4, 3, 'LATE'),
(4, 4, 'ABSENT'),
(4, 5, 'PRESENT');


INSERT INTO attendance (session_id, student_id, status) VALUES
(6, 6, 'PRESENT'),
(6, 7, 'PRESENT'),
(6, 8, 'ABSENT');


INSERT INTO attendance (session_id, student_id, status) VALUES
(8, 9, 'PRESENT'),
(8, 10, 'LATE');



CREATE VIEW vw_attendance_summary AS
SELECT
    s.student_id,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    s.reg_number,
    sub.subject_id,
    sub.subject_name,
    sub.subject_code,
    COUNT(a.attendance_id)                              AS total_sessions,
    SUM(a.status = 'PRESENT')                          AS present_count,
    SUM(a.status = 'ABSENT')                           AS absent_count,
    SUM(a.status = 'LATE')                             AS late_count,
    ROUND(SUM(a.status = 'PRESENT') * 100.0 / COUNT(a.attendance_id), 1) AS attendance_pct
FROM attendance a
JOIN class_sessions cs  ON a.session_id  = cs.session_id
JOIN students s         ON a.student_id  = s.student_id
JOIN subjects sub       ON cs.subject_id = sub.subject_id
GROUP BY s.student_id, sub.subject_id;


CREATE VIEW vw_attendance_detail AS
SELECT
    a.attendance_id,
    cs.session_date,
    cs.start_time,
    cs.venue,
    sub.subject_code,
    sub.subject_name,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    s.reg_number,
    c.course_name,
    a.status,
    a.remarks,
    CONCAT(l.first_name, ' ', l.last_name) AS lecturer_name
FROM attendance a
JOIN class_sessions cs  ON a.session_id  = cs.session_id
JOIN students s         ON a.student_id  = s.student_id
JOIN subjects sub       ON cs.subject_id = sub.subject_id
JOIN courses c          ON s.course_id   = c.course_id
JOIN lecturers l        ON cs.lecturer_id= l.lecturer_id;
