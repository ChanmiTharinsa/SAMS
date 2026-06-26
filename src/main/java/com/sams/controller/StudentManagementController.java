/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.Course;
import com.sams.model.Student;
import com.sams.service.CourseService;
import com.sams.service.StudentService;
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
public class StudentManagementController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colRegNumber;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, Integer> colYear;

    @FXML private TextField regNumberField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField enrolledYearField;
    @FXML private ComboBox<Course> courseComboBox;
    @FXML private Label formMessageLabel;

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();

    private Student selectedStudent;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCourseOptions();
        loadStudentData();
        setupRowSelectionListener();
    }

    private void setupTableColumns() {
        colRegNumber.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("enrolledYear"));
    }

   
    private void loadCourseOptions() {
        ObservableList<Course> courses =
                FXCollections.observableArrayList(courseService.getAllCourses());
        courseComboBox.setItems(courses);
    }

    private void loadStudentData() {
        ObservableList<Student> students =
                FXCollections.observableArrayList(studentService.getAllStudents());
        studentTable.setItems(students);
    }

    private void setupRowSelectionListener() {
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedStudent = newSelection;
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(Student student) {
        regNumberField.setText(student.getRegNumber());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        enrolledYearField.setText(String.valueOf(student.getEnrolledYear()));

        
        for (Course course : courseComboBox.getItems()) {
            if (course.getCourseId() == student.getCourseId()) {
                courseComboBox.getSelectionModel().select(course);
                break;
            }
        }

        formMessageLabel.setText("");
    }


    @FXML
    private void handleAdd() {
        Student student = buildStudentFromForm(false);
        if (student == null) {
            return;
        }

        String result = studentService.addStudent(student);

        if (result.equals("SUCCESS")) {
            showMessage("Student added successfully.", true);
            loadStudentData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedStudent == null) {
            showMessage("Select a student from the table first.", false);
            return;
        }

        Student student = buildStudentFromForm(true);
        if (student == null) {
            return;
        }

        String result = studentService.updateStudent(student);

        if (result.equals("SUCCESS")) {
            showMessage("Student updated successfully.", true);
            loadStudentData();
            handleClear();
        } else {
            showMessage(result, false);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedStudent == null) {
            showMessage("Select a student from the table first.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete student \"" + selectedStudent.getFullName() + "\"?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = studentService.deleteStudent(selectedStudent.getStudentId());

                if (result.equals("SUCCESS")) {
                    showMessage("Student deleted.", true);
                    loadStudentData();
                    handleClear();
                } else {
                    showMessage(result, false);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        regNumberField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        enrolledYearField.clear();
        courseComboBox.getSelectionModel().clearSelection();
        formMessageLabel.setText("");
        selectedStudent = null;
        studentTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AdminDashboard.fxml")
            );
            Stage stage = (Stage) studentTable.getScene().getWindow();
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

    
    private Student buildStudentFromForm(boolean isUpdate) {
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showMessage("Please select a course.", false);
            return null;
        }

        int enrolledYear;
        try {
            enrolledYear = Integer.parseInt(enrolledYearField.getText().trim());
        } catch (NumberFormatException e) {
            showMessage("Enrolled year must be a whole number.", false);
            return null;
        }

        Student student = new Student();
        student.setRegNumber(regNumberField.getText());
        student.setFirstName(firstNameField.getText());
        student.setLastName(lastNameField.getText());
        student.setEmail(emailField.getText());
        student.setPhone(phoneField.getText());
        student.setCourseId(selectedCourse.getCourseId());
        student.setEnrolledYear(enrolledYear);
        student.setActive(true);

        if (isUpdate && selectedStudent != null) {
            student.setStudentId(selectedStudent.getStudentId());
        }

        return student;
    }
}
