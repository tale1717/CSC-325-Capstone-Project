package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleLogin() {
        String username = safeTrim(usernameField.getText());
        String password = safeTrim(passwordField.getText());

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Enter username and password.");
            return;
        }

        try {
            DocumentReference docRef =
                    Main.fstore.collection("Admins").document(username);

            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot snapshot = future.get();

            if (!snapshot.exists()) {
                setStatus("Admin not found.");
                return;
            }

            String storedPassword = snapshot.getString("password");
            if (storedPassword != null && storedPassword.equals(password)) {
                setStatus("");
                // Go to the existing Admin screen
                Main.setRoot("admin-view", loginButton);
            } else {
                setStatus("Incorrect password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Login failed. See console.");
        }
    }

    @FXML
    private void handleCancel() {
        Main.setRoot("gym-layout-home", cancelButton);
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void setStatus(String msg) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
        }
    }
}