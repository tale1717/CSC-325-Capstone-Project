package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GymAppHomeController {

    @FXML
    private Button gymLayoutButton;

    @FXML
    private Button workoutPlanningButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleGymLayout() {
        Main.setRoot("gym-layout-home", gymLayoutButton);
    }

    @FXML
    private void handleWorkoutPlanning() {
        Main.setRoot("workout-planning.fxml", workoutPlanningButton);
    }

    @FXML
    private void handleBack() {
        Main.setRoot("next-step.fxml", backButton);
    }
}