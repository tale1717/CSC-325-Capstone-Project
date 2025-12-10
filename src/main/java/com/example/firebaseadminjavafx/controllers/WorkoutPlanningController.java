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
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.time.LocalDate;
import com.example.firebaseadminjavafx.logic.SuggestedMachineStore;



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

        // Feed list into global suggested machine store for Gym Layout highlighting
        SuggestedMachineStore.clear();
        for (String m : machineSuggestions) {

            // Convert visible text → machine code used in GymLayout
            switch (m) {
                case "T1 Treadmills":
                case "T1/T2 Treadmills":
                    SuggestedMachineStore.add("T1");
                    SuggestedMachineStore.add("T2");
                    break;

                case "T1":
                    SuggestedMachineStore.add("T1");
                    break;

                case "T2":
                    SuggestedMachineStore.add("T2");
                    break;

                case "E1/E2/E3 Ellipticals":
                    SuggestedMachineStore.add("E1");
                    SuggestedMachineStore.add("E2");
                    SuggestedMachineStore.add("E3");
                    break;

                case "Cable machine C1":
                case "C1 Cable Machine":
                    SuggestedMachineStore.add("C1");
                    break;

                case "W1 Weight Assist Dip/Chin":
                    SuggestedMachineStore.add("W1");
                    break;

                case "P1 Preacher Curl":
                    SuggestedMachineStore.add("P1");
                    break;

                case "OB1 Olympic Bench":
                    SuggestedMachineStore.add("OB1");
                    break;

                case "OI1 Olympic Incline":
                    SuggestedMachineStore.add("OI1");
                    break;

                case "AD1 Adductor":
                    SuggestedMachineStore.add("AD1");
                    break;

                case "AB1 Abductor":
                    SuggestedMachineStore.add("AB1");
                    break;

                case "PC1 Chest Press":
                case "Chest Press (PC1)":
                    SuggestedMachineStore.add("PC1");
                    break;
            }
        }


        String progressSuggestions = buildProgressSuggestions(goalLower, sessionsPerWeekVal, focusList);
        if (!progressSuggestions.isBlank()) {
            planBuilder.append("\nProgress-based suggestions:\n");
            planBuilder.append(progressSuggestions).append("\n");
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

    private String buildProgressSuggestions(String goalLower, int targetSessions, List<String> focusList) {
        if (Main.currentUserUid == null || Main.currentUserUid.isEmpty()) {
            return "";
        }

        try {
            CollectionReference col = Main.fstore
                    .collection("users")
                    .document(Main.currentUserUid)
                    .collection("weeklyProgress");

            ApiFuture<QuerySnapshot> future =
                    col.orderBy("weekStart").limitToLast(4).get();

            QuerySnapshot snapshot = future.get();
            List<QueryDocumentSnapshot> docs = snapshot.getDocuments();

            if (docs.size() < 2) {
                return "";
            }

            double firstWeight = Double.NaN;
            double lastWeight = Double.NaN;
            double sumVisits = 0.0;
            double sumMinutes = 0.0;
            double sumMachines = 0.0;
            int count = 0;

            for (QueryDocumentSnapshot doc : docs) {
                Double w = doc.getDouble("weight");
                Long visits = doc.getLong("gymVisits");
                Long minutes = doc.getLong("totalMinutesAtGym");
                Long machines = doc.getLong("machineSessions");

                if (w == null || visits == null || minutes == null) {
                    continue;
                }

                if (Double.isNaN(firstWeight)) {
                    firstWeight = w;
                }
                lastWeight = w;

                sumVisits += visits;
                sumMinutes += minutes;
                if (machines != null) {
                    sumMachines += machines;
                }
                count++;
            }

            if (count < 2 || Double.isNaN(firstWeight) || Double.isNaN(lastWeight)) {
                return "";
            }

            double avgVisits = sumVisits / count;
            double avgMinutes = sumMinutes / count;
            double avgMachines = sumMachines / Math.max(1, count);
            double weightDelta = lastWeight - firstWeight;

            boolean wantLoss = goalLower.contains("lose");
            boolean wantGain = goalLower.contains("gain") || goalLower.contains("strength");
            boolean wantRecomp = goalLower.contains("recomposition");
            boolean wantEndurance = goalLower.contains("endurance");

            StringBuilder sb = new StringBuilder();

            // consistency vs target sessions
            if (avgVisits < Math.max(1, targetSessions - 1)) {
                sb.append("- You are averaging about ")
                        .append(String.format("%.1f", avgVisits))
                        .append(" gym visits per week, but your plan targets ")
                        .append(targetSessions)
                        .append(". Consider lowering your sessions per week to 2–3 and focusing on consistency.\n");
            } else if (avgVisits >= targetSessions + 1) {
                sb.append("- You are consistently going to the gym more often than your plan expects (about ")
                        .append(String.format("%.1f", avgVisits))
                        .append(" visits/week). If workouts feel easy, you could increase intensity or move up an experience level.\n");
            }

            // weight trend vs goal – loss / recomp
            if (wantLoss || wantRecomp) {
                if (weightDelta > -0.5 && weightDelta < 0.5) {
                    sb.append("- Your weight has been fairly stable over recent weeks. If fat loss is the goal, try tightening nutrition or adding a bit more cardio time.\n");
                } else if (weightDelta < -3.0) {
                    sb.append("- You have lost weight fairly quickly over the last few weeks. Make sure you are recovering well and consider slightly increasing calories if you feel overly fatigued.\n");
                }
            }

            // weight trend vs goal – gain / strength
            if (wantGain || (wantRecomp && !wantLoss)) {
                if (weightDelta < 0.5) {
                    sb.append("- Your weight is not increasing much over recent weeks. For muscle/strength gain, you may need a small calorie surplus or one more strength-focused session per week.\n");
                } else if (weightDelta > 3.0) {
                    sb.append("- You are gaining weight fairly quickly. If you feel sluggish, consider slightly reducing calories or adding a bit more cardio while keeping strength work heavy.\n");
                }
            }

            // endurance goal vs minutes
            if (wantEndurance) {
                if (avgMinutes < 60.0) {
                    sb.append("- Your total weekly minutes at the gym are relatively low for an endurance goal. Try building toward at least 60–90 minutes of cardio per week.\n");
                }
            }

            // focus vs goal alignment
            List<String> lowerFocus = new ArrayList<>();
            for (String f : focusList) {
                if (f != null) {
                    lowerFocus.add(f.toLowerCase(Locale.ROOT));
                }
            }

            boolean hasCardioFocus = lowerFocus.stream()
                    .anyMatch(f -> f.contains("cardio") || f.contains("endurance"));
            boolean hasStrengthFocus = lowerFocus.stream()
                    .anyMatch(f -> f.contains("upper body")
                            || f.contains("lower body")
                            || f.contains("full body")
                            || f.contains("core"));

            // cardio mismatch / reinforcement
            if ((wantLoss || wantRecomp || wantEndurance) && !hasCardioFocus) {
                sb.append("- Your goal involves fat loss or endurance, but your selected focus areas do not include Cardio/Endurance. Consider adding Cardio/Endurance so your classes match your goal better.\n");
            } else if ((wantLoss || wantRecomp || wantEndurance)
                    && hasCardioFocus
                    && weightDelta > -0.5 && weightDelta < 0.5
                    && avgMinutes < 60.0) {
                sb.append("- You are choosing some Cardio/Endurance focus, but your recent progress is still limited. Try increasing total weekly cardio minutes or choosing more cardio-focused classes.\n");
            }

            // strength mismatch: goal is gain but focus is mostly cardio
            if (wantGain && hasCardioFocus && !hasStrengthFocus) {
                sb.append("- Your goal is muscle/strength gain, but most of your focus areas are cardio-based. Consider adding more strength-focused areas like Upper Body, Lower Body, or Full Body.\n");
            }

            // NEW: data-backed strength reinforcement
            if ((wantGain || wantRecomp) && hasStrengthFocus) {
                if (avgMachines < 3.0) {
                    sb.append("- You selected strength-focused areas, but you are averaging relatively few machine sessions per week. Try adding another strength day or a few extra sets to make those sessions more effective.\n");
                } else if (avgMachines >= 5.0 && weightDelta < 0.5) {
                    sb.append("- You are doing a solid number of machine sessions each week, but your weight is not moving much. Consider slightly increasing calories or moving up in weight on key lifts.\n");
                }
            }

            String result = sb.toString().trim();
            return result.isEmpty() ? "" : result;

        } catch (Exception e) {
            log.info("Progress suggestions failed: " + e.getMessage());
            return "";
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
