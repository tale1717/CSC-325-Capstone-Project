package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GymLayoutHomeController {

    @FXML
    private Button openMapButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleOpenMap() {
        Main.setRoot("gym-layout.fxml", openMapButton);
    }

    @FXML
    private void handleOpenAdmin() {
        Main.setRoot("admin-view.fxml", adminButton);
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }
}