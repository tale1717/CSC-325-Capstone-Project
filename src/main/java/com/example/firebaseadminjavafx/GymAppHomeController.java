package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GymAppHomeController {

    @FXML
    private Button gymLayoutButton;

    @FXML
    private Button workoutPlanningButton;

    @FXML
    private Button classSchedulerButton;

    @FXML
    private Button backButton;

    @FXML
    private Button progressTrackerButton;

    @FXML
    private void handleGymLayout() {
        Main.setRoot("gym-layout-home", gymLayoutButton);
    }

    @FXML
    private void handleWorkoutPlanning() {
        Main.setRoot("workout-planning", workoutPlanningButton);
    }

    @FXML
    private void handleClassScheduling() {
        Main.setRoot("class-scheduler-home", classSchedulerButton);
    }

    @FXML
    private void handleBack() {
        Main.setRoot("next-step", backButton);
    }

    @FXML
    private void handleProgressTracker() {
        Main.setRoot("progress-tracker", progressTrackerButton);
    }
}
