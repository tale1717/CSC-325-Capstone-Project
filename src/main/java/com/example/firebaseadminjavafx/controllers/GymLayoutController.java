package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.Main;
import com.example.firebaseadminjavafx.logic.SuggestedMachineStore;
import com.example.firebaseadminjavafx.models.EquipmentState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GymLayoutController {

    @FXML private Label treadmill1StatusLabel;        // T1
    @FXML private Label treadmill2StatusLabel;        // T2

    @FXML private Label bench1StatusLabel;            // B1
    @FXML private Label bench2StatusLabel;            // B2

    @FXML private Label elliptical1StatusLabel;       // E1
    @FXML private Label elliptical2StatusLabel;       // E2
    @FXML private Label elliptical3StatusLabel;       // E3

    @FXML private Label preacherCurl1StatusLabel;     // P1
    @FXML private Label olympicBench1StatusLabel;     // OB1
    @FXML private Label olympicIncline1StatusLabel;   // OI1

    @FXML private Label chestPress1StatusLabel;       // PC1

    @FXML private Label adductor1StatusLabel;         // AD1
    @FXML private Label abductor1StatusLabel;         // AB1

    @FXML private Label cable1StatusLabel;            // C1
    @FXML private Label weightAssistDipChinStatusLabel; // W1

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        applyState(treadmill1StatusLabel,       EquipmentState.t1Working,  "T1");
        applyState(treadmill2StatusLabel,       EquipmentState.t2Working,  "T2");

        applyState(bench1StatusLabel,           EquipmentState.b1Working,  "B1");
        applyState(bench2StatusLabel,           EquipmentState.b2Working,  "B2");

        applyState(elliptical1StatusLabel,      EquipmentState.e1Working,  "E1");
        applyState(elliptical2StatusLabel,      EquipmentState.e2Working,  "E2");
        applyState(elliptical3StatusLabel,      EquipmentState.e3Working,  "E3");

        applyState(preacherCurl1StatusLabel,    EquipmentState.p1Working,  "P1");
        applyState(olympicBench1StatusLabel,    EquipmentState.ob1Working, "OB1");
        applyState(olympicIncline1StatusLabel,  EquipmentState.oi1Working, "OI1");

        applyState(chestPress1StatusLabel,      EquipmentState.pc1Working, "PC1");

        applyState(adductor1StatusLabel,        EquipmentState.ad1Working, "AD1");
        applyState(abductor1StatusLabel,        EquipmentState.ab1Working, "AB1");

        applyState(cable1StatusLabel,           EquipmentState.c1Working,  "C1");
        applyState(weightAssistDipChinStatusLabel, EquipmentState.w1Working, "W1");
    }


    private void applyState(Label label, boolean isWorking, String machineCode) {
        if (label == null) return;

        label.setText(isWorking ? "YES" : "NO");

        StringBuilder css = new StringBuilder();
        if (isWorking) {
            css.append("-fx-border-color: green;")
                    .append(" -fx-background-color: lightgreen;")
                    .append(" -fx-font-weight: bold;");
        } else {
            css.append("-fx-border-color: darkred;")
                    .append(" -fx-background-color: red;")
                    .append(" -fx-font-weight: bold;");
        }

        // Add highlight glow if this machine is suggested
        if (SuggestedMachineStore.isSuggested(machineCode)) {
            css.append(" -fx-effect: dropshadow(three-pass-box, rgba(255,215,0,0.9), 25, 0, 0, 0);");
        }

        label.setStyle(css.toString());
    }


    @FXML
    private void returnBackToHome() {
        Main.setRoot("gym-layout-home.fxml", backButton);
    }
}
