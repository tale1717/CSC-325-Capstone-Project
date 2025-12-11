package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.Main;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Bucket;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EnterInfoController {

    private static final Logger log = Logger.getLogger(EnterInfoController.class.getName());

    @FXML private Label nameField;
    @FXML private Label emailField;

    @FXML private Label totalTimeLabel;
    @FXML private Label totalCaloriesLabel;
    @FXML private Label totalSessionsLabel;

    @FXML private Button homeButton;
    @FXML private ImageView profileImageView;

    @FXML
    private void initialize() {
        log.info("EnterInfoController initialized");

        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            nameField.setText("Not signed in");
            emailField.setText("");
            setStatLabels(0L, 0L, 0L);
            loadProfilePhoto(null);
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

                Long totalMinutes = snapshot.getLong("totalMinutesAtGym");
                Long totalCalories = snapshot.getLong("totalCaloriesBurned");
                Long totalSessions = snapshot.getLong("totalMachineSessions");

                setStatLabels(
                        totalMinutes != null ? totalMinutes : 0L,
                        totalCalories != null ? totalCalories : 0L,
                        totalSessions != null ? totalSessions : 0L
                );
            } else {
                setStatLabels(0L, 0L, 0L);
            }

            nameField.setText(displayName != null ? displayName : "Your name here");
            emailField.setText(email != null ? email : "Your email here");

            loadProfilePhoto(snapshot);

        } catch (Exception e) {
            log.warning("Error loading profile: " + e.getMessage());
            nameField.setText("Error loading profile");
            emailField.setText("");
            setStatLabels(0L, 0L, 0L);
            loadProfilePhoto(null);
        }
    }

    private void setStatLabels(Long totalMinutes,
                               Long totalCalories,
                               Long totalSessions) {
        if (totalTimeLabel != null) {
            totalTimeLabel.setText(totalMinutes + " min");
        }
        if (totalCaloriesLabel != null) {
            totalCaloriesLabel.setText(totalCalories + " kcal");
        }
        if (totalSessionsLabel != null) {
            totalSessionsLabel.setText(totalSessions + " sessions");
        }
    }

    private void loadProfilePhoto(DocumentSnapshot snapshot) {
        if (profileImageView == null) {
            return;
        }
        try {
            if (snapshot != null && snapshot.exists()) {
                String url = snapshot.getString("profilePhotoUrl");
                if (url != null && !url.isEmpty()) {
                    profileImageView.setImage(new Image(url, true));
                } else {
                    profileImageView.setImage(null);
                }
            } else {
                profileImageView.setImage(null);
            }
        } catch (Exception e) {
            log.warning("Error loading profile photo: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangePhoto() {
        try {
            if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
                showAlert("Not signed in", "You must be signed in to change your profile picture.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Profile Picture");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            Window window = null;
            if (nameField != null && nameField.getScene() != null) {
                window = nameField.getScene().getWindow();
            } else if (homeButton != null && homeButton.getScene() != null) {
                window = homeButton.getScene().getWindow();
            }

            File file = fileChooser.showOpenDialog(window);
            if (file == null) {
                return;
            }

            String uid = Main.currentUserUid;
            String objectName = "profilePictures/" + uid + "-" + System.currentTimeMillis();

            Bucket bucket = StorageClient.getInstance().bucket();
            try (InputStream in = new FileInputStream(file)) {
                bucket.create(objectName, in, "image/jpeg");
            }

            String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
            String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + objectName;

            DocumentReference userDoc =
                    Main.fstore.collection("Users").document(uid);

            Map<String, Object> data = new HashMap<>();
            data.put("profilePhotoUrl", publicUrl);
            userDoc.set(data, SetOptions.merge()).get();

            if (profileImageView != null) {
                profileImageView.setImage(new Image(publicUrl, true));
            }

        } catch (Exception e) {
            log.warning("Error uploading profile photo: " + e.getMessage());
            e.printStackTrace();
            showAlert("Upload failed", "Could not upload profile picture.");
        }
    }

    @FXML
    private void handleHome() {
        try {
            log.info("handleHome() called - navigating to gymapp-home.fxml");
            Main.setRoot("gymapp-home.fxml", nameField);
        } catch (Exception e) {
            log.severe("Error navigating to home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
