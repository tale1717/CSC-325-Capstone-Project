package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminController {

    @FXML
    private Button t1YesButton;
    @FXML
    private Button t1NoButton;
    @FXML
    private Button b1YesButton;
    @FXML
    private Button b1NoButton;
    @FXML
    private Button backButton;

    @FXML
    private Label t1StatusLabel;
    @FXML
    private Label b1StatusLabel;

    @FXML
    private void initialize() {
        applyT1State(EquipmentState.t1Working);
        applyB1State(EquipmentState.b1Working);
    }

    @FXML
    private void handleT1Yes() {
        EquipmentState.t1Working = true;
        applyT1State(true);
    }

    @FXML
    private void handleT1No() {
        EquipmentState.t1Working = false;
        applyT1State(false);
    }

    @FXML
    private void handleB1Yes() {
        EquipmentState.b1Working = true;
        applyB1State(true);
    }

    @FXML
    private void handleB1No() {
        EquipmentState.b1Working = false;
        applyB1State(false);
    }

    @FXML
    private void handleBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyT1State(boolean isWorking) {
        t1StatusLabel.setText(isWorking ? "YES" : "NO");
        t1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        t1YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        t1NoButton.setStyle(!isWorking
                ? "-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
    }

    private void applyB1State(boolean isWorking) {
        b1StatusLabel.setText(isWorking ? "YES" : "NO");
        b1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        b1YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        b1NoButton.setStyle(!isWorking
                ? "-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
    }
}