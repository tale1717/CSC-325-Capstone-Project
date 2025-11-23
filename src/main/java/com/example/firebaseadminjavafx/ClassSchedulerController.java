package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ClassSchedulerController {

    @FXML
    private Button openClassSchdbtn;

    @FXML
    private Button adminButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleOpenClass(){
        Main.setRoot("class-scheduler.fxml", openClassSchdbtn);
    }

    @FXML
    private void handleAdminButton() {
        Main.setRoot("admin-class-view.fxml", adminButton);
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }
}
