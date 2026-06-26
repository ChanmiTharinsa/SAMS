/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.Lecturer;
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

/**
 *
 * @author User
 */
public class LecturerManagementController {

    @FXML private TableView<Lecturer> lecturerTable;
    @FXML private TableColumn<Lecturer, String> colEmpCode;
    @FXML private TableColumn<Lecturer, String> colFirstName;
    @FXML private TableColumn<Lecturer, String> colLastName;
    @FXML private TableColumn<Lecturer, String> colEmail;
    @FXML private TableColumn<Lecturer, String> colDepartment;
    @FXML private TableColumn<Lecturer, String> colUsername;

    @FXML private TextField empCodeField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField departmentField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label formMessageLabel;

    private final LecturerService lecturerService = new LecturerService();
    private Lecturer selectedLecturer;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadLecturerData();
        setupRowSelectionListener();
    }

    private void setupTableColumns() {
        colEmpCode.setCellValueFactory(new PropertyValueFactory<>("employeeCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadLecturerData() {
        ObservableList<Lecturer> lecturers =
                FXCollections.observableArrayList(lecturerService.getAllLecturers());
        lecturerTable.setItems(lecturers);
    }

    private void setupRowSelectionListener() {
        lecturerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedLecturer = newSelection;
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(Lecturer lecturer) {
        empCodeField.setText(lecturer.getEmployeeCode());
        firstNameField.setText(lecturer.getFirstName());
        lastNameField.setText(lecturer.getLastName());
        emailField.setText(lecturer.getEmail());
        phoneField.setText(lecturer.getPhone());
        departmentField.setText(lecturer.getDepartment());
        usernameField.setText(lecturer.getUsername());
        passwordField.clear();
        formMessageLabel.setText("");
    }

    @FXML
    private void handleAdd() {
        Lecturer lecturer = buildLecturerFromForm();
        if (lecturer == null) return;

        String username = usernameField.getText();
        String password = passwordField.getText();

        String result = lecturerService.addLecturer(lecturer, username, password);

        if (result.equals("SUCCESS")) {
            showMessage("Lecturer added successfully.", true);
            loadLecturerData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedLecturer == null) {
            showMessage("Select a lecturer from the table first.", false);
            return;
        }

        Lecturer lecturer = buildLecturerFromForm();
        if (lecturer == null) return;

        lecturer.setLecturerId(selectedLecturer.getLecturerId());

        String result = lecturerService.updateLecturer(lecturer);

        if (result.equals("SUCCESS")) {
            showMessage("Lecturer updated successfully.", true);
            loadLecturerData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedLecturer == null) {
            showMessage("Select a lecturer from the table first.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete lecturer \"" + selectedLecturer.getFullName() + "\"?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = lecturerService.deleteLecturer(
                        selectedLecturer.getLecturerId(),
                        selectedLecturer.getUserId()
                );

                if (result.equals("SUCCESS")) {
                    showMessage("Lecturer deleted.", true);
                    loadLecturerData();
                    handleClear();
                } else {
                    showMessage(result, false);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        empCodeField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        departmentField.clear();
        usernameField.clear();
        passwordField.clear();
        formMessageLabel.setText("");
        selectedLecturer = null;
        lecturerTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AdminDashboard.fxml")
            );
            Stage stage = (Stage) lecturerTable.getScene().getWindow();
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

    private Lecturer buildLecturerFromForm() {
        Lecturer lecturer = new Lecturer();
        lecturer.setEmployeeCode(empCodeField.getText());
        lecturer.setFirstName(firstNameField.getText());
        lecturer.setLastName(lastNameField.getText());
        lecturer.setEmail(emailField.getText());
        lecturer.setPhone(phoneField.getText());
        lecturer.setDepartment(departmentField.getText());
        lecturer.setActive(true);
        return lecturer;
    }
}

