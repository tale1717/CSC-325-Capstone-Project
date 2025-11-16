package com.example.firebaseadminjavafx;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class WorkoutPlanningController {

    private static final Logger log = Logger.getLogger(WorkoutPlanningController.class.getName());

    @FXML private TextField nameField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> goalBox;
    @FXML private TextArea resultArea;
    @FXML private Button generateButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        goalBox.getItems().setAll("Lose Weight", "Gain Muscle", "Maintain");

        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            resultArea.setText("Not signed in. Please go back and sign in first.");
            disableInputs(true);
            return;
        }

        try {
            DocumentReference userDoc =
                    Main.fstore.collection("Users").document(Main.currentUserUid);
            ApiFuture<DocumentSnapshot> fut = userDoc.get();
            DocumentSnapshot snap = fut.get();

            if (snap.exists()) {
                String existingName   = snap.getString("name");
                Long   wpAge          = snap.getLong("wpAge");
                Double wpHeightCm     = snap.getDouble("wpHeightCm");
                Double wpWeightKg     = snap.getDouble("wpWeightKg");
                String wpGoal         = snap.getString("wpGoal");
                String wpResult       = snap.getString("wpResultText");

                if (existingName != null)  nameField.setText(existingName);
                if (wpAge != null)         ageField.setText(String.valueOf(wpAge));
                if (wpHeightCm != null)    heightField.setText(String.valueOf(wpHeightCm));
                if (wpWeightKg != null)    weightField.setText(String.valueOf(wpWeightKg));
                if (wpGoal != null)        goalBox.setValue(wpGoal);
                if (wpResult != null)      resultArea.setText(wpResult);
            }
        } catch (Exception e) {
            log.info("Workout prefill failed: " + e.getMessage());
            resultArea.setText("Could not load saved workout plan.");
        }
    }

    @FXML
    private void handleGenerateAndSave() {
        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            resultArea.setText("Not signed in.");
            log.info("Workout save aborted: no signed-in user.");
            return;
        }

        String name  = safeTrim(nameField.getText());
        String heightS = safeTrim(heightField.getText());
        String weightS = safeTrim(weightField.getText());
        String ageS    = safeTrim(ageField.getText());
        String goal    = goalBox.getValue();

        if (name.isEmpty() || heightS.isEmpty() || weightS.isEmpty()
                || ageS.isEmpty() || goal == null || goal.isEmpty()) {
            resultArea.setText("Please fill in all fields and select a goal.");
            return;
        }

        double heightCm;
        double weightKg;
        int age;

        try {
            heightCm = Double.parseDouble(heightS);
            weightKg = Double.parseDouble(weightS);
            age = Integer.parseInt(ageS);
        } catch (NumberFormatException ex) {
            resultArea.setText("Height, weight, and age must be valid numbers.");
            return;
        }

        if (age <= 0 || age > 120 || heightCm <= 0 || weightKg <= 0) {
            resultArea.setText("Please enter realistic values.");
            return;
        }

        User user = new User(age, heightCm, weightKg, goal);
        CalorieCalculator calculator = new CalorieCalculator();
        GoalPlanner planner = new GoalPlanner();

        double calories = calculator.calculateCalories(user);
        double bmi = weightKg / Math.pow(heightCm / 100.0, 2);
        String workoutPlan = planner.getWorkoutPlan(goal);

        String resultText =
                "Name: " + name + "\n\n" +
                        "Height: " + String.format("%.1f", heightCm) + " cm\n" +
                        "Weight: " + String.format("%.1f", weightKg) + " kg\n" +
                        "Age: " + age + "\n" +
                        "Goal: " + goal + "\n\n" +
                        String.format("Estimated BMI: %.1f\n", bmi) +
                        String.format("Estimated Daily Calories: %.0f kcal\n\n", calories) +
                        "Workout plan:\n" + workoutPlan;

        resultArea.setText(resultText);

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("wpAge", age);
        data.put("wpHeightCm", heightCm);
        data.put("wpWeightKg", weightKg);
        data.put("wpGoal", goal);
        data.put("wpResultText", resultText);
        data.put("wpUpdatedAt", Instant.now().toString());

        try {
            DocumentReference userDoc =
                    Main.fstore.collection("Users").document(Main.currentUserUid);
            ApiFuture<WriteResult> write = userDoc.set(data, SetOptions.merge());
            write.get();

            log.info("Workout plan saved for user: " + Main.currentUserEmail);
        } catch (Exception e) {
            e.printStackTrace();
            resultArea.setText("Failed to save workout plan. See console.");
            log.info("Workout save failed for user: " + Main.currentUserEmail);
        }
    }

    @FXML
    private void handleBack() {
        Main.setRoot("next-step.fxml", backButton);
    }

    private void disableInputs(boolean disable) {
        nameField.setDisable(disable);
        heightField.setDisable(disable);
        weightField.setDisable(disable);
        ageField.setDisable(disable);
        goalBox.setDisable(disable);
        generateButton.setDisable(disable);
    }

    private static String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}