package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AdminController {

    private static final Logger log = Logger.getLogger(AdminController.class.getName());

    @FXML
    private Button t1YesButton;
    @FXML
    private Button t1NoButton;
    @FXML
    private Button b1YesButton;
    @FXML
    private Button b1NoButton;
    @FXML
    private Button backButton;

    @FXML
    private Label t1StatusLabel;
    @FXML
    private Label b1StatusLabel;

    @FXML
    private void initialize() {

        boolean t1 = EquipmentState.t1Working;
        boolean b1 = EquipmentState.b1Working;

        try {
            if (Main.fstore != null) {
                DocumentReference doc =
                        Main.fstore.collection("EquipmentStatus").document("current");

                ApiFuture<DocumentSnapshot> fut = doc.get();
                DocumentSnapshot snap = fut.get();

                if (snap.exists()) {
                    Boolean t1Val = snap.getBoolean("T1");
                    Boolean b1Val = snap.getBoolean("B1");

                    if (t1Val != null) {
                        t1 = t1Val;
                    }
                    if (b1Val != null) {
                        b1 = b1Val;
                    }

                    EquipmentState.t1Working = t1;
                    EquipmentState.b1Working = b1;
                }
            } else {
                log.info("Firestore is null in AdminController; using in-memory state only.");
            }
        } catch (Exception e) {
            log.info("Failed to load EquipmentStatus from Firestore, using in-memory state. " + e.getMessage());
        }

        applyT1State(t1);
        applyB1State(b1);
    }

    @FXML
    private void handleT1Yes() {
        EquipmentState.t1Working = true;
        applyT1State(true);
        saveToFirestore();
    }

    @FXML
    private void handleT1No() {
        EquipmentState.t1Working = false;
        applyT1State(false);
        saveToFirestore();
    }

    @FXML
    private void handleB1Yes() {
        EquipmentState.b1Working = true;
        applyB1State(true);
        saveToFirestore();
    }

    @FXML
    private void handleB1No() {
        EquipmentState.b1Working = false;
        applyB1State(false);
        saveToFirestore();
    }

    @FXML
    private void handleBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyT1State(boolean isWorking) {
        t1StatusLabel.setText(isWorking ? "YES" : "NO");
        t1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        t1YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        t1NoButton.setStyle(!isWorking
                ? "-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
    }

    private void applyB1State(boolean isWorking) {
        b1StatusLabel.setText(isWorking ? "YES" : "NO");
        b1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        b1YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        b1NoButton.setStyle(!isWorking
                ? "-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
    }

    private void saveToFirestore() {
        if (Main.fstore == null) {
            log.info("Firestore is null in AdminController; not saving to backend.");
            return;
        }

        try {
            DocumentReference doc =
                    Main.fstore.collection("EquipmentStatus").document("current");

            Map<String, Object> data = new HashMap<>();
            data.put("T1", EquipmentState.t1Working);
            data.put("B1", EquipmentState.b1Working);

            ApiFuture<WriteResult> write = doc.set(data, SetOptions.merge());
            write.get();

            log.info("EquipmentStatus saved to Firestore: T1=" +
                    EquipmentState.t1Working + ", B1=" + EquipmentState.b1Working);
        } catch (Exception e) {
            log.info("Failed to save EquipmentStatus to Firestore: " + e.getMessage());
        }
    }
}