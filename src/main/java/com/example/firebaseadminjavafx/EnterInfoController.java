package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class EnterInfoController {

    @FXML private Label headerLabel;
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField phoneField;
    @FXML private Label statusLabel;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        String email = (Main.currentUserEmail != null) ? Main.currentUserEmail : "(unknown)";
        headerLabel.setText("Enter Info — " + email);
        setStatus("");
    }

    @FXML
    private void handleSave() {
        if (Main.currentUserUid == null) {
            setStatus("Please sign in again.");
            return;
        }

        String name = safeTrim(nameField.getText());
        String ageText = safeTrim(ageField.getText());
        String phone = safeTrim(phoneField.getText());

        if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()) {
            setStatus("Please enter name, age, and phone.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 0) {
                setStatus("Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            setStatus("Age must be a valid number.");
            return;
        }

        try {
            DocumentReference docRef = FirestoreContext.fstore
                    .collection("UserInfo")
                    .document(Main.currentUserUid);

            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("age", age);
            data.put("phone", phone);
            data.put("email", Main.currentUserEmail);

            ApiFuture<WriteResult> future = docRef.set(data);
            future.get();

            setStatus("Info saved.");
            System.out.println("INFO: User info saved: example - " + Main.currentUserEmail);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Failed to save info. See console.");
        }
    }

    @FXML
    private void handleBack() {
        Main.setRoot("NextStep", backButton);
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private void setStatus(String msg) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
        }
    }
}
