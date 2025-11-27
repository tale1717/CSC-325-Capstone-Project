package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WelcomeController {

    private static final Logger log = Logger.getLogger(WelcomeController.class.getName());

    @FXML private TextField emailID;
    @FXML private PasswordField passwordID;
    @FXML private Button registerButton;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;

    @FXML
    private void registerButtonClicked(ActionEvent event) {
        clearError();
        registerUser();
    }

    @FXML
    private void signInButtonClicked(ActionEvent event) {
        clearError();

        String email = safeTrim(emailID.getText());
        String password = safeTrim(passwordID.getText());

        if (email.isEmpty() || password.isEmpty()) {
            setError("Please enter both email and password.");
            return;
        }

        try {
            ApiFuture<QuerySnapshot> future = Main.fstore.collection("Users")
                    .whereEqualTo("email", email)
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            if (docs.isEmpty()) {
                setError("No account found with this email.");
                log.info("Sign in failed (no account): " + email);
                return;
            }

            QueryDocumentSnapshot doc = docs.get(0);
            String storedPassword = doc.getString("password");
            if (storedPassword != null && storedPassword.equals(password)) {
                Main.currentUserUid = doc.getId();   // uid stored as document id at register
                Main.currentUserEmail = email;

                log.info("User signed in successfully: " + Main.currentUserEmail);
                Main.setRoot("next-step.fxml", signInButton);
            } else {
                setError("Incorrect password.");
                log.info("Sign in failed (bad password): " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setError("Sign-in failed. See console.");
            log.info("Sign in failed (exception) for: " + email);
        }
    }

    // --- Helpers & Registration ---

    private boolean registerUser() {
        String email = safeTrim(emailID.getText());
        String password = safeTrim(passwordID.getText());

        if (email.isEmpty() || password.length() < 6) {
            setError("Email is invalid or password must be at least 6 characters.");
            return false;
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        try {
            UserRecord userRecord = Main.fauth.createUser(request);

            Map<String, Object> data = new HashMap<>();
            data.put("email", email);
            data.put("password", password);

            Main.fstore.collection("Users").document(userRecord.getUid()).set(data);


            setError("Registered! Please sign in.");
            log.info("User registered successfully: " + email);
            return true;

        } catch (FirebaseAuthException e) {
            setError("Registration error: " + e.getMessage());
            log.info("Registration failed (auth): " + email + " -> " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            setError("Registration failed. See console.");
            log.info("Registration failed (exception): " + email);
            return false;
        }
    }

    private static String safeTrim(String s) { return (s == null) ? "" : s.trim(); }
    private void setError(String msg) { if (errorLabel != null) errorLabel.setText(msg); }
    private void clearError() { setError(""); }
}