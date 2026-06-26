/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.controller;

import com.sams.model.User;
import com.sams.service.AuthService;
import com.sams.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author User
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.login(username, password);

        if (user == null) {
            messageLabel.setText("Invalid username or password.");
            return;
        }

        // Save the logged-in user for the rest of the session
        SessionManager.setCurrentUser(user);

        // Route to the correct dashboard based on role
        String fxmlPath = user.isAdmin()
                ? "/com/sams/view/AdminDashboard.fxml"
                : "/com/sams/view/LecturerDashboard.fxml";

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SAMS - " + (user.isAdmin() ? "Admin" : "Lecturer") + " Dashboard");
        } catch (IOException e) {
            messageLabel.setText("Error loading dashboard.");
            e.printStackTrace();
        }
    }
}
