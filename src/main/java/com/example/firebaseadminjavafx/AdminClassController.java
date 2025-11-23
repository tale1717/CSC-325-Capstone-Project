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
    @FXML private Button editButton;
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
    private void handleEditClass() {
        GymClass selected = classTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            System.out.println("Select A Class To Edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-class-form.fxml"));
            Parent root = loader.load();
            AddClassController controller = loader.getController();

            controller.setEditingClass(selected);

            controller.setOnSave(updatedClass -> {
                updateClassInFirestore(updatedClass);
                classTable.refresh();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Class");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteClass() {
        GymClass selected = classTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Select a class to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Class");
        confirm.setHeaderText("Are you sure you want this class deleted?");
        confirm.setContentText(selected.getTitle());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                //Remove from the Table
                classList.remove(selected);

                //Remove from Firestore
                if (selected.getId() != null) {
                    Main.fstore.collection("gymClasses")
                            .document(selected.getId()).delete();
                }

                System.out.println("You Have Successfully Deleted This Class!");
            }
        });
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    //Handling and maintaining the Firestore Database if we perform one of the above actions

    private void saveGymClassToFirestore(GymClass gymClass) {
        DocumentReference docRef = Main.fstore.collection("gymClasses").document();
        gymClass.setId(docRef.getId());
        docRef.set(gymClass);
    }

    private void updateClassInFirestore(GymClass gymClass) {
        if (gymClass.getId() == null)
            return;

        DocumentReference docReference =
                Main.fstore.collection("gymClasses").document(gymClass.getId());

        docReference.set(gymClass);
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
