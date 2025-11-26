package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymAppHomeController {

    @FXML
    private Label emailValue;

    @FXML
    private Button gymLayoutButton;

    @FXML
    private Button workoutPlanningButton;

    @FXML
    private Button classSchedulerButton;

    @FXML
    private Button progressTrackerButton;

    @FXML
    private Button myProfileButton;

    @FXML
    private Button returnToWelcomeButton;

    @FXML
    private void initialize() {
        emailValue.setText(Main.currentUserEmail != null ? Main.currentUserEmail : "(unknown)");
    }

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
