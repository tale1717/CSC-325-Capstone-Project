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
        System.out.println("Profile button clicked");
        // Navigate to profile screen
        // Main.setRoot("profile-screen", profileButton);
    }

    @FXML
    private void handleViewClasses() {
        System.out.println("View Classes button clicked");
        // Navigate to classes screen
        Main.setRoot("class-scheduler-home", viewClassesButton);
    }

    @FXML
    private void handleWorkout() {
        System.out.println("Workout button clicked");
        // Navigate to workout planning screen
        Main.setRoot("workout-planning", workoutButton);
    }

    @FXML
    private void handleGymMap() {
        System.out.println("Gym Map button clicked");
        // Navigate to gym layout screen
        Main.setRoot("gym-layout-home", gymMapButton);
    }

    @FXML
    private void handleProgressTracker() {
        System.out.println("Workout Progress Tracker button clicked");
        // Navigate to progress tracker screen
        Main.setRoot("progress-tracker", progressTrackerButton);
    }

    @FXML
    private void handleBack() {
        System.out.println("Back button clicked");
        // Navigate back to login screen
        Main.setRoot("welcome", backButton);
    }
}