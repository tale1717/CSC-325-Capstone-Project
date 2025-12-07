package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.models.EquipmentState;
import com.example.firebaseadminjavafx.logic.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymLayoutController {

    @FXML private Label treadmill1StatusLabel;
    @FXML private Label treadmill2StatusLabel;

    @FXML private Label bench1StatusLabel;
    @FXML private Label bench2StatusLabel;

    @FXML private Label elliptical1StatusLabel;
    @FXML private Label elliptical2StatusLabel;
    @FXML private Label elliptical3StatusLabel;

    @FXML private Label preacherCurl1StatusLabel;
    @FXML private Label olympicBench1StatusLabel;
    @FXML private Label olympicIncline1StatusLabel;

    @FXML private Label chestPress1StatusLabel;       // visual-only for now

    @FXML private Label adductor1StatusLabel;
    @FXML private Label abductor1StatusLabel;

    @FXML private Label cable1StatusLabel;
    @FXML private Label weightAssistDipChinStatusLabel;

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        // Pull current values from our shared in-memory state
        applyState(treadmill1StatusLabel, EquipmentState.t1Working);
        applyState(treadmill2StatusLabel, EquipmentState.t2Working);

        applyState(bench1StatusLabel, EquipmentState.b1Working);
        applyState(bench2StatusLabel, EquipmentState.b2Working);

        applyState(elliptical1StatusLabel, EquipmentState.e1Working);
        applyState(elliptical2StatusLabel, EquipmentState.e2Working);
        applyState(elliptical3StatusLabel, EquipmentState.e3Working);

        applyState(preacherCurl1StatusLabel, EquipmentState.p1Working);
        applyState(olympicBench1StatusLabel, EquipmentState.ob1Working);
        applyState(olympicIncline1StatusLabel, EquipmentState.oi1Working);

        applyState(chestPress1StatusLabel, EquipmentState.pc1Working);

        applyState(adductor1StatusLabel, EquipmentState.ad1Working);
        applyState(abductor1StatusLabel, EquipmentState.ab1Working);

        // Cable machine C1
        applyState(cable1StatusLabel, EquipmentState.c1Working);

        // Weight assist dip/chin
        applyState(weightAssistDipChinStatusLabel, EquipmentState.w1Working);
    }

    private void applyState(Label label, boolean isWorking) {
        if (label == null) {
            return;
        }

        label.setText(isWorking ? "YES" : "NO");
        label.setStyle(isWorking
                ? "-fx-border-color: green; -fx-background-color: lightgreen; -fx-font-weight: bold;"
                : "-fx-border-color: darkred; -fx-background-color: red; -fx-font-weight: bold;");
    }

    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }
}