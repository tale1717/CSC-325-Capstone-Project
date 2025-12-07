package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.GymClass;
import com.example.firebaseadminjavafx.logic.Main;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminClassController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<GymClass> classTable;

    @FXML
    private TableColumn<GymClass, String> titleColumn;

    @FXML
    private TableColumn<GymClass, String> instructorColumn;

    @FXML
    private TableColumn<GymClass, String> timeColumn;

    @FXML
    private TableColumn<GymClass, String> capacityColumn;

    @FXML
    private TableColumn<GymClass, String> focusColumn;

    private final List<GymClass> classes = new ArrayList<>();

    @FXML
    public void initialize() {
        setupColumns();
        loadClassesFromFirestore();
    }

    private void setupColumns() {
        titleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        instructorColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getInstructor()));

        timeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTime()));

        capacityColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCapacity())));

        focusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFocusAreasString()));
    }

    private void loadClassesFromFirestore() {
        try {
            ApiFuture<QuerySnapshot> future =
                    Main.fstore.collection("gymClasses").get();
            QuerySnapshot snapshot = future.get();

            classes.clear();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                String id = doc.getId();

                String name = doc.contains("name")
                        ? doc.getString("name")
                        : doc.getString("title");

                String instructor = doc.getString("instructor");
                String time = doc.getString("time");

                Long capLong = doc.getLong("capacity");
                int capacity = (capLong != null) ? capLong.intValue() : 0;

                List<String> focusAreas = new ArrayList<>();
                Object raw = doc.get("focusAreas");
                if (raw instanceof List<?>) {
                    for (Object o : (List<?>) raw) {
                        if (o != null) {
                            focusAreas.add(o.toString());
                        }
                    }
                } else if (raw instanceof String s) {
                    String[] parts = s.split(",");
                    for (String p : parts) {
                        String trimmed = p.trim();
                        if (!trimmed.isEmpty()) {
                            focusAreas.add(trimmed);
                        }
                    }
                }

                GymClass gc = new GymClass(id, name, instructor, time, capacity, focusAreas);
                classes.add(gc);
            }

            classTable.getItems().setAll(classes);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading classes", e.getMessage());
        }
    }

    @FXML
    private void handleAddClass() {
        openClassForm(null);
    }

    @FXML
    private void handleEditClass() {
        GymClass selected = classTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Please select a class to edit.");
            return;
        }
        openClassForm(selected);
    }

    @FXML
    private void handleDeleteClass() {
        GymClass selected = classTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Please select a class to delete.");
            return;
        }

        try {
            Main.fstore.collection("gymClasses")
                    .document(selected.getId())
                    .delete()
                    .get();

            loadClassesFromFirestore();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error deleting class", e.getMessage());
        }
    }

    private void openClassForm(GymClass gc) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/firebaseadminjavafx/add-class.fxml"));
            Parent root = loader.load();

            AddClassController controller = loader.getController();
            controller.setEditingClass(gc);
            controller.setOnSaveOrCancel(this::loadClassesFromFirestore);

            Stage dialog = new Stage();
            dialog.setTitle(gc == null ? "Add Class" : "Edit Class");
            controller.setDialogStage(dialog);

            dialog.initOwner(rootPane.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error opening form", e.getMessage());
        }
    }

    @FXML
    private void handleBackToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/firebaseadminjavafx/gymapp-home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
