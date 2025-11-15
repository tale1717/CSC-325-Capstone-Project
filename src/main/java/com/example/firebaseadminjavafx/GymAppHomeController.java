package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymAppHomeController {

    @FXML private Label headerLabel;
    @FXML private Button gymLayoutButton;
    @FXML private Button workoutPlanningButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        String email = (Main.currentUserEmail != null) ? Main.currentUserEmail : "(unknown)";
        headerLabel.setText("Gym Home — " + email);
    }

    @FXML
    private void handleGymLayout() {
        System.out.println("Gym Layout clicked for: " + Main.currentUserEmail);

    }

    @FXML
    private void handleWorkoutPlanning() {
        System.out.println("Workout Planning clicked for: " + Main.currentUserEmail);

    }

    @FXML
    private void handleBack() {
        Main.setRoot("NextStep", backButton);
    }
}