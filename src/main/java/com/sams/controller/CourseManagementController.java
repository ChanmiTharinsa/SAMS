/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.Course;
import com.sams.service.CourseService;
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
public class CourseManagementController {

    // Table and its columns 
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colCourseCode;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, Integer> colDuration;
    @FXML private TableColumn<Course, String> colDescription;

    // Form fields
    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private TextField durationField;
    @FXML private TextArea descriptionField;
    @FXML private Label formMessageLabel;

    // Buttons
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    private final CourseService courseService = new CourseService();

   
    private Course selectedCourse;

    
    @FXML
    public void initialize() {
        setupTableColumns();
        loadCourseData();
        setupRowSelectionListener();
    }

    
    private void setupTableColumns() {
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationYears"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    
    private void loadCourseData() {
        ObservableList<Course> courses =
                FXCollections.observableArrayList(courseService.getAllCourses());
        courseTable.setItems(courses);
    }

    
    private void setupRowSelectionListener() {
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedCourse = newSelection;
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(Course course) {
        courseCodeField.setText(course.getCourseCode());
        courseNameField.setText(course.getCourseName());
        durationField.setText(String.valueOf(course.getDurationYears()));
        descriptionField.setText(course.getDescription());
        formMessageLabel.setText("");
    }

    

    @FXML
    private void handleAdd() {
        Course course = buildCourseFromForm(false);
        if (course == null) {
            return; 
        }

        String result = courseService.addCourse(course);

        if (result.equals("SUCCESS")) {
            formMessageLabel.setStyle("-fx-text-fill: green;");
            formMessageLabel.setText("Course added successfully.");
            loadCourseData();
            handleClear();
        } else {
            formMessageLabel.setStyle("-fx-text-fill: red;");
            formMessageLabel.setText(result);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedCourse == null) {
            formMessageLabel.setStyle("-fx-text-fill: red;");
            formMessageLabel.setText("Select a course from the table first.");
            return;
        }

        Course course = buildCourseFromForm(true);
        if (course == null) {
            return;
        }

        String result = courseService.updateCourse(course);

        if (result.equals("SUCCESS")) {
            formMessageLabel.setStyle("-fx-text-fill: green;");
            formMessageLabel.setText("Course updated successfully.");
            loadCourseData();
            handleClear();
        } else {
            formMessageLabel.setStyle("-fx-text-fill: red;");
            formMessageLabel.setText(result);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCourse == null) {
            formMessageLabel.setStyle("-fx-text-fill: red;");
            formMessageLabel.setText("Select a course from the table first.");
            return;
        }

        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete course \"" + selectedCourse.getCourseName() + "\"?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String result = courseService.deleteCourse(selectedCourse.getCourseId());

                if (result.equals("SUCCESS")) {
                    formMessageLabel.setStyle("-fx-text-fill: green;");
                    formMessageLabel.setText("Course deleted.");
                    loadCourseData();
                    handleClear();
                } else {
                    formMessageLabel.setStyle("-fx-text-fill: red;");
                    formMessageLabel.setText(result);
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        courseCodeField.clear();
        courseNameField.clear();
        durationField.clear();
        descriptionField.clear();
        formMessageLabel.setText("");
        selectedCourse = null;
        courseTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/sams/view/AdminDashboard.fxml")
            );
            Stage stage = (Stage) courseTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - Admin Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param isUpdate if true, keeps the existing selectedCourse's ID so the Service knows which row to update.
     */
    private Course buildCourseFromForm(boolean isUpdate) {
        String code = courseCodeField.getText();
        String name = courseNameField.getText();
        String durationText = durationField.getText();
        String description = descriptionField.getText();

        int duration;
        try {
            duration = Integer.parseInt(durationText.trim());
        } catch (NumberFormatException e) {
            formMessageLabel.setStyle("-fx-text-fill: red;");
            formMessageLabel.setText("Duration must be a whole number.");
            return null;
        }

        Course course = new Course();
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setDurationYears(duration);
        course.setDescription(description);
        course.setActive(true);

        if (isUpdate && selectedCourse != null) {
            course.setCourseId(selectedCourse.getCourseId());
        }

        return course;
    }
}

