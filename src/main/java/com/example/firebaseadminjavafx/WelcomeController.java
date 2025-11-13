package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WelcomeController {

    @FXML private TextField emailID;
    @FXML private PasswordField passwordID;
    @FXML private Button registerButton;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;

    private static final Logger logger = Logger.getLogger(WelcomeController.class.getName());

    @FXML
    void registerButtonClicked(ActionEvent event) {
        clearError();
        registerUser(); // do NOT auto-navigate; require explicit sign-in (matches old behavior)
    }

    @FXML
    void signInButtonClicked(ActionEvent event) {
        clearError();

        String email = safeTrim(emailID.getText());
        String password = safeTrim(passwordID.getText());

        if (email.isEmpty() || password.isEmpty()) {
            setError("Please enter both email and password.");
            return;
        }

        try {
            ApiFuture<QuerySnapshot> future = FirestoreContext.fstore
                    .collection("Users")
                    .whereEqualTo("email", email)
                    .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (!documents.isEmpty()) {
                QueryDocumentSnapshot doc = documents.get(0);
                String storedPassword = doc.getString("password");
                if (password.equals(storedPassword)) {
                    // Record identity and navigate
                    FirestoreContext.currentUserUid = doc.getId();
                    FirestoreContext.currentUserEmail = email;
                    Main.currentUserUid = doc.getId();
                    Main.currentUserEmail = email;

                    logger.info("User signed in successfully: " + email);
                    Main.setRoot("NextStep", signInButton);
                } else {
                    setError("Incorrect password.");
                    logger.warning("Incorrect password for: " + email);
                }
            } else {
                setError("No account found with this email.");
                logger.warning("No account found for: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setError("Sign-in failed. See console.");
            logger.severe("Sign-in error for " + email + ": " + e.getMessage());
        }
    }

    public void registerUser() {
        String email = safeTrim(emailID.getText());
        String password = safeTrim(passwordID.getText());

        if (email.isEmpty() || password.length() < 6) {
            setError("Email is invalid or password must be at least 6 characters.");
            logger.warning("Invalid registration attempt for: " + email);
            return;
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        try {
            UserRecord userRecord = FirestoreContext.fauth.createUser(request);

            Map<String, Object> data = new HashMap<>();
            data.put("email", email);
            data.put("password", password); // DEMO ONLY

            FirestoreContext.fstore.collection("Users")
                    .document(userRecord.getUid())
                    .set(data);

            FirestoreContext.currentUserUid = userRecord.getUid();
            FirestoreContext.currentUserEmail = email;

            // Show success on the same screen; require user to sign in afterward
            setError("Registration successful! Please sign in.");
            logger.info("User registered successfully: " + email);

        } catch (FirebaseAuthException e) {
            setError("Registration error: " + e.getMessage());
            logger.severe("Registration error for " + email + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            setError("Registration failed. See console.");
            logger.severe("Registration failed for " + email + ": " + e.getMessage());
        }
    }

    private static String safeTrim(String s) { return (s == null) ? "" : s.trim(); }
    private void setError(String msg) { if (errorLabel != null) errorLabel.setText(msg); }
    private void clearError() { setError(""); }
}