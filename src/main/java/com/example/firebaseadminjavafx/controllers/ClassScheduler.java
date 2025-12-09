package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.GymClass;
import com.example.firebaseadminjavafx.logic.Main;
import com.example.firebaseadminjavafx.logic.ProfileDataStore;
import com.example.firebaseadminjavafx.models.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassScheduler {

    @FXML
    private Button suggestClassButton;

    @FXML
    private Button searchClassButton;

    @FXML
    private Button backButton;

    @FXML
    private Button adminButton;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> classListView;

    private final List<GymClass> allClasses = new ArrayList<>();

    @FXML
    public void initialize() {
        loadClassesFromFirestore();
    }

    private void loadClassesFromFirestore() {
        try {
            ApiFuture<QuerySnapshot> future =
                    Main.fstore.collection("gymClasses").get();

            QuerySnapshot snapshot = future.get();
            allClasses.clear();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                GymClass gc = doc.toObject(GymClass.class);
                if (gc != null) {
                    gc.setId(doc.getId());
                    allClasses.add(gc);
                }
            }

            List<String> lines = allClasses.stream()
                    .map(this::formatClassLine)
                    .collect(Collectors.toList());

            classListView.getItems().setAll(lines);

        } catch (Exception e) {
            e.printStackTrace();
            showInfo("Error", "Could not load classes from Firestore.");
        }
    }

    private String formatClassLine(GymClass gc) {
        String focus = gc.getFocusAreasString();
        if (focus == null || focus.isBlank()) {
            focus = "General";
        }

        return gc.getName()
                + " | " + gc.getTime()
                + " | " + gc.getInstructor()
                + " | Cap: " + gc.getCapacity()
                + " | Focus: " + focus;
    }

    @FXML
    private void handleOpenClass() {
        int index = classListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= allClasses.size()) {
            showInfo("No Selection", "Please select a class from the list.");
            return;
        }

        GymClass gc = allClasses.get(index);
        showClassDetails(gc);
    }

    @FXML
    private void handleAdminButton() {
        Main.setRoot("class-scheduler-home.fxml");
    }

    @FXML
    private void handleBackToMain() {
        Main.setRoot("class-scheduler-home.fxml");
    }

    @FXML
    private void handleSearchClass() {
        String term = (searchField == null || searchField.getText() == null)
                ? ""
                : searchField.getText().trim().toLowerCase();

        if (term.isEmpty()) {
            List<String> allLines = allClasses.stream()
                    .map(this::formatClassLine)
                    .collect(Collectors.toList());
            classListView.getItems().setAll(allLines);
            return;
        }

        List<GymClass> filtered = allClasses.stream()
                .filter(gc -> gc.getName() != null &&
                        gc.getName().toLowerCase().startsWith(term))
                .collect(Collectors.toList());

        List<String> lines = filtered.stream()
                .map(this::formatClassLine)
                .collect(Collectors.toList());

        classListView.getItems().setAll(lines);
    }

    // New: normalize focus names so small label differences still match
    private String normalizeFocus(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        if (t.startsWith("cardio")) {
            return "cardio"; // "cardio", "cardio/endurance", etc.
        }
        if (t.equals("core") || t.equals("abs") || t.equals("core/abs")) {
            return "core/abs";
        }
        return t;
    }

    @FXML
    private void handleSuggestClasses() {
        if (!ProfileDataStore.hasProfile()) {
            showInfo("No Profile",
                    "Please create a workout plan first so we know your focus areas.");
            return;
        }

        UserProfile profile = ProfileDataStore.getCurrentProfile();
        if (profile == null || profile.getFocusAreas() == null
                || profile.getFocusAreas().isEmpty()) {
            showInfo("No Focus Areas",
                    "Your profile does not have any focus areas yet.");
            return;
        }

        Set<String> userFocus =
                profile.getFocusAreas().stream()
                        .filter(Objects::nonNull)
                        .map(this::normalizeFocus)
                        .collect(Collectors.toSet());

        List<GymClass> matches = allClasses.stream()
                .filter(gc -> {
                    List<String> focusList = gc.getFocusAreas();
                    if (focusList == null || focusList.isEmpty()) {
                        return false;
                    }
                    for (String f : focusList) {
                        if (f != null && userFocus.contains(normalizeFocus(f))) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            showInfo("No Matches",
                    "No classes match your current workout focus areas yet.");
            return;
        }

        List<String> lines = matches.stream()
                .map(this::formatClassLine)
                .collect(Collectors.toList());

        classListView.getItems().setAll(lines);
    }

    private void showClassDetails(GymClass gymClass) {
        String focus = gymClass.getFocusAreasString();
        if (focus == null || focus.isBlank()) {
            focus = "General";
        }

        String message =
                "Name: " + gymClass.getName() + "\n"
                        + "Instructor: " + gymClass.getInstructor() + "\n"
                        + "Time: " + gymClass.getTime() + "\n"
                        + "Capacity: " + gymClass.getCapacity() + "\n"
                        + "Focus Areas: " + focus;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Class Details");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
