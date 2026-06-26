/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams;

/**
 *
 * @author User
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import com.sams.util.DBConnection;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/sams/view/Login.fxml")
        );

        Scene scene = new Scene(root);

        primaryStage.setTitle("SAMS - Student Attendance Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        
        DBConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
