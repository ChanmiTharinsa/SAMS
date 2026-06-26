/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.User;
import com.sams.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author User
 */
public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User current = SessionManager.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Welcome, " + current.getFullName() + " (Admin)");
        }
    }

    @FXML
    private void handleCourseManagement() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/CourseManagement.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Course Management");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStudentManagement() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/StudentManagement.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Student Management");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLecturerManagement() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/LecturerManagement.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Lecturer Management");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClassScheduling() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/ClassScheduling.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Class Scheduling");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAttendanceMarking() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AttendanceMarking.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Attendance Marking");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAttendanceReports() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AttendanceReport.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Attendance Reports");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.clear();

        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/Login.fxml")
            );
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Student Attendance Management System");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



