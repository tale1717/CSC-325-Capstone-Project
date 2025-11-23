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
    private Button t2YesButton;
    @FXML
    private Button t2NoButton;
    @FXML
    private Button b1YesButton;
    @FXML
    private Button b1NoButton;
    @FXML
    private Button b2YesButton;
    @FXML
    private Button b2NoButton;
    @FXML
    private Button backButton;

    @FXML
    private Label t1StatusLabel;
    @FXML
    private Label t2StatusLabel;
    @FXML
    private Label b1StatusLabel;
    @FXML
    private Label b2StatusLabel;

    @FXML
    private void initialize() {
        boolean t1 = EquipmentState.t1Working;
        boolean t2 = EquipmentState.t2Working;
        boolean b1 = EquipmentState.b1Working;
        boolean b2 = EquipmentState.b2Working;

        try {
            if (Main.fstore != null) {
                DocumentReference doc =
                        Main.fstore.collection("EquipmentStatus").document("current");

                ApiFuture<DocumentSnapshot> fut = doc.get();
                DocumentSnapshot snap = fut.get();

                if (snap.exists()) {
                    Boolean t1Val = snap.getBoolean("T1");
                    Boolean t2Val = snap.getBoolean("T2");
                    Boolean b1Val = snap.getBoolean("B1");
                    Boolean b2Val = snap.getBoolean("B2");

                    if (t1Val != null) t1 = t1Val;
                    if (t2Val != null) t2 = t2Val;
                    if (b1Val != null) b1 = b1Val;
                    if (b2Val != null) b2 = b2Val;

                    EquipmentState.t1Working = t1;
                    EquipmentState.t2Working = t2;
                    EquipmentState.b1Working = b1;
                    EquipmentState.b2Working = b2;
                }
            } else {
                log.info("Firestore is null in AdminController; using in-memory state only.");
            }
        } catch (Exception e) {
            log.info("Failed to load EquipmentStatus from Firestore, using in-memory state. " + e.getMessage());
        }

        applyT1State(t1);
        applyT2State(t2);
        applyB1State(b1);
        applyB2State(b2);
    }

    //Add more machines
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
    private void handleT2Yes() {
        EquipmentState.t2Working = true;
        applyT2State(true);
        saveToFirestore();
    }

    @FXML
    private void handleT2No() {
        EquipmentState.t2Working = false;
        applyT2State(false);
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
    private void handleB2Yes() {
        EquipmentState.b2Working = true;
        applyB2State(true);
        saveToFirestore();
    }

    @FXML
    private void handleB2No() {
        EquipmentState.b2Working = false;
        applyB2State(false);
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

    private void applyT2State(boolean isWorking) {
        t2StatusLabel.setText(isWorking ? "YES" : "NO");
        t2StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        t2YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        t2NoButton.setStyle(!isWorking
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

    private void applyB2State(boolean isWorking) {
        b2StatusLabel.setText(isWorking ? "YES" : "NO");
        b2StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");

        b2YesButton.setStyle(isWorking
                ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                : "");
        b2NoButton.setStyle(!isWorking
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
            data.put("T2", EquipmentState.t2Working);
            data.put("B1", EquipmentState.b1Working);
            data.put("B2", EquipmentState.b2Working);

            ApiFuture<WriteResult> write = doc.set(data, SetOptions.merge());
            write.get();

            log.info("EquipmentStatus saved to Firestore: " +
                    "T1=" + EquipmentState.t1Working +
                    ", T2=" + EquipmentState.t2Working +
                    ", B1=" + EquipmentState.b1Working +
                    ", B2=" + EquipmentState.b2Working);
        } catch (Exception e) {
            log.info("Failed to save EquipmentStatus to Firestore: " + e.getMessage());
        }
    }
}