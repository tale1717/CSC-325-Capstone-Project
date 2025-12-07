package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.GymClass;
import com.example.firebaseadminjavafx.logic.Main;
import com.google.cloud.firestore.DocumentReference;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddClassController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField instructorField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField capacityField;

    @FXML
    private CheckBox focusFullBody;

    @FXML
    private CheckBox focusUpperBody;

    @FXML
    private CheckBox focusLowerBody;

    @FXML
    private CheckBox focusCardio;

    @FXML
    private CheckBox focusStrength;

    @FXML
    private CheckBox focusFlexibility;

    private GymClass editingClass;
    private Stage dialogStage;
    private Runnable onSaveOrCancel;

    public void setEditingClass(GymClass editingClass) {
        this.editingClass = editingClass;
        if (editingClass != null) {
            nameField.setText(editingClass.getName());
            instructorField.setText(editingClass.getInstructor());
            timeField.setText(editingClass.getTime());
            capacityField.setText(String.valueOf(editingClass.getCapacity()));

            List<String> areas = editingClass.getFocusAreas();
            focusFullBody.setSelected(areas.contains("Full Body"));
            focusUpperBody.setSelected(areas.contains("Upper Body"));
            focusLowerBody.setSelected(areas.contains("Lower Body"));
            focusCardio.setSelected(areas.contains("Cardio"));
            focusStrength.setSelected(areas.contains("Strength / Weights"));
            focusFlexibility.setSelected(areas.contains("Flexibility / Mobility"));
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOnSaveOrCancel(Runnable onSaveOrCancel) {
        this.onSaveOrCancel = onSaveOrCancel;
    }

    @FXML
    private void handleAddClass() {
        String name = safeText(nameField);
        String instructor = safeText(instructorField);
        String time = safeText(timeField);
        String capacityText = safeText(capacityField);

        if (name.isEmpty() || instructor.isEmpty() || time.isEmpty() || capacityText.isEmpty()) {
            showInfo("Missing Data", "Please fill in all fields.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            showInfo("Invalid Capacity", "Capacity must be a number.");
            return;
        }

        List<String> focusAreas = collectFocusAreas();
        if (focusAreas.isEmpty()) {
            showInfo("Missing Focus Areas", "Please select at least one focus area.");
            return;
        }

        try {
            if (editingClass == null) {
                DocumentReference docRef =
                        Main.fstore.collection("gymClasses").document();
                String id = docRef.getId();

                GymClass gc = new GymClass(id, name, instructor, time, capacity, focusAreas);
                docRef.set(gc).get();
            } else {
                DocumentReference docRef =
                        Main.fstore.collection("gymClasses").document(editingClass.getId());

                editingClass.setName(name);
                editingClass.setInstructor(instructor);
                editingClass.setTime(time);
                editingClass.setCapacity(capacity);
                editingClass.setFocusAreas(focusAreas);

                docRef.set(editingClass).get();
            }

            if (onSaveOrCancel != null) {
                onSaveOrCancel.run();
            }
            if (dialogStage != null) {
                dialogStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error saving class", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if (onSaveOrCancel != null) {
            onSaveOrCancel.run();
        }
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private List<String> collectFocusAreas() {
        List<String> areas = new ArrayList<>();
        if (focusFullBody.isSelected()) {
            areas.add("Full Body");
        }
        if (focusUpperBody.isSelected()) {
            areas.add("Upper Body");
        }
        if (focusLowerBody.isSelected()) {
            areas.add("Lower Body");
        }
        if (focusCardio.isSelected()) {
            areas.add("Cardio");
        }
        if (focusStrength.isSelected()) {
            areas.add("Strength / Weights");
        }
        if (focusFlexibility.isSelected()) {
            areas.add("Flexibility / Mobility");
        }
        return areas;
    }

    private String safeText(TextField tf) {
        return tf.getText() == null ? "" : tf.getText().trim();
    }

    private void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
