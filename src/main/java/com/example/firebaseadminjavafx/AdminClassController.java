package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;

public class AdminClassController {

    @FXML private TableView<GymClass> classTable;
    @FXML private TableColumn<GymClass, String> titleColumn;
    @FXML private TableColumn<GymClass, String> instructorColumn;
    @FXML private TableColumn<GymClass, String> timeColumn;
    @FXML private TableColumn<GymClass, String> capacityColumn;

    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private ObservableList<GymClass> classList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        classTable.setItems(classList);

        loadClassesFromFirestore();
    }

    @FXML
    private void handleAddClass() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-class-form.fxml"));
            Parent root = loader.load();
            AddClassController controller = loader.getController();
            controller.setOnSave(gymClass -> {
                classList.add(gymClass);
                saveGymClassToFirestore(gymClass);
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Gym Class");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteClass() {
        GymClass selected = classTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            classList.remove(selected);
            if (selected.getId() != null) {
                Main.fstore.collection("gymClasses").document(selected.getId()).delete();
            }
        } else {
            System.out.println("Select a class to delete");
        }
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    private void saveGymClassToFirestore(GymClass gymClass) {
        DocumentReference docRef = Main.fstore.collection("gymClasses").document();
        gymClass.setId(docRef.getId());
        docRef.set(gymClass);
    }

    private void loadClassesFromFirestore() {
        ApiFuture<QuerySnapshot> future = Main.fstore.collection("gymClasses").get();
        try {
            QuerySnapshot snapshot = future.get();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                GymClass gc = doc.toObject(GymClass.class);
                classList.add(gc);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
