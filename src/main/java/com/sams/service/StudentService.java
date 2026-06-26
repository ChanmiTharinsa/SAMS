/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.StudentDAO;
import com.sams.model.Student;

import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class StudentService {

    private final StudentDAO studentDAO;

    
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    public Student getStudentById(int studentId) {
        return studentDAO.findById(studentId);
    }

    public String addStudent(Student student) {
        String validationError = validate(student);
        if (validationError != null) {
            return validationError;
        }

        if (studentDAO.existsByRegNumber(student.getRegNumber())) {
            return "A student with this registration number already exists.";
        }

        int newId = studentDAO.insert(student);
        if (newId == -1) {
            return "Failed to save student. Please try again.";
        }

        return "SUCCESS";
    }

    public String updateStudent(Student student) {
        String validationError = validate(student);
        if (validationError != null) {
            return validationError;
        }

        boolean updated = studentDAO.update(student);
        if (!updated) {
            return "Failed to update student. Please try again.";
        }

        return "SUCCESS";
    }

    public String deleteStudent(int studentId) {
        boolean deleted = studentDAO.softDelete(studentId);
        if (!deleted) {
            return "Failed to delete student.";
        }
        return "SUCCESS";
    }

    private String validate(Student student) {
        if (student.getRegNumber() == null || student.getRegNumber().isBlank()) {
            return "Registration number is required.";
        }
        if (student.getFirstName() == null || student.getFirstName().isBlank()) {
            return "First name is required.";
        }
        if (student.getLastName() == null || student.getLastName().isBlank()) {
            return "Last name is required.";
        }
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            return "A valid email address is required.";
        }
        if (student.getCourseId() <= 0) {
            return "Please select a course.";
        }
        if (student.getEnrolledYear() < 2000 || student.getEnrolledYear() > 2100) {
            return "Enrolled year looks invalid.";
        }
        return null;
    }
}

