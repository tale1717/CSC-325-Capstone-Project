package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.ExecutionException;

public class ClassScheduler {

    @FXML
    private Button suggestClassButton;

    @FXML
    private Button searchClassButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<GymClass> classListView;

    private ObservableList<GymClass> allClasses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadClassesFromFirestore();
        classListView.setItems(allClasses);

        classListView.setCellFactory(param -> new ListCell<>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Button viewButton = new Button("View");

            {
                label.setStyle("-fx-text-fill: black;");
                hbox.setSpacing(10);
                hbox.getChildren().addAll(label, viewButton);
                HBox.setHgrow(label, Priority.ALWAYS);

                viewButton.setOnAction(event -> {
                    GymClass gymClass = getItem();
                    if (gymClass != null) showClassDetails(gymClass);
                });
            }

            @Override
            protected void updateItem(GymClass gymClass, boolean empty) {
                super.updateItem(gymClass, empty);
                if (empty || gymClass == null) {
                    setGraphic(null);
                }
                else {
                    label.setText(gymClass.getTitle());
                    setGraphic(hbox);
                }
            }
        });
    }

    //PLACEHOLDER FXML FILE
    @FXML
    private void handleSuggestClass(){
        Main.setRoot("class-scheduler.fxml", suggestClassButton);
    }

    @FXML
    private void handleSearchClass() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        ObservableList<GymClass> filtered = FXCollections.observableArrayList();

        for (GymClass gymClass : allClasses) {
            if (gymClass.getTitle().toLowerCase().startsWith(searchTerm)) {
                filtered.add(gymClass);
            }
        }

        classListView.setItems(filtered);
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    private void showClassDetails(GymClass gymClass) {
        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Class Details");
        detailsAlert.setHeaderText(gymClass.getTitle());
        detailsAlert.setContentText(
                "Instructor: " + gymClass.getInstructor() + "\n" +
                        "Time: " + gymClass.getTime() + "\n" +
                        "Capacity: " + gymClass.getCapacity()
        );
        detailsAlert.showAndWait();
    }

    private void loadClassesFromFirestore() {
        ApiFuture<QuerySnapshot> future = Main.fstore.collection("gymClasses").get();
        try {
            QuerySnapshot snapshot = future.get();
            allClasses.clear();

            for (DocumentSnapshot documentSnapshot: snapshot.getDocuments()) {
                GymClass gc = documentSnapshot.toObject(GymClass.class);
                allClasses.add(gc);
            }
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
