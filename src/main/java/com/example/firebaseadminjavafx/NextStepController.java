package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.logging.Logger;

public class NextStepController {

    @FXML private Label emailValue;
    @FXML private Label uidValue;
    @FXML private Button signOutButton;

    private static final Logger logger = Logger.getLogger(NextStepController.class.getName());

    @FXML
    private void initialize() {
        emailValue.setText(Main.currentUserEmail != null ? Main.currentUserEmail : "(unknown)");
        uidValue.setText(Main.currentUserUid != null ? Main.currentUserUid : "(unknown)");
    }

    @FXML
    private void handleSignOut() {
        logger.info("User signed out: " + Main.currentUserEmail);

        Main.currentUserEmail = null;
        Main.currentUserUid = null;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/firebaseadminjavafx/welcome-view.fxml"));
            Parent root = loader.load();
            signOutButton.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error returning to welcome screen: " + e.getMessage());
        }
    }
}