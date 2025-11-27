package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymAppHomeController {

    @FXML
    private Label emailValue;

    @FXML
    private Button gymLayoutButton;
    private Button profileButton;

    @FXML
    private Button viewClassesButton;

    @FXML
    private Button workoutButton;

    @FXML
    private Button progressTrackerButton;
    private Button gymMapButton;

    @FXML
    private Button myProfileButton;

    @FXML
    private Button returnToWelcomeButton;

    @FXML
    private void initialize() {
        emailValue.setText(Main.currentUserEmail != null ? Main.currentUserEmail : "(unknown)");
    }

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
    private void handleProgressTracker() {
        Main.setRoot("progress-tracker", progressTrackerButton);
    }

    @FXML
    private void handleMyProfile() { Main.setRoot("enter-info.fxml", myProfileButton); }

    @FXML
    private void handleReturnToWelcome() {
        Main.currentUserEmail = null;
        Main.currentUserUid = null;
        Main.setRoot("welcome-view.fxml", returnToWelcomeButton);
    }


}
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
