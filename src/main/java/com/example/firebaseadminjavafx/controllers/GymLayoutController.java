package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.EquipmentState;
import com.example.firebaseadminjavafx.logic.Main;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.logging.Logger;

public class GymLayoutController {

    private static final Logger log = Logger.getLogger(GymLayoutController.class.getName());

    @FXML private Label bench1StatusLabel;                // B1
    @FXML private Label bench2StatusLabel;                // B2
    @FXML private Label elliptical1StatusLabel;           // E1
    @FXML private Label elliptical2StatusLabel;           // E2
    @FXML private Label elliptical3StatusLabel;           // E3
    @FXML private Label treadmill1StatusLabel;            // T1
    @FXML private Label treadmill2StatusLabel;            // T2
    @FXML private Label chestPress1StatusLabel;           // PC1
    @FXML private Label preacherCurl1StatusLabel;         // P1
    @FXML private Label olympicBench1StatusLabel;         // OB1
    @FXML private Label adductor1StatusLabel;             // AD1
    @FXML private Label abductor1StatusLabel;             // AB1
    @FXML private Label olympicIncline1StatusLabel;       // OI1
    @FXML private Label weightAssistDipChinStatusLabel;   // W1
    @FXML private Label cable1StatusLabel;                // C1

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        // Apply immediately from memory so UI always shows something
        applyAllFromMemory();

        // Then refresh from Firestore (if available)
        if (Main.fstore == null) {
            log.info("Firestore is null in GymLayoutController; using in-memory state only.");
            return;
        }

        Thread t = new Thread(() -> {
            try {
                DocumentReference doc = Main.fstore.collection("EquipmentStatus").document("current");
                ApiFuture<DocumentSnapshot> fut = doc.get();
                DocumentSnapshot snap = fut.get();

                if (snap.exists()) {
                    EquipmentState.t1Working = getBoolOr(snap, "T1", EquipmentState.t1Working);
                    EquipmentState.t2Working = getBoolOr(snap, "T2", EquipmentState.t2Working);
                    EquipmentState.b1Working = getBoolOr(snap, "B1", EquipmentState.b1Working);
                    EquipmentState.b2Working = getBoolOr(snap, "B2", EquipmentState.b2Working);

                    EquipmentState.e1Working = getBoolOr(snap, "E1", EquipmentState.e1Working);
                    EquipmentState.e2Working = getBoolOr(snap, "E2", EquipmentState.e2Working);
                    EquipmentState.e3Working = getBoolOr(snap, "E3", EquipmentState.e3Working);

                    EquipmentState.pc1Working = getBoolOr(snap, "PC1", EquipmentState.pc1Working);
                    EquipmentState.p1Working  = getBoolOr(snap, "P1",  EquipmentState.p1Working);
                    EquipmentState.ob1Working = getBoolOr(snap, "OB1", EquipmentState.ob1Working);
                    EquipmentState.ad1Working = getBoolOr(snap, "AD1", EquipmentState.ad1Working);
                    EquipmentState.ab1Working = getBoolOr(snap, "AB1", EquipmentState.ab1Working);
                    EquipmentState.oi1Working = getBoolOr(snap, "OI1", EquipmentState.oi1Working);
                    EquipmentState.w1Working  = getBoolOr(snap, "W1",  EquipmentState.w1Working);
                    EquipmentState.c1Working  = getBoolOr(snap, "C1",  EquipmentState.c1Working);

                    Platform.runLater(this::applyAllFromMemory);
                }
            } catch (Exception e) {
                log.info("Failed to load EquipmentStatus from Firestore, using in-memory state. " + e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private static boolean getBoolOr(DocumentSnapshot snap, String key, boolean fallback) {
        Boolean v = snap.getBoolean(key);
        return (v == null) ? fallback : v;
    }

    private void applyAllFromMemory() {
        applyStatus(treadmill1StatusLabel, EquipmentState.t1Working);
        applyStatus(treadmill2StatusLabel, EquipmentState.t2Working);

        applyStatus(bench1StatusLabel, EquipmentState.b1Working);
        applyStatus(bench2StatusLabel, EquipmentState.b2Working);

        applyStatus(elliptical1StatusLabel, EquipmentState.e1Working);
        applyStatus(elliptical2StatusLabel, EquipmentState.e2Working);
        applyStatus(elliptical3StatusLabel, EquipmentState.e3Working);

        applyStatus(chestPress1StatusLabel, EquipmentState.pc1Working);
        applyStatus(preacherCurl1StatusLabel, EquipmentState.p1Working);
        applyStatus(olympicBench1StatusLabel, EquipmentState.ob1Working);
        applyStatus(adductor1StatusLabel, EquipmentState.ad1Working);
        applyStatus(abductor1StatusLabel, EquipmentState.ab1Working);
        applyStatus(olympicIncline1StatusLabel, EquipmentState.oi1Working);
        applyStatus(weightAssistDipChinStatusLabel, EquipmentState.w1Working);
        applyStatus(cable1StatusLabel, EquipmentState.c1Working);
    }

    private void applyStatus(Label label, boolean isWorking) {
        if (label == null) return;
        label.setText(isWorking ? "YES" : "NO");
        label.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }
}