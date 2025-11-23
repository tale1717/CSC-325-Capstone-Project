package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.util.logging.Logger;

public class AdminLoginController {

    private static final Logger log = Logger.getLogger(AdminLoginController.class.getName());

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void handleLogin() {
        String username = safeTrim(usernameField.getText());
        String password = safeTrim(passwordField.getText());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (Main.fstore == null) {
            log.info("Firestore is null in AdminLoginController.");
            showError("Server not ready. Please try again in a moment.");
            return;
        }

        try {
            DocumentReference doc =
                    Main.fstore.collection("Admins").document(username);

            ApiFuture<DocumentSnapshot> fut = doc.get();
            DocumentSnapshot snap = fut.get();

            if (!snap.exists()) {
                showError("Invalid username or password.");
                return;
            }

            String storedPassword = snap.getString("password");
            if (storedPassword == null || !storedPassword.equals(password)) {
                showError("Invalid username or password.");
                return;
            }

            // Login success
            log.info("Admin login success for username: " + username);

            // Tell FirestoreContext who is the current user
            // Using username as both uid and email identifier here.
            FirestoreContext.setCurrentUser(username, username);

            // Keep Main in sync if other code reads these
            Main.currentUserUid = FirestoreContext.currentUserUid;
            Main.currentUserEmail = FirestoreContext.currentUserEmail;

            // Go to admin view
            Main.setRoot("admin-view.fxml", loginButton);

        } catch (Exception e) {
            log.info("Admin login failed: " + e.getMessage());
            showError("Login failed due to an error. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        Main.setRoot("gym-layout-home.fxml", cancelButton);
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Admin Login");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
