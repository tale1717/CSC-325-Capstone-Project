package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ClassScheduler {
    @FXML
    private Button suggestClassButton;

    @FXML
    private Button searchClassButton;

    @FXML
    private Button backButton;

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleSuggestClass(){
        Main.setRoot("class-scheduler.fxml", suggestClassButton);
    }

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleSearchClass() {
        Main.setRoot("admin-class-view.fxml", searchClassButton);
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }
}
