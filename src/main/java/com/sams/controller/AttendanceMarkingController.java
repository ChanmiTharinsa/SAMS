/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.Attendance;
import com.sams.model.ClassSession;
import com.sams.service.AttendanceService;
import com.sams.service.ClassSessionService;
import com.sams.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

/**
 *
 * @author User
 */
public class AttendanceMarkingController {

    // Session selector table
    @FXML private TableView<ClassSession> sessionTable;
    @FXML private TableColumn<ClassSession, String> colDate;
    @FXML private TableColumn<ClassSession, String> colSubject;
    @FXML private TableColumn<ClassSession, String> colStart;
    @FXML private TableColumn<ClassSession, String> colEnd;
    @FXML private TableColumn<ClassSession, String> colVenue;

    // Attendance marking table
    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, String> colRegNumber;
    @FXML private TableColumn<Attendance, String> colStudentName;
    @FXML private TableColumn<Attendance, String> colStatus;

    @FXML private Label sessionInfoLabel;
    @FXML private Label messageLabel;

    private final ClassSessionService sessionService = new ClassSessionService();
    private final AttendanceService attendanceService = new AttendanceService();

    private int currentLecturerId = -1;

    @FXML
    public void initialize() {
        setupSessionTable();
        setupAttendanceTable();
        loadSessionsForLecturer();
        setupSessionSelectionListener();
    }

    private void setupSessionTable() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDateStr"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTimeStr"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTimeStr"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
    }

    
    private void setupAttendanceTable() {
        colRegNumber.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        
        attendanceTable.setEditable(true);
        colStatus.setEditable(true);

        ObservableList<String> statusOptions =
                FXCollections.observableArrayList("PRESENT", "ABSENT", "LATE");

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colStatus.setCellFactory(
                ComboBoxTableCell.forTableColumn(statusOptions)
        );

        
        colStatus.setOnEditCommit(event -> {
            Attendance attendance = event.getRowValue();
            attendance.setStatus(event.getNewValue());
        });
    }

    
    private void loadSessionsForLecturer() {
        if (SessionManager.isAdmin()) {
            
            ObservableList<ClassSession> sessions =
                    FXCollections.observableArrayList(sessionService.getAllSessions());
            sessionTable.setItems(sessions);
        } else {
            
            ObservableList<ClassSession> sessions =
                    FXCollections.observableArrayList(sessionService.getAllSessions());
            sessionTable.setItems(sessions);
        }
    }

    private void setupSessionSelectionListener() {
        sessionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        loadAttendanceForSession(newSelection);
                    }
                }
        );
    }

    
    private void loadAttendanceForSession(ClassSession session) {
        sessionInfoLabel.setText(
                "Session: " + session.getSubjectName()
                + " | Date: " + session.getSessionDateStr()
                + " | Venue: " + session.getVenue()
        );

        ObservableList<Attendance> attendanceList =
                FXCollections.observableArrayList(
                        attendanceService.getAttendanceForSession(session.getSessionId())
                );

        attendanceTable.setItems(attendanceList);
        messageLabel.setText("");
    }

    @FXML
    private void handleSave() {
        ObservableList<Attendance> attendanceList = attendanceTable.getItems();

        if (attendanceList == null || attendanceList.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("No attendance data to save. Select a session first.");
            return;
        }

        String result = attendanceService.saveAttendance(attendanceList);

        if (result.equals("SUCCESS")) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Attendance saved successfully.");
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(result);
        }
    }

    @FXML
    private void handleBack() {
        try {
            String fxmlPath = SessionManager.isAdmin()
                    ? "/com/sams/view/AdminDashboard.fxml"
                    : "/com/sams/view/LecturerDashboard.fxml";

            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) sessionTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

