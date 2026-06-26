/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.service;

import com.sams.dao.LecturerDAO;
import com.sams.model.Lecturer;

import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class LecturerService {

    private final LecturerDAO lecturerDAO;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public LecturerService() {
        this.lecturerDAO = new LecturerDAO();
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerDAO.findAll();
    }

    public Lecturer getLecturerById(int lecturerId) {
        return lecturerDAO.findById(lecturerId);
    }

    
    public String addLecturer(Lecturer lecturer, String username, String password) {
        String validationError = validate(lecturer);
        if (validationError != null) {
            return validationError;
        }

        if (username == null || username.isBlank()) {
            return "Username is required.";
        }
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters.";
        }

        if (lecturerDAO.existsByEmployeeCode(lecturer.getEmployeeCode())) {
            return "A lecturer with this employee code already exists.";
        }

        int newId = lecturerDAO.insert(lecturer, username, password);
        if (newId == -1) {
            return "Failed to save lecturer. Username may already be taken.";
        }

        return "SUCCESS";
    }

    public String updateLecturer(Lecturer lecturer) {
        String validationError = validate(lecturer);
        if (validationError != null) {
            return validationError;
        }

        boolean updated = lecturerDAO.update(lecturer);
        if (!updated) {
            return "Failed to update lecturer. Please try again.";
        }

        return "SUCCESS";
    }

    public String deleteLecturer(int lecturerId, int userId) {
        boolean deleted = lecturerDAO.softDelete(lecturerId, userId);
        if (!deleted) {
            return "Failed to delete lecturer.";
        }
        return "SUCCESS";
    }

    private String validate(Lecturer lecturer) {
        if (lecturer.getEmployeeCode() == null || lecturer.getEmployeeCode().isBlank()) {
            return "Employee code is required.";
        }
        if (lecturer.getFirstName() == null || lecturer.getFirstName().isBlank()) {
            return "First name is required.";
        }
        if (lecturer.getLastName() == null || lecturer.getLastName().isBlank()) {
            return "Last name is required.";
        }
        if (lecturer.getEmail() == null || !EMAIL_PATTERN.matcher(lecturer.getEmail()).matches()) {
            return "A valid email address is required.";
        }
        if (lecturer.getDepartment() == null || lecturer.getDepartment().isBlank()) {
            return "Department is required.";
        }
        return null;
    }
}
