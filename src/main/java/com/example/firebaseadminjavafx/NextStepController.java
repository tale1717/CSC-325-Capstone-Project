package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NextStepController {

    @FXML private Label emailValue;
    @FXML private Button goGymHomeButton;
    @FXML private Button enterInfoButton;
    @FXML private Button returnToWelcomeButton;

    @FXML
    private void initialize() {
        emailValue.setText(Main.currentUserEmail != null ? Main.currentUserEmail : "(unknown)");
    }

    @FXML
    private void handleGoGymHome() {
        Main.setRoot("gymapp-home.fxml", goGymHomeButton);
    }

    @FXML
    private void handleEnterInfo() {
        Main.setRoot("enter-info.fxml", enterInfoButton);
    }

    @FXML
    private void handleReturnToWelcome() {
        Main.currentUserEmail = null;
        Main.currentUserUid = null;
        Main.setRoot("welcome-view.fxml", returnToWelcomeButton);
    }
}