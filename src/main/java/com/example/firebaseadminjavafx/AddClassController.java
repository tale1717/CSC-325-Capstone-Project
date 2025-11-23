package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AddClassController {

    @FXML private TextField nameField;
    @FXML private TextField instructorField;
    @FXML private TextField timeField;
    @FXML private TextField capacityField;

    private Consumer<GymClass> onSave;

    public void setOnSave(Consumer<GymClass> onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void handleAddClass() {
        String name = nameField.getText().trim();
        String instructor = instructorField.getText().trim();
        String time = timeField.getText().trim();
        String capacity = capacityField.getText().trim();

        if (name.isEmpty() || instructor.isEmpty() || time.isEmpty() || capacity.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        GymClass gymClass = new GymClass(null, name, instructor, time, capacity);

        if (onSave != null) {
            onSave.accept(gymClass);
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
