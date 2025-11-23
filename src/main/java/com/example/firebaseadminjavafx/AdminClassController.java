package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminClassController {
    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleAddClass(){
        Main.setRoot("class-scheduler.fxml", addButton);
    }

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleEditClass() {
        Main.setRoot("admin-class-view.fxml", editButton);
    }

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleRemoveClass(){
        Main.setRoot("admin-class-view.fxml", deleteButton);
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }
}
