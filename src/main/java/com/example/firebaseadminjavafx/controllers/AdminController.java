package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.models.EquipmentState;
import com.example.firebaseadminjavafx.logic.Main;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AdminController {

    private static final Logger log = Logger.getLogger(AdminController.class.getName());
    public ScrollPane gymAdminRoot;

    // Treadmills
    @FXML
    private Button t1YesButton;
    @FXML private Button t1NoButton;
    @FXML private Label t1StatusLabel;

    @FXML private Button t2YesButton;
    @FXML private Button t2NoButton;
    @FXML private Label t2StatusLabel;

    // Benches
    @FXML private Button b1YesButton;
    @FXML private Button b1NoButton;
    @FXML private Label b1StatusLabel;

    @FXML private Button b2YesButton;
    @FXML private Button b2NoButton;
    @FXML private Label b2StatusLabel;

    // Ellipticals
    @FXML private Button e1YesButton;
    @FXML private Button e1NoButton;
    @FXML private Label e1StatusLabel;

    @FXML private Button e2YesButton;
    @FXML private Button e2NoButton;
    @FXML private Label e2StatusLabel;

    @FXML private Button e3YesButton;
    @FXML private Button e3NoButton;
    @FXML private Label e3StatusLabel;

    // PC1 and P1 / S1 row from FXML
    @FXML private Button pc1YesButton;
    @FXML private Button pc1NoButton;
    @FXML private Label pc1StatusLabel;

    @FXML private Button s1YesButton;
    @FXML private Button s1NoButton;
    @FXML private Label s1StatusLabel;

    // Olympic bench
    @FXML private Button ob1YesButton;
    @FXML private Button ob1NoButton;
    @FXML private Label ob1StatusLabel;

    // Olympic incline
    @FXML private Button oi1YesButton;
    @FXML private Button oi1NoButton;
    @FXML private Label oi1StatusLabel;

    // Cable
    @FXML private Button c1YesButton;
    @FXML private Button c1NoButton;
    @FXML private Label c1StatusLabel;

    // Adductor
    @FXML private Button ad1YesButton;
    @FXML private Button ad1NoButton;
    @FXML private Label ad1StatusLabel;

    // Abductor
    @FXML private Button ab1YesButton;
    @FXML private Button ab1NoButton;
    @FXML private Label ab1StatusLabel;

    // Weight assist dip/chin
    @FXML private Button w1YesButton;
    @FXML private Button w1NoButton;
    @FXML private Label w1StatusLabel;

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        applyAllFromMemory();

        if (Main.fstore == null) {
            log.info("Firestore is null in AdminController; using in-memory state only.");
            return;
        }

        Thread t = new Thread(() -> {
            try {
                DocumentReference doc = Main.fstore.collection("EquipmentStatus").document("current");
                DocumentSnapshot snap = doc.get().get();

                if (snap.exists()) {
                    // Treadmills / benches
                    EquipmentState.t1Working = getBoolOr(snap, "T1", EquipmentState.t1Working);
                    EquipmentState.t2Working = getBoolOr(snap, "T2", EquipmentState.t2Working);
                    EquipmentState.b1Working = getBoolOr(snap, "B1", EquipmentState.b1Working);
                    EquipmentState.b2Working = getBoolOr(snap, "B2", EquipmentState.b2Working);

                    // Ellipticals
                    EquipmentState.e1Working = getBoolOr(snap, "E1", EquipmentState.e1Working);
                    EquipmentState.e2Working = getBoolOr(snap, "E2", EquipmentState.e2Working);
                    EquipmentState.e3Working = getBoolOr(snap, "E3", EquipmentState.e3Working);

                    // Other strength machines
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

    private static boolean getBoolOr(DocumentSnapshot snap, String key, boolean fallback) {
        Boolean v = snap.getBoolean(key);
        return (v == null) ? fallback : v;
    }

    // T1
    @FXML private void handleT1Yes() { EquipmentState.t1Working = true;  applyT1State(true);  saveToFirestore(); }
    @FXML private void handleT1No()  { EquipmentState.t1Working = false; applyT1State(false); saveToFirestore(); }

    // T2
    @FXML private void handleT2Yes() { EquipmentState.t2Working = true;  applyT2State(true);  saveToFirestore(); }
    @FXML private void handleT2No()  { EquipmentState.t2Working = false; applyT2State(false); saveToFirestore(); }

    // B1
    @FXML private void handleB1Yes() { EquipmentState.b1Working = true;  applyB1State(true);  saveToFirestore(); }
    @FXML private void handleB1No()  { EquipmentState.b1Working = false; applyB1State(false); saveToFirestore(); }

    // B2
    @FXML private void handleB2Yes() { EquipmentState.b2Working = true;  applyB2State(true);  saveToFirestore(); }
    @FXML private void handleB2No()  { EquipmentState.b2Working = false; applyB2State(false); saveToFirestore(); }

    // E1
    @FXML private void handleE1Yes() { EquipmentState.e1Working = true;  applyE1State(true);  saveToFirestore(); }
    @FXML private void handleE1No()  { EquipmentState.e1Working = false; applyE1State(false); saveToFirestore(); }

    // E2
    @FXML private void handleE2Yes() { EquipmentState.e2Working = true;  applyE2State(true);  saveToFirestore(); }
    @FXML private void handleE2No()  { EquipmentState.e2Working = false; applyE2State(false); saveToFirestore(); }

    // E3
    @FXML private void handleE3Yes() { EquipmentState.e3Working = true;  applyE3State(true);  saveToFirestore(); }
    @FXML private void handleE3No()  { EquipmentState.e3Working = false; applyE3State(false); saveToFirestore(); }

    // PC1
    @FXML private void handlePC1Yes() { EquipmentState.pc1Working = true;  applyPC1State(true);  saveToFirestore(); }
    @FXML private void handlePC1No()  { EquipmentState.pc1Working = false; applyPC1State(false); saveToFirestore(); }

    // P1 (wired to S1 buttons in your current FXML)
    @FXML private void handleS1Yes() { EquipmentState.p1Working = true;  applyS1State(true);  saveToFirestore(); }
    @FXML private void handleS1No()  { EquipmentState.p1Working = false; applyS1State(false); saveToFirestore(); }

    // OB1
    @FXML private void handleOB1Yes() { EquipmentState.ob1Working = true;  applyOB1State(true);  saveToFirestore(); }
    @FXML private void handleOB1No()  { EquipmentState.ob1Working = false; applyOB1State(false); saveToFirestore(); }

    // OI1
    @FXML private void handleOI1Yes() { EquipmentState.oi1Working = true;  applyOI1State(true);  saveToFirestore(); }
    @FXML private void handleOI1No()  { EquipmentState.oi1Working = false; applyOI1State(false); saveToFirestore(); }

    // C1
    @FXML private void handleC1Yes() { EquipmentState.c1Working = true;  applyC1State(true);  saveToFirestore(); }
    @FXML private void handleC1No()  { EquipmentState.c1Working = false; applyC1State(false); saveToFirestore(); }

    // AD1
    @FXML private void handleAD1Yes() { EquipmentState.ad1Working = true;  applyAD1State(true);  saveToFirestore(); }
    @FXML private void handleAD1No()  { EquipmentState.ad1Working = false; applyAD1State(false); saveToFirestore(); }

    // AB1
    @FXML private void handleAB1Yes() { EquipmentState.ab1Working = true;  applyAB1State(true);  saveToFirestore(); }
    @FXML private void handleAB1No()  { EquipmentState.ab1Working = false; applyAB1State(false); saveToFirestore(); }

    // W1
    @FXML private void handleW1Yes() { EquipmentState.w1Working = true;  applyW1State(true);  saveToFirestore(); }
    @FXML private void handleW1No()  { EquipmentState.w1Working = false; applyW1State(false); saveToFirestore(); }

    @FXML
    private void handleBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }

    private void applyAllFromMemory() {
        applyT1State(EquipmentState.t1Working);
        applyT2State(EquipmentState.t2Working);
        applyB1State(EquipmentState.b1Working);
        applyB2State(EquipmentState.b2Working);

        applyE1State(EquipmentState.e1Working);
        applyE2State(EquipmentState.e2Working);
        applyE3State(EquipmentState.e3Working);

        applyPC1State(EquipmentState.pc1Working);
        applyS1State(EquipmentState.p1Working);

        applyOB1State(EquipmentState.ob1Working);
        applyAD1State(EquipmentState.ad1Working);
        applyAB1State(EquipmentState.ab1Working);
        applyOI1State(EquipmentState.oi1Working);
        applyW1State(EquipmentState.w1Working);
        applyC1State(EquipmentState.c1Working);
    }

    private void applyT1State(boolean v) { applyState(t1StatusLabel, t1YesButton, t1NoButton, v); }
    private void applyT2State(boolean v) { applyState(t2StatusLabel, t2YesButton, t2NoButton, v); }
    private void applyB1State(boolean v) { applyState(b1StatusLabel, b1YesButton, b1NoButton, v); }
    private void applyB2State(boolean v) { applyState(b2StatusLabel, b2YesButton, b2NoButton, v); }

    private void applyE1State(boolean v) { applyState(e1StatusLabel, e1YesButton, e1NoButton, v); }
    private void applyE2State(boolean v) { applyState(e2StatusLabel, e2YesButton, e2NoButton, v); }
    private void applyE3State(boolean v) { applyState(e3StatusLabel, e3YesButton, e3NoButton, v); }

    private void applyPC1State(boolean v) { applyState(pc1StatusLabel, pc1YesButton, pc1NoButton, v); }
    private void applyS1State(boolean v)  { applyState(s1StatusLabel, s1YesButton, s1NoButton, v); }

    private void applyOB1State(boolean v) { applyState(ob1StatusLabel, ob1YesButton, ob1NoButton, v); }
    private void applyOI1State(boolean v) { applyState(oi1StatusLabel, oi1YesButton, oi1NoButton, v); }
    private void applyC1State(boolean v)  { applyState(c1StatusLabel, c1YesButton, c1NoButton, v); }
    private void applyAD1State(boolean v) { applyState(ad1StatusLabel, ad1YesButton, ad1NoButton, v); }
    private void applyAB1State(boolean v) { applyState(ab1StatusLabel, ab1YesButton, ab1NoButton, v); }
    private void applyW1State(boolean v)  { applyState(w1StatusLabel, w1YesButton, w1NoButton, v); }

    private void applyState(Label statusLabel, Button yesButton, Button noButton, boolean isWorking) {
        if (statusLabel != null) {
            statusLabel.setText(isWorking ? "YES" : "NO");
            statusLabel.setStyle(isWorking
                    ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                    : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
        }

        if (yesButton != null) {
            yesButton.setStyle(isWorking
                    ? "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;"
                    : "");
        }
        if (noButton != null) {
            noButton.setStyle(!isWorking
                    ? "-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;"
                    : "");
        }
    }

    private void saveToFirestore() {
        if (Main.fstore == null) {
            log.info("Firestore is null in AdminController; not saving to backend.");
            return;
        }

        try {
            DocumentReference doc = Main.fstore.collection("EquipmentStatus").document("current");

            Map<String, Object> data = new HashMap<>();
            data.put("T1",  EquipmentState.t1Working);
            data.put("T2",  EquipmentState.t2Working);
            data.put("B1",  EquipmentState.b1Working);
            data.put("B2",  EquipmentState.b2Working);

            data.put("E1",  EquipmentState.e1Working);
            data.put("E2",  EquipmentState.e2Working);
            data.put("E3",  EquipmentState.e3Working);

            data.put("PC1", EquipmentState.pc1Working);
            data.put("P1",  EquipmentState.p1Working);
            data.put("OB1", EquipmentState.ob1Working);
            data.put("AD1", EquipmentState.ad1Working);
            data.put("AB1", EquipmentState.ab1Working);
            data.put("OI1", EquipmentState.oi1Working);
            data.put("W1",  EquipmentState.w1Working);
            data.put("C1",  EquipmentState.c1Working);

            ApiFuture<WriteResult> write = doc.set(data, SetOptions.merge());
            write.get();
        } catch (Exception e) {
            log.info("Failed to save EquipmentStatus to Firestore: " + e.getMessage());
        }
    }
}