package com.example.firebaseadminjavafx.models;

public class WorkoutProfile {

    public enum GoalType {
        LOSE_WEIGHT,
        GAIN_MUSCLE,
        MAINTAIN
    }

    private String userId;
    private String userEmail;
    private String name;
    private double heightIn;
    private double weightLb;
    private int age;
    private GoalType goalType;
    private String originalGoalText;

    public WorkoutProfile(String userId,
                          String userEmail,
                          String name,
                          double heightIn,
                          double weightLb,
                          int age,
                          GoalType goalType,
                          String originalGoalText) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.name = name;
        this.heightIn = heightIn;
        this.weightLb = weightLb;
        this.age = age;
        this.goalType = goalType;
        this.originalGoalText = originalGoalText;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public double getHeightIn() {
        return heightIn;
    }

    public double getWeightLb() {
        return weightLb;
    }

    public int getAge() {
        return age;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public String getOriginalGoalText() {
        return originalGoalText;
    }
}
