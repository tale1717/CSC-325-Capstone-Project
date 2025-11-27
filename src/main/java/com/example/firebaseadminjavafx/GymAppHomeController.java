package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GymAppHomeController {

    @FXML
    private Button profileButton;

    @FXML
    private Button viewClassesButton;

    @FXML
    private Button workoutButton;

    @FXML
    private Button gymMapButton;

    @FXML
    private Button progressTrackerButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleProfile() {
        Main.setRoot("enter-info.fxml", profileButton);
    }

    @FXML
    private void handleViewClasses() {
        Main.setRoot("class-scheduler-home.fxml", viewClassesButton);
    }

    @FXML
    private void handleWorkout() {
        Main.setRoot("workout-planning.fxml", workoutButton);
    }

    @FXML
    private void handleGymMap() {
        Main.setRoot("gym-layout-home.fxml", gymMapButton);
    }

    @FXML
    private void handleProgressTracker() {
        Main.setRoot("progress-tracker.fxml", progressTrackerButton);
    }

    @FXML
    private void handleBack() {
        Main.currentUserUid = null;
        Main.currentUserEmail = null;
        Main.setRoot("welcome-view.fxml", backButton);
    }
}