package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HelloController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ListView<String> userList;

    private final Firestore db = FirestoreClient.getFirestore();

    @FXML
    private void handleAddUser() {
        String name = nameField.getText();
        String email = emailField.getText();

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            show("Error", "Please enter both name and email.");
            return;
        }

        Map<String, Object> user = Map.of("name", name, "email", email);

        try {
            ApiFuture<DocumentReference> future = db.collection("users").add(user);
            future.get(); // block briefly for demo
            show("Success", "User added.");
            nameField.clear();
            emailField.clear();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            show("Error", "Operation interrupted.");
        } catch (ExecutionException ee) {
            show("Error", "Failed to add user: " + ee.getMessage());
        } catch (Exception e) {
            show("Error", "Unexpected: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadUsers() {
        userList.getItems().clear();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            for (QueryDocumentSnapshot d : docs) {
                String name = d.getString("name");
                String email = d.getString("email");
                userList.getItems().add((name != null ? name : "(no name)")
                        + " (" + (email != null ? email : "no-email") + ")");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            show("Error", "Operation interrupted.");
        } catch (ExecutionException ee) {
            show("Error", "Failed to load users: " + ee.getMessage());
        } catch (Exception e) {
            show("Error", "Unexpected: " + e.getMessage());
        }
    }

    private void show(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}