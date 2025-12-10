package com.example.firebaseadminjavafx.logic;

import com.example.firebaseadminjavafx.models.WorkoutProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationEngine {

    // WorkoutProfile builder from raw strings
    public static WorkoutProfile buildProfile(String userId,
                                              String userEmail,
                                              String name,
                                              double heightIn,
                                              double weightLb,
                                              int age,
                                              String rawGoalText) {

        WorkoutProfile.GoalType goalType = normalizeGoal(rawGoalText);

        return new WorkoutProfile(
                userId,
                userEmail,
                name,
                heightIn,
                weightLb,
                age,
                goalType,
                rawGoalText
        );
    }

    private static WorkoutProfile.GoalType normalizeGoal(String goalText) {
        if (goalText == null) {
            return WorkoutProfile.GoalType.MAINTAIN;
        }

        String lower = goalText.toLowerCase();

        if (lower.contains("lose")) {
            return WorkoutProfile.GoalType.LOSE_WEIGHT;
        }
        if (lower.contains("gain")) {
            return WorkoutProfile.GoalType.GAIN_MUSCLE;
        }
        if (lower.contains("maintain")) {
            return WorkoutProfile.GoalType.MAINTAIN;
        }

        return WorkoutProfile.GoalType.MAINTAIN;
    }

    // Suggest machines by map labels (T1, E2, B1, O1, C1, A1, W1, etc.)
    public static List<String> suggestMachines(WorkoutProfile profile) {
        if (profile == null) {
            return Collections.emptyList();
        }

        List<String> machines = new ArrayList<>();

        switch (profile.getGoalType()) {
            case LOSE_WEIGHT:
                machines.add("T1");
                machines.add("T2");
                machines.add("E1");
                machines.add("E2");
                machines.add("E3");
                machines.add("C1");  // cable / full body
                break;

            case GAIN_MUSCLE:
                machines.add("B1");
                machines.add("B2");
                machines.add("P1");  // preacher curl row on map
                machines.add("O1");  // olympic bench on map
                machines.add("C1");
                machines.add("W1");  // weight assist dip/chin
                machines.add("A1");
                machines.add("A2");
                break;

            case MAINTAIN:
            default:
                machines.add("T1");
                machines.add("E2");
                machines.add("B1");
                machines.add("O1");
                machines.add("C1");
                machines.add("A1");
                machines.add("W1");
                break;
        }

        return machines;
    }

    // Keywords to match gym classes against
    public static List<String> suggestClassKeywords(WorkoutProfile profile) {
        if (profile == null) {
            return Collections.emptyList();
        }

        List<String> tags = new ArrayList<>();

        switch (profile.getGoalType()) {
            case LOSE_WEIGHT:
                tags.add("cardio");
                tags.add("HIIT");
                tags.add("cycling");
                tags.add("bootcamp");
                break;

            case GAIN_MUSCLE:
                tags.add("strength");
                tags.add("lifting");
                tags.add("power");
                tags.add("conditioning");
                break;

            case MAINTAIN:
            default:
                tags.add("full body");
                tags.add("conditioning");
                tags.add("circuit");
                tags.add("mobility");
                break;
        }

        return tags;
    }

    public static String machinesSummary(List<String> machines) {
        if (machines == null || machines.isEmpty()) {
            return "No specific machines recommended.";
        }
        return String.join(", ", machines);
    }
}
