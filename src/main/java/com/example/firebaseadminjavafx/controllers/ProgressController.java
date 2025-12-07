package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.Main;
import com.example.firebaseadminjavafx.logic.ProgressTracker;
import com.example.firebaseadminjavafx.logic.WeeklyProgress;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProgressController {

    @FXML private DatePicker datePickerWeek;
    @FXML private TextField txtWeight;
    @FXML private TextField txtGymVisits;
    @FXML private TextField txtMinutesAtGym;
    @FXML private TextField txtMachineSessions;
    @FXML private TextField txtCalories;

    @FXML private TableView<WeeklyProgress> tableProgress;
    @FXML private TableColumn<WeeklyProgress, String> colWeek;
    @FXML private TableColumn<WeeklyProgress, String> colWeight;
    @FXML private TableColumn<WeeklyProgress, Number> colGymVisits;
    @FXML private TableColumn<WeeklyProgress, Number> colMinutes;
    @FXML private TableColumn<WeeklyProgress, Number> colMachines;
    @FXML private TableColumn<WeeklyProgress, Number> colCalories;

    @FXML private Label lblSummary;

    @FXML private Button backButton;

    private final ProgressTracker tracker = new ProgressTracker();
    private final DateTimeFormatter weekFormat =
            DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private Firestore db;
    private String userId;

    @FXML
    private void initialize() {
        db = Main.fstore;
        userId = Main.currentUserUid;

        if (db == null) {
            System.out.println("Firestore not initialized; check Main.initFirebase.");
        }

        if (userId == null || userId.isEmpty()) {
            userId = "defaultUser";
            System.out.println("No current user set; using fallback userId = " + userId);
        } else {
            System.out.println("Using logged-in userId = " + userId);
        }

        setupTable();
        setupSelectionListener();
        loadFromFirestore();
        updateSummary();
    }

    private void setupTable() {
        ObservableList<WeeklyProgress> data = tracker.getEntries();
        tableProgress.setItems(data);

        colWeek.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getWeekStart().format(weekFormat)));

        colWeight.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.format("%.1f", c.getValue().getWeight())));

        colGymVisits.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getGymVisits()));

        colMinutes.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getTotalMinutesAtGym()));

        colMachines.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getMachineSessions()));

        colCalories.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getCaloriesBurned()));
    }

    private void setupSelectionListener() {
        tableProgress.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    if (newSel != null) {
                        datePickerWeek.setValue(newSel.getWeekStart());
                        txtWeight.setText(String.valueOf(newSel.getWeight()));
                        txtGymVisits.setText(String.valueOf(newSel.getGymVisits()));
                        txtMinutesAtGym.setText(String.valueOf(newSel.getTotalMinutesAtGym()));
                        txtMachineSessions.setText(String.valueOf(newSel.getMachineSessions()));
                        txtCalories.setText(String.valueOf(newSel.getCaloriesBurned()));
                    }
                }
        );
    }

    // ========= BUTTON HANDLERS =========

    @FXML
    private void onSaveClicked() {
        LocalDate date = datePickerWeek.getValue();
        if (date == null) {
            showError("Please select a date for the week.");
            return;
        }

        LocalDate weekStart = getWeekStart(date);

        Double weight = parseDoubleOrNull(txtWeight.getText().trim(), "Weight");
        if (weight == null && !txtWeight.getText().trim().isEmpty()) return;
        if (weight == null) weight = 0.0;

        Integer gymVisits = parseIntOrNull(txtGymVisits.getText().trim(), "Gym visits");
        if (gymVisits == null && !txtGymVisits.getText().trim().isEmpty()) return;
        if (gymVisits == null) gymVisits = 0;

        Integer minutes = parseIntOrNull(txtMinutesAtGym.getText().trim(), "Total minutes at gym");
        if (minutes == null && !txtMinutesAtGym.getText().trim().isEmpty()) return;
        if (minutes == null) minutes = 0;

        Integer machines = parseIntOrNull(txtMachineSessions.getText().trim(), "Machine sessions");
        if (machines == null && !txtMachineSessions.getText().trim().isEmpty()) return;
        if (machines == null) machines = 0;

        Integer calories = parseIntOrNull(txtCalories.getText().trim(), "Calories burned");
        if (calories == null && !txtCalories.getText().trim().isEmpty()) return;
        if (calories == null) calories = 0;

        WeeklyProgress wp = new WeeklyProgress(
                weekStart, weight, gymVisits, minutes, machines, calories
        );

        tracker.addOrUpdate(wp);
        tableProgress.refresh();

        saveEntryToFirestore(wp);

        clearInputs();
        updateSummary();
    }

    @FXML
    private void onDeleteClicked() {
        WeeklyProgress selected = tableProgress.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tracker.remove(selected);
            tableProgress.getSelectionModel().clearSelection();
            clearInputs();
            updateSummary();

            deleteEntryFromFirestore(selected);
        } else {
            showError("Please select a row to delete.");
        }
    }

    @FXML
    private void onClearFormClicked() {
        clearInputs();
        tableProgress.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    // ========= FIRESTORE =========

    private CollectionReference getUserProgressCollection() {
        if (db == null) {
            System.out.println("Firestore not initialized; skipping Firestore operations.");
            return null;
        }
        return db.collection("users")
                .document(userId)
                .collection("weeklyProgress");
    }

    private void loadFromFirestore() {
        try {
            CollectionReference col = getUserProgressCollection();
            if (col == null) return;

            ApiFuture<QuerySnapshot> future = col.get();
            QuerySnapshot snapshot = future.get();

            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
                String weekStartStr = doc.getString("weekStart");
                Double weight = doc.getDouble("weight");
                Long gymVisits = doc.getLong("gymVisits");
                Long minutes = doc.getLong("totalMinutesAtGym");
                Long machines = doc.getLong("machineSessions");
                Long calories = doc.getLong("caloriesBurned");

                if (weekStartStr == null) {
                    continue;
                }

                LocalDate weekStart = LocalDate.parse(weekStartStr);
                WeeklyProgress wp = new WeeklyProgress(
                        weekStart,
                        weight == null ? 0.0 : weight,
                        gymVisits == null ? 0 : gymVisits.intValue(),
                        minutes == null ? 0 : minutes.intValue(),
                        machines == null ? 0 : machines.intValue(),
                        calories == null ? 0 : calories.intValue()
                );

                tracker.addOrUpdate(wp);
            }

            tableProgress.refresh();
            updateSummary();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void saveEntryToFirestore(WeeklyProgress wp) {
        try {
            CollectionReference col = getUserProgressCollection();
            if (col == null) return;

            String docId = wp.getWeekStart().toString();
            DocumentReference docRef = col.document(docId);

            Map<String, Object> data = new HashMap<>();
            data.put("weekStart", wp.getWeekStart().toString());
            data.put("weight", wp.getWeight());
            data.put("gymVisits", wp.getGymVisits());
            data.put("totalMinutesAtGym", wp.getTotalMinutesAtGym());
            data.put("machineSessions", wp.getMachineSessions());
            data.put("caloriesBurned", wp.getCaloriesBurned());

            ApiFuture<WriteResult> future = docRef.set(data);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void deleteEntryFromFirestore(WeeklyProgress wp) {
        try {
            CollectionReference col = getUserProgressCollection();
            if (col == null) return;

            String docId = wp.getWeekStart().toString();
            DocumentReference docRef = col.document(docId);

            ApiFuture<WriteResult> future = docRef.delete();
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // ========= HELPERS =========

    private void clearInputs() {
        datePickerWeek.setValue(null);
        txtWeight.clear();
        txtGymVisits.clear();
        txtMinutesAtGym.clear();
        txtMachineSessions.clear();
        txtCalories.clear();
    }

    private LocalDate getWeekStart(LocalDate anyDay) {
        while (anyDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            anyDay = anyDay.minusDays(1);
        }
        return anyDay;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Double parseDoubleOrNull(String text, String fieldName) {
        if (text.isEmpty()) return null;
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            showError(fieldName + " must be a number.");
            return null;
        }
    }

    private Integer parseIntOrNull(String text, String fieldName) {
        if (text.isEmpty()) return null;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            showError(fieldName + " must be a whole number.");
            return null;
        }
    }

    private void updateSummary() {
        ObservableList<WeeklyProgress> entries = tracker.getEntries();
        int totalWeeks = entries.size();
        int totalGymVisits = entries.stream().mapToInt(WeeklyProgress::getGymVisits).sum();
        int totalMinutes = entries.stream().mapToInt(WeeklyProgress::getTotalMinutesAtGym).sum();
        int totalCalories = entries.stream().mapToInt(WeeklyProgress::getCaloriesBurned).sum();

        lblSummary.setText(String.format(
                "Weeks tracked: %d | Total gym visits: %d | Total minutes: %d | Total calories: %d",
                totalWeeks, totalGymVisits, totalMinutes, totalCalories
        ));
    }
}
