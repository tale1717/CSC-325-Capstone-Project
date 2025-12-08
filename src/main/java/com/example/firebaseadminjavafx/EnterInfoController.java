package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class EnterInfoController {

    private static final Logger log = Logger.getLogger(EnterInfoController.class.getName());

    // --- UI elements from enter-info.fxml ---
    @FXML private Label nameField;
    @FXML private Label emailField;

    // Reference to the Home button in the navigation bar
    @FXML private Button homeButton;  // We need to add fx:id to the button in FXML

    @FXML
    private void initialize() {
        log.info("EnterInfoController initialized");

        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            nameField.setText("Not signed in");
            emailField.setText("");
            return;
        }

        try {
            DocumentReference userDoc =
                    Main.fstore.collection("Users").document(Main.currentUserUid);

            ApiFuture<DocumentSnapshot> future = userDoc.get();
            DocumentSnapshot snapshot = future.get();

            String displayName = null;
            String email = Main.currentUserEmail;

            if (snapshot.exists()) {
                displayName = snapshot.getString("name");
            }

            // If Firestore has a name, show it; otherwise placeholder:
            nameField.setText(displayName != null ? displayName : "Your name here");

            // Show email always:
            emailField.setText(email != null ? email : "Your email here");

        } catch (Exception e) {
            log.warning("Error loading profile: " + e.getMessage());
            nameField.setText("Error loading profile");
            emailField.setText("");
        }
    }

    // Method to navigate to Home page when clicking "Home" in navigation bar
    @FXML
    private void handleHome() {
        try {
            log.info("handleHome() called - navigating to gymapp-home.fxml");

            // We need to pass a Control object to Main.setRoot()
            // The homeButton is a Control that we can pass
            // But first, let's use the nameField which is also a Control
            Main.setRoot("gymapp-home.fxml", nameField);

        } catch (Exception e) {
            log.severe("Error navigating to home: " + e.getMessage());
            e.printStackTrace();
        }
    }
}