package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymAppHomeController {

    @FXML private Label welcomeLabel;
    @FXML private Button goAdminButton;
    @FXML private Button signOutButton;

    @FXML
    private void initialize() {
        String email = (Main.currentUserEmail != null) ? Main.currentUserEmail : "(unknown)";
        welcomeLabel.setText("Welcome, " + email);
    }

    @FXML
    private void handleGoAdmin() {
        Main.setRoot("Admin", goAdminButton);
    }

    @FXML
    private void handleSignOut() {
        System.out.println("INFO: User signed out successfully: example - " + Main.currentUserEmail);
        Main.currentUserEmail = null;
        Main.currentUserUid = null;
        Main.setRoot("Welcome", signOutButton);
    }
}