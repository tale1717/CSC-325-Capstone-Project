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
    private Label bench1StatusLabel;     // B1

    @FXML
    private Label bench2StatusLabel;     // B2|

    @FXML
    private Label elliptical1StatusLabel;     // E1

    @FXML
    private Label elliptical2StatusLabel;     // E2

    @FXML
    private Label elliptical3StatusLabel;     // E3

    @FXML
    private Label treadmill1StatusLabel; // T1

    @FXML
    private Label treadmill2StatusLabel; // T2

    @FXML
    private Label chestPress1StatusLabel;     // PC1

    @FXML
    private Label cable1StatusLabel;     // C1

    @FXML
    private Label olympicBench1StatusLabel;     // OB1

    @FXML
    private Label olympicIncline1StatusLabel;     // OI1

    @FXML
    private Label adductor1StatusLabel;     // AD1

    @FXML
    private Label abductor1StatusLabel;     // AB1

    @FXML
    private Label preacherCurl1StatusLabel;     // P1

    @FXML
    private Label weight1StatusLabel;     // W1

    @FXML
    private Button backButton;

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
                log.info("Firestore is null in GymLayoutController; using in-memory state only.");
            }
        } catch (Exception e) {
            log.info("Failed to load EquipmentStatus from Firestore, using in-memory state. " + e.getMessage());
        }

        applyTreadmill1Status(t1);
        applyTreadmill2Status(t2);
        applyBench1Status(b1);
        applyBench2Status(b2);
    }

    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyTreadmill1Status(boolean isWorking) {
        treadmill1StatusLabel.setText(isWorking ? "YES" : "NO");
        treadmill1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    private void applyTreadmill2Status(boolean isWorking) {
        treadmill2StatusLabel.setText(isWorking ? "YES" : "NO");
        treadmill2StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    private void applyBench1Status(boolean isWorking) {
        bench1StatusLabel.setText(isWorking ? "YES" : "NO");
        bench1StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    private void applyBench2Status(boolean isWorking) {
        bench2StatusLabel.setText(isWorking ? "YES" : "NO");
        bench2StatusLabel.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }
}