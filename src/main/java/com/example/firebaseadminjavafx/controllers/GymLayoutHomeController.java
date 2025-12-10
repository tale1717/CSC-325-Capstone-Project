package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.Main;
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
    private void initialize() {
        System.out.println("[GymLayoutHomeController] initialize()");
    }

    @FXML
    private void handleOpenMap() {
        System.out.println("[GymLayoutHomeController] Open Map clicked");
        Main.setRoot("gym-layout", openMapButton); // loads gym-layout.fxml
    }

    @FXML
    private void handleAdmin() {
        System.out.println("[GymLayoutHomeController] Admin clicked");

        Main.setRoot("admin-login" +
                "", adminButton);
    }

    @FXML
    private void handleBack() {
        System.out.println("[GymLayoutHomeController] Back clicked");

        Main.setRoot("gymapp-home", backButton);
    }
}