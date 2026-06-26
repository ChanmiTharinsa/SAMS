/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.dao.ReportDAO.ReportRecord;
import com.sams.model.Student;
import com.sams.model.Subject;
import com.sams.service.ReportService;
import com.sams.service.StudentService;
import com.sams.service.ClassSessionService;
import com.sams.util.SessionManager;
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
import java.util.List;

/**
 *
 * @author User
 */
public class AttendanceReportController {

    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private Label filterMessageLabel;
    @FXML private Label summaryLabel;

    @FXML private TableView<ReportRecord> reportTable;
    @FXML private TableColumn<ReportRecord, String> colDate;
    @FXML private TableColumn<ReportRecord, String> colRegNumber;
    @FXML private TableColumn<ReportRecord, String> colStudentName;
    @FXML private TableColumn<ReportRecord, String> colCourse;
    @FXML private TableColumn<ReportRecord, String> colSubject;
    @FXML private TableColumn<ReportRecord, String> colStatus;
    @FXML private TableColumn<ReportRecord, String> colLecturer;

    private final ReportService reportService = new ReportService();
    private final StudentService studentService = new StudentService();
    private final ClassSessionService sessionService = new ClassSessionService();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadFilterOptions();
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colRegNumber.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturerName"));
    }

    private void loadFilterOptions() {
        // Student ComboBox
        ObservableList<Student> students =
                FXCollections.observableArrayList(studentService.getAllStudents());
        studentComboBox.setItems(students);
        studentComboBox.setPromptText("All Students");

        // Subject ComboBox
        ObservableList<Subject> subjects =
                FXCollections.observableArrayList(sessionService.getAllSubjects());
        subjectComboBox.setItems(subjects);
        subjectComboBox.setPromptText("All Subjects");
    }

    @FXML
    private void handleGenerate() {
        filterMessageLabel.setText("");

        
        if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null
                && fromDatePicker.getValue().isAfter(toDatePicker.getValue())) {
            filterMessageLabel.setText("'From' date cannot be after 'To' date.");
            return;
        }

        
        Integer studentId = studentComboBox.getSelectionModel().getSelectedItem() != null
                ? studentComboBox.getSelectionModel().getSelectedItem().getStudentId()
                : null;

        Integer subjectId = subjectComboBox.getSelectionModel().getSelectedItem() != null
                ? subjectComboBox.getSelectionModel().getSelectedItem().getSubjectId()
                : null;

        List<ReportRecord> results = reportService.generateReport(
                studentId,
                subjectId,
                fromDatePicker.getValue(),
                toDatePicker.getValue()
        );

        ObservableList<ReportRecord> data = FXCollections.observableArrayList(results);
        reportTable.setItems(data);

        
        long present = results.stream().filter(r -> "PRESENT".equals(r.getStatus())).count();
        long absent  = results.stream().filter(r -> "ABSENT".equals(r.getStatus())).count();
        long late    = results.stream().filter(r -> "LATE".equals(r.getStatus())).count();

        summaryLabel.setText(
                "Total Records: " + results.size()
                + "   |   Present: " + present
                + "   |   Absent: " + absent
                + "   |   Late: " + late
        );
    }

    @FXML
    private void handleClear() {
        studentComboBox.getSelectionModel().clearSelection();
        subjectComboBox.getSelectionModel().clearSelection();
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        filterMessageLabel.setText("");
        reportTable.getItems().clear();
        summaryLabel.setText("Generate a report to see results.");
    }

    @FXML
    private void handleBack() {
        try {
            String fxmlPath = SessionManager.isAdmin()
                    ? "/com/sams/view/AdminDashboard.fxml"
                    : "/com/sams/view/LecturerDashboard.fxml";

            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) reportTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
