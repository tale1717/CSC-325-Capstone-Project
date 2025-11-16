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
    @FXML private TextField heightField;  // in inches
    @FXML private TextField weightField;  // in pounds
    @FXML private TextField ageField;
    @FXML private ComboBox<String> goalBox;
    @FXML private TextArea resultArea;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        if (goalBox != null && goalBox.getItems().isEmpty()) {
            goalBox.getItems().addAll("Lose Weight", "Gain Muscle", "Maintain");
            goalBox.getSelectionModel().selectFirst();
        }


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

                if (name != null)      nameField.setText(name);
                if (hIn != null)       heightField.setText(String.valueOf(hIn));
                if (wLb != null)       weightField.setText(String.valueOf(wLb));
                if (age != null)       ageField.setText(String.valueOf(age));
                if (goal != null && goalBox != null) {
                    goalBox.getSelectionModel().select(goal);
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
        String heightText = safeTrim(heightField.getText());  // inches
        String weightText = safeTrim(weightField.getText());  // pounds
        String ageText    = safeTrim(ageField.getText());
        String goal       = (goalBox.getValue() == null)
                ? "Maintain"
                : goalBox.getValue();

        if (nameText.isEmpty() || heightText.isEmpty() || weightText.isEmpty() || ageText.isEmpty()) {
            appendResult("Please fill in all fields (name, height, weight, age).");
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

        String plan;
        if (goal.toLowerCase().contains("lose")) {
            calories -= 500;
            plan = "Focus on calorie deficit, cardio 4–5x/week, and lighter weights.";
        } else if (goal.toLowerCase().contains("gain")) {
            calories += 500;
            plan = "Focus on progressive overload, compound lifts, and a calorie surplus.";
        } else {
            plan = "Maintain balanced nutrition and a mix of strength + cardio 3–4x/week.";
        }

        String summary = String.format(
                "Name: %s%n" +
                        "Height: %.1f in%n" +
                        "Weight: %.1f lb%n" +
                        "Age: %d%n" +
                        "Goal: %s%n%n" +
                        "BMI: %.1f%n" +
                        "Estimated daily calories: %.0f kcal%n%n" +
                        "Plan:%n%s",
                nameText, heightIn, weightLb, age, goal, bmi, calories, plan
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