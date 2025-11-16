package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymLayoutController {

    @FXML
    private Label treadmillStatusLabel; // T1 status
    @FXML
    private Label benchStatusLabel;     // B1 status
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        applyTreadmillStatus(EquipmentState.t1Working);
        applyBenchStatus(EquipmentState.b1Working);
    }

    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyTreadmillStatus(boolean isWorking) {
        treadmillStatusLabel.setText(isWorking ? "YES" : "NO");
        treadmillStatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    private void applyBenchStatus(boolean isWorking) {
        benchStatusLabel.setText(isWorking ? "YES" : "NO");
        benchStatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }
}