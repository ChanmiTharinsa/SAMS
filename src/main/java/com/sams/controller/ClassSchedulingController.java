/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.ClassSession;
import com.sams.model.Lecturer;
import com.sams.model.Subject;
import com.sams.service.ClassSessionService;
import com.sams.service.LecturerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 *
 * @author User
 */
public class ClassSchedulingController {

    @FXML private TableView<ClassSession> sessionTable;
    @FXML private TableColumn<ClassSession, String> colDate;
    @FXML private TableColumn<ClassSession, String> colSubject;
    @FXML private TableColumn<ClassSession, String> colLecturer;
    @FXML private TableColumn<ClassSession, String> colStart;
    @FXML private TableColumn<ClassSession, String> colEnd;
    @FXML private TableColumn<ClassSession, String> colVenue;

    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private ComboBox<Lecturer> lecturerComboBox;
    @FXML private DatePicker sessionDatePicker;
    @FXML private TextField venueField;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private Label formMessageLabel;

    private final ClassSessionService sessionService = new ClassSessionService();
    private final LecturerService lecturerService = new LecturerService();

    private ClassSession selectedSession;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadSubjectOptions();
        loadLecturerOptions();
        loadSessionData();
        setupRowSelectionListener();
    }

    private void setupTableColumns() {
        
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDateStr"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturerName"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTimeStr"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTimeStr"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
    }

    private void loadSubjectOptions() {
        ObservableList<Subject> subjects =
                FXCollections.observableArrayList(sessionService.getAllSubjects());
        subjectComboBox.setItems(subjects);
    }

    private void loadLecturerOptions() {
        ObservableList<Lecturer> lecturers =
                FXCollections.observableArrayList(lecturerService.getAllLecturers());
        lecturerComboBox.setItems(lecturers);
    }

    private void loadSessionData() {
        ObservableList<ClassSession> sessions =
                FXCollections.observableArrayList(sessionService.getAllSessions());
        sessionTable.setItems(sessions);
    }

    private void setupRowSelectionListener() {
        sessionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedSession = newSelection;
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(ClassSession session) {
        
        for (Subject subject : subjectComboBox.getItems()) {
            if (subject.getSubjectId() == session.getSubjectId()) {
                subjectComboBox.getSelectionModel().select(subject);
                break;
            }
        }

        
        for (Lecturer lecturer : lecturerComboBox.getItems()) {
            if (lecturer.getLecturerId() == session.getLecturerId()) {
                lecturerComboBox.getSelectionModel().select(lecturer);
                break;
            }
        }

        sessionDatePicker.setValue(session.getSessionDate());
        startTimeField.setText(session.getStartTimeStr());
        endTimeField.setText(session.getEndTimeStr());
        venueField.setText(session.getVenue());
        formMessageLabel.setText("");
    }


    @FXML
    private void handleAdd() {
        ClassSession session = buildSessionFromForm(false);
        if (session == null) return;

        String result = sessionService.addSession(session);

        if (result.equals("SUCCESS")) {
            showMessage("Session scheduled successfully.", true);
            loadSessionData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedSession == null) {
            showMessage("Select a session from the table first.", false);
            return;
        }

        ClassSession session = buildSessionFromForm(true);
        if (session == null) return;

        String result = sessionService.updateSession(session);

        if (result.equals("SUCCESS")) {
            showMessage("Session updated successfully.", true);
            loadSessionData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedSession == null) {
            showMessage("Select a session from the table first.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this session? This will also delete all attendance records for it.");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = sessionService.deleteSession(selectedSession.getSessionId());

                if (result.equals("SUCCESS")) {
                    showMessage("Session deleted.", true);
                    loadSessionData();
                    handleClear();
                } else {
                    showMessage(result, false);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        subjectComboBox.getSelectionModel().clearSelection();
        lecturerComboBox.getSelectionModel().clearSelection();
        sessionDatePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        venueField.clear();
        formMessageLabel.setText("");
        selectedSession = null;
        sessionTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AdminDashboard.fxml")
            );
            Stage stage = (Stage) sessionTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Admin Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String text, boolean success) {
        formMessageLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        formMessageLabel.setText(text);
    }

    
    private ClassSession buildSessionFromForm(boolean isUpdate) {
        Subject selectedSubject = subjectComboBox.getSelectionModel().getSelectedItem();
        Lecturer selectedLecturer = lecturerComboBox.getSelectionModel().getSelectedItem();

        if (selectedSubject == null) {
            showMessage("Please select a subject.", false);
            return null;
        }
        if (selectedLecturer == null) {
            showMessage("Please select a lecturer.", false);
            return null;
        }
        if (sessionDatePicker.getValue() == null) {
            showMessage("Please select a date.", false);
            return null;
        }

        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(startTimeField.getText().trim());
        } catch (DateTimeParseException e) {
            showMessage("Start time must be in HH:MM format (e.g. 09:00).", false);
            return null;
        }

        try {
            endTime = LocalTime.parse(endTimeField.getText().trim());
        } catch (DateTimeParseException e) {
            showMessage("End time must be in HH:MM format (e.g. 11:00).", false);
            return null;
        }

        ClassSession session = new ClassSession();
        session.setSubjectId(selectedSubject.getSubjectId());
        session.setLecturerId(selectedLecturer.getLecturerId());
        session.setSessionDate(sessionDatePicker.getValue());
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setVenue(venueField.getText());
        session.setNotes("");

        if (isUpdate && selectedSession != null) {
            session.setSessionId(selectedSession.getSessionId());
        }

        return session;
    }
}

