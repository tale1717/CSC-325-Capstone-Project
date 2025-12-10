package com.example.firebaseadminjavafx.controllers;

import com.example.firebaseadminjavafx.logic.Main;
import com.example.firebaseadminjavafx.logic.ProfileDataStore;
import com.example.firebaseadminjavafx.models.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

public class WorkoutPlanningController {

    private static final Logger log = Logger.getLogger(WorkoutPlanningController.class.getName());

    @FXML private TextField nameField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> goalBox;
    @FXML private ComboBox<String> experienceLevelBox;
    @FXML private ComboBox<Integer> sessionsPerWeekBox;
    @FXML private ListView<String> focusAreasList;
    @FXML private TextArea resultArea;
    @FXML private Button backButton;

    private final ObservableSet<String> selectedFocusAreas = FXCollections.observableSet();

    @FXML
    private void initialize() {
        // Expanded goal options
        if (goalBox != null && goalBox.getItems().isEmpty()) {
            goalBox.getItems().addAll(
                    "Lose Weight / Fat",
                    "Gain Muscle / Strength",
                    "Body Recomposition (Lean & Strong)",
                    "Improve Endurance / Cardio",
                    "Maintain / General Fitness"
            );
            goalBox.getSelectionModel().selectFirst();
        }

        if (experienceLevelBox != null && experienceLevelBox.getItems().isEmpty()) {
            experienceLevelBox.getItems().addAll("Beginner", "Intermediate", "Advanced");
            experienceLevelBox.getSelectionModel().select("Beginner");
        }

        if (sessionsPerWeekBox != null && sessionsPerWeekBox.getItems().isEmpty()) {
            sessionsPerWeekBox.getItems().addAll(1, 2, 3, 4, 5, 6);
            sessionsPerWeekBox.getSelectionModel().select(Integer.valueOf(3));
        }

        if (focusAreasList != null && focusAreasList.getItems().isEmpty()) {
            focusAreasList.getItems().addAll(
                    "Upper Body",
                    "Lower Body",
                    "Full Body",
                    "Core/Abs",
                    "Cardio/Endurance",
                    "Flexibility/Mobility"
            );

            focusAreasList.setCellFactory(listView ->
                    new CheckBoxListCell<>(item -> {
                        BooleanProperty observable =
                                new SimpleBooleanProperty(selectedFocusAreas.contains(item));
                        observable.addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                selectedFocusAreas.add(item);
                            } else {
                                selectedFocusAreas.remove(item);
                            }
                        });
                        return observable;
                    })
            );
        }

        // Prefill from Firestore (support both CSV string and List<String>)
        try {
            if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
                log.info("No signed-in user; skipping workout plan prefill.");
                return;
            }

            DocumentReference doc =
                    Main.fstore.collection("WorkoutPlans").document(Main.currentUserUid);

            ApiFuture<DocumentSnapshot> fut = doc.get();
            DocumentSnapshot snap = fut.get();
            if (snap.exists()) {
                String name   = snap.getString("name");
                Double hIn    = snap.getDouble("heightIn");
                Double wLb    = snap.getDouble("weightLb");
                Long age      = snap.getLong("age");
                String goal   = snap.getString("goal");
                String summary = snap.getString("planSummary");
                String expLevel = snap.getString("experienceLevel");
                Long sessionsPerWeek = snap.getLong("sessionsPerWeek");

                if (name != null)      nameField.setText(name);
                if (hIn != null)       heightField.setText(String.valueOf(hIn));
                if (wLb != null)       weightField.setText(String.valueOf(wLb));
                if (age != null)       ageField.setText(String.valueOf(age));
                if (goal != null && goalBox != null) {
                    goalBox.getSelectionModel().select(goal);
                }
                if (expLevel != null && experienceLevelBox != null) {
                    experienceLevelBox.getSelectionModel().select(expLevel);
                }
                if (sessionsPerWeek != null && sessionsPerWeekBox != null) {
                    sessionsPerWeekBox.getSelectionModel()
                            .select(sessionsPerWeek.intValue());
                }

                // focusAreas: support both List<String> (new) and CSV string (old)
                if (focusAreasList != null) {
                    selectedFocusAreas.clear();
                    List<String> focusList = new ArrayList<>();

                    Object focusObj = snap.get("focusAreas");
                    if (focusObj instanceof java.util.List) {
                        @SuppressWarnings("unchecked")
                        List<String> storedList = (List<String>) focusObj;
                        for (String f : storedList) {
                            if (f != null && !f.trim().isEmpty()) {
                                String trimmed = f.trim();
                                selectedFocusAreas.add(trimmed);
                                focusList.add(trimmed);
                            }
                        }
                    } else if (focusObj instanceof String) {
                        String focusAreasCsv = (String) focusObj;
                        String[] parts = focusAreasCsv.split(",");
                        for (String raw : parts) {
                            String trimmed = raw.trim();
                            if (!trimmed.isEmpty()) {
                                selectedFocusAreas.add(trimmed);
                                focusList.add(trimmed);
                            }
                        }
                    }

                    focusAreasList.refresh();

                    // Rebuild in-memory profile so Suggested Classes works on reload
                    if (!focusList.isEmpty()) {
                        UserProfile profile = new UserProfile(focusList);
                        ProfileDataStore.setCurrentProfile(profile);
                    }
                }

                if (summary != null && resultArea != null) {
                    resultArea.setText(summary);
                }
            }
        } catch (Exception e) {
            log.info("Workout plan prefill failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateAndSave() {
        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            appendResult("Not signed in.");
            log.info("Workout plan save aborted: no signed-in user.");
            return;
        }

        String nameText   = safeTrim(nameField.getText());
        String heightText = safeTrim(heightField.getText());
        String weightText = safeTrim(weightField.getText());
        String ageText    = safeTrim(ageField.getText());
        String goal       = (goalBox.getValue() == null)
                ? "Maintain / General Fitness"
                : goalBox.getValue();
        String experienceLevel = (experienceLevelBox.getValue() == null)
                ? "Beginner"
                : experienceLevelBox.getValue();
        Integer sessionsPerWeekVal = (sessionsPerWeekBox.getValue() == null)
                ? 3
                : sessionsPerWeekBox.getValue();

        if (nameText.isEmpty() || heightText.isEmpty() || weightText.isEmpty() || ageText.isEmpty()) {
            appendResult("Please fill in all fields (name, height, weight, age).");
            return;
        }

        if (selectedFocusAreas.isEmpty()) {
            appendResult("Please choose at least one focus area.");
            return;
        }

        double heightIn;
        double weightLb;
        int age;
        try {
            heightIn = Double.parseDouble(heightText);
            weightLb = Double.parseDouble(weightText);
            age      = Integer.parseInt(ageText);

            if (heightIn <= 0 || weightLb <= 0 || age <= 0) {
                appendResult("Height, weight, and age must be positive.");
                return;
            }
        } catch (NumberFormatException nfe) {
            appendResult("Height (in), weight (lb), and age must be valid numbers.");
            return;
        }

        double heightCm = heightIn * 2.54;
        double weightKg = weightLb * 0.45359237;
        double bmi = weightKg / Math.pow(heightCm / 100.0, 2);
        double bmr = 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        double calories = bmr * 1.2;

        String goalLower = goal.toLowerCase(Locale.ROOT);

        if (goalLower.contains("lose")) {
            calories -= 500;
        } else if (goalLower.contains("gain")) {
            calories += 300; // a bit more conservative surplus
        } else if (goalLower.contains("recomposition")) {
            // stay around maintenance, let training do most of the work
            calories = bmr * 1.2;
        } else if (goalLower.contains("endurance")) {
            // slight surplus can help with long sessions
            calories += 150;
        }

        List<String> focusList = new ArrayList<>(selectedFocusAreas);
        focusList.sort(String::compareToIgnoreCase);
        String focusAreasCsv = String.join(", ", focusList);

        // Make profile available to Suggested Classes (in memory)
        UserProfile profile = new UserProfile(focusList);
        ProfileDataStore.setCurrentProfile(profile);

        StringBuilder planBuilder = new StringBuilder();

        planBuilder.append("Base plan:\n");
        if (goalLower.contains("lose")) {
            planBuilder.append("- Calorie deficit of about 500 kcal/day.\n")
                    .append("- Cardio 4–5x/week (20–40 min).\n")
                    .append("- Full-body resistance training 2–3x/week.\n");
        } else if (goalLower.contains("gain")) {
            planBuilder.append("- Calorie surplus of about 300–500 kcal/day.\n")
                    .append("- Progressive overload with compound lifts 3–4x/week.\n")
                    .append("- 1–2 days of light cardio or active recovery.\n");
        } else if (goalLower.contains("recomposition")) {
            planBuilder.append("- Calories around maintenance (small surplus or deficit of 150–200 kcal).\n")
                    .append("- Strength training 3–4x/week with progressive overload.\n")
                    .append("- 2–3 moderate cardio sessions to support fat loss.\n");
        } else if (goalLower.contains("endurance")) {
            planBuilder.append("- Focus on steady-state and interval cardio 4–5x/week.\n")
                    .append("- 1–2 strength sessions to maintain muscle.\n")
                    .append("- Pay attention to carbs and hydration around workouts.\n");
        } else {
            planBuilder.append("- Maintain calories around current maintenance.\n")
                    .append("- Mix of strength and cardio 3–4x/week.\n")
                    .append("- 1–2 days of mobility / flexibility.\n");
        }

        planBuilder.append("\nExperience level: ").append(experienceLevel).append("\n");
        planBuilder.append("Sessions per week target: ").append(sessionsPerWeekVal).append("\n");
        planBuilder.append("Focus areas: ").append(focusAreasCsv).append("\n\n");

        planBuilder.append("Suggested machines to prioritize based on focus:\n");
        Map<String, List<String>> focusToMachines = new HashMap<>();
        focusToMachines.put("Upper Body", Arrays.asList("P1 Preacher Curl", "S1 Shoulder Press", "OB1 Olympic Bench", "OI1 Olympic Incline", "C1 Cable Machine", "W1 Weight Assist Dip/Chin"));
        focusToMachines.put("Lower Body", Arrays.asList("AD1 Adductor", "AB1 Abductor", "E1/E2/E3 Ellipticals"));
        focusToMachines.put("Full Body", Arrays.asList("T1/T2 Treadmills", "E1/E2/E3 Ellipticals", "C1 Cable Machine", "W1 Weight Assist Dip/Chin"));
        focusToMachines.put("Core/Abs", Arrays.asList("Cable core work at C1", "Bodyweight planks near free-weight area"));
        focusToMachines.put("Cardio/Endurance", Arrays.asList("T1/T2 Treadmills", "E1/E2/E3 Ellipticals"));
        focusToMachines.put("Flexibility/Mobility", Arrays.asList("Stretching area (mats)", "Light cable and bodyweight movements"));

        List<String> machineSuggestions = new ArrayList<>();
        for (String focus : focusList) {
            List<String> mapped = focusToMachines.get(focus);
            if (mapped != null) {
                for (String m : mapped) {
                    if (!machineSuggestions.contains(m)) {
                        machineSuggestions.add(m);
                    }
                }
            }
        }
        for (String m : machineSuggestions) {
            planBuilder.append("- ").append(m).append("\n");
        }

        String summary = String.format(
                "Name: %s%n" +
                        "Height: %.1f in%n" +
                        "Weight: %.1f lb%n" +
                        "Age: %d%n" +
                        "Goal: %s%n" +
                        "Experience level: %s%n" +
                        "Sessions per week: %d%n" +
                        "Focus areas: %s%n%n" +
                        "BMI: %.1f%n" +
                        "Estimated daily calories: %.0f kcal%n%n" +
                        "Plan:%n%s",
                nameText, heightIn, weightLb, age, goal, experienceLevel, sessionsPerWeekVal,
                focusAreasCsv, bmi, calories, planBuilder.toString()
        );

        resultArea.setText(summary);

        Map<String, Object> data = new HashMap<>();
        data.put("userEmail", Main.currentUserEmail);
        data.put("name", nameText);
        data.put("heightIn", heightIn);
        data.put("weightLb", weightLb);
        data.put("age", age);
        data.put("goal", goal);
        data.put("bmi", bmi);
        data.put("dailyCalories", calories);
        data.put("planSummary", summary);
        data.put("experienceLevel", experienceLevel);
        data.put("sessionsPerWeek", sessionsPerWeekVal);
        // Store focusAreas as a List<String> now
        data.put("focusAreas", focusList);
        data.put("updatedAt", Instant.now().toString());

        try {
            DocumentReference doc =
                    Main.fstore.collection("WorkoutPlans").document(Main.currentUserUid);

            ApiFuture<WriteResult> write = doc.set(data, SetOptions.merge());
            write.get();

            log.info("Workout plan saved for user: " + Main.currentUserEmail);
        } catch (Exception e) {
            e.printStackTrace();
            appendResult("\n\nSave failed. See console.");
            log.info("Workout plan save failed for user: " + Main.currentUserEmail);
        }
    }

    @FXML
    private void handleBack() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private void appendResult(String text) {
        if (resultArea == null) return;
        if (resultArea.getText() == null || resultArea.getText().isEmpty()) {
            resultArea.setText(text);
        } else {
            resultArea.setText(resultArea.getText() + "\n" + text);
        }
    }
}
