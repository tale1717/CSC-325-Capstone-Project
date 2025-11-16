package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.logging.Logger;

public class GymLayoutController {

    private static final Logger log = Logger.getLogger(GymLayoutController.class.getName());

    @FXML
    private Label treadmillStatusLabel; // T1 status

    @FXML
    private Label benchStatusLabel;     // B1 status

    @FXML
    private Button backButton;

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
                log.info("Firestore is null in GymLayoutController; using in-memory state only.");
            }
        } catch (Exception e) {
            log.info("Failed to load EquipmentStatus from Firestore, using in-memory state. " + e.getMessage());
        }

        applyTreadmillStatus(t1);
        applyBenchStatus(b1);
    }

    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyTreadmillStatus(boolean isWorking) {
        treadmillStatusLabel.setText(isWorking ? "YES" : "NO");
        treadmillStatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    private void applyBenchStatus(boolean isWorking) {
        benchStatusLabel.setText(isWorking ? "YES" : "NO");
        benchStatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }
}