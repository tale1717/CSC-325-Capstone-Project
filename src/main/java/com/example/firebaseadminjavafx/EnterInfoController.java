package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EnterInfoController {

    private static final Logger log = Logger.getLogger(EnterInfoController.class.getName());

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {

        try {
            if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
                setStatus("Not signed in.");
                return;
            }
            DocumentReference userDoc = Main.fstore.collection("Users").document(Main.currentUserUid);
            ApiFuture<DocumentSnapshot> fut = userDoc.get();
            DocumentSnapshot snap = fut.get();
            if (snap.exists()) {
                String existingName  = snap.getString("name");
                Long   existingAge   = snap.getLong("age");
                String existingPhone = snap.getString("phone");

                if (existingName != null)  nameField.setText(existingName);
                if (existingAge != null)   ageField.setText(String.valueOf(existingAge));
                if (existingPhone != null) phoneField.setText(existingPhone);
            }
        } catch (Exception e) {
            log.info("Prefill failed: " + e.getMessage());
            setStatus("Could not load saved info.");
        }
    }

    @FXML
    private void handleSave() {
        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            setStatus("Not signed in.");
            log.info("Save aborted: no signed-in user.");
            return;
        }

        String name  = safeTrim(nameField.getText());
        String ageS  = safeTrim(ageField.getText());
        String phone = safeTrim(phoneField.getText());

        if (name.isEmpty() || ageS.isEmpty()) {
            setStatus("Enter name and age.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageS);
            if (age < 0 || age > 120) {
                setStatus("Enter a valid age.");
                return;
            }
        } catch (NumberFormatException nfe) {
            setStatus("Age must be a number.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("email", Main.currentUserEmail);   // tie to email
        data.put("name", name);
        data.put("age", age);
        data.put("phone", phone.isEmpty() ? null : phone);
        data.put("updatedAt", Instant.now().toString());

        try {
            DocumentReference userDoc = Main.fstore.collection("Users").document(Main.currentUserUid);

            ApiFuture<WriteResult> write = userDoc.set(data, SetOptions.merge());
            write.get();

            setStatus("Saved.");
            log.info("Info saved for user: " + Main.currentUserEmail);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Save failed. See console.");
            log.info("Save failed for user: " + Main.currentUserEmail);
        }
    }

    @FXML
    private void handleBack() {
        Main.setRoot("next-step.fxml", backButton);
    }

    // --- helpers ---
    private static String safeTrim(String s) { return (s == null) ? "" : s.trim(); }
    private void setStatus(String msg) { if (statusLabel != null) statusLabel.setText(msg); }
}