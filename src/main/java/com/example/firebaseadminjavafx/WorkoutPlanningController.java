package com.example.firebaseadminjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class WorkoutPlanningController {

    @FXML private VBox root;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ageField;
    @FXML private TextField heightField;   // cm
    @FXML private TextField weightField;   // lbs
    @FXML private ComboBox<String> goalBox;
    @FXML private TextArea resultArea;
    @FXML private Label statusLabel;
    @FXML private Button seePlanButton;
    @FXML private Button calorieButton;
    @FXML private Button backButton;

    private final CalorieCalculator calorieCalculator = new CalorieCalculator();
    private final GoalPlanner goalPlanner = new GoalPlanner();

    @FXML
    private void initialize() {
        System.out.println("WorkoutPlanningController initialized");
        goalBox.getItems().setAll("Lose Weight", "Gain Muscle", "Maintain");
        goalBox.getSelectionModel().selectFirst();
        statusLabel.setText("");
        resultArea.setText("");
    }

    @FXML
    private void handleSeeMyPlan() {
        statusLabel.setText("");

        try {
            String firstName = firstNameField.getText().trim();
            String lastName  = lastNameField.getText().trim();
            int age          = Integer.parseInt(ageField.getText().trim());
            double heightCm  = Double.parseDouble(heightField.getText().trim());
            double weightLbs = Double.parseDouble(weightField.getText().trim());
            String goal      = goalBox.getSelectionModel().getSelectedItem();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                statusLabel.setText("Please enter your first and last name.");
                return;
            }

            double weightKg = weightLbs * 0.453592;
            User user = new User(age, heightCm, weightKg, goal);

            String planText = buildPlanText(user, firstName, lastName, weightLbs);
            resultArea.setText(planText);

        } catch (NumberFormatException ex) {
            statusLabel.setText("Please enter valid numbers for age, height, and weight.");
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Something went wrong. See console.");
        }
    }

    @FXML
    private void handleCalorieIntake() {
        statusLabel.setText("");

        try {
            int age          = Integer.parseInt(ageField.getText().trim());
            double heightCm  = Double.parseDouble(heightField.getText().trim());
            double weightLbs = Double.parseDouble(weightField.getText().trim());
            String goal      = goalBox.getSelectionModel().getSelectedItem();

            double weightKg  = weightLbs * 0.453592;
            User user = new User(age, heightCm, weightKg, goal);

            double calories = calorieCalculator.calculateCalories(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Calorie Intake");
            alert.setHeaderText("Estimated Daily Calorie Intake");
            alert.setContentText(String.format("Approximately %.0f kcal per day.", calories));
            alert.showAndWait();

        } catch (NumberFormatException ex) {
            statusLabel.setText("Please enter valid numbers before calculating calories.");
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Failed to calculate calories. See console.");
        }
    }

    @FXML
    private void handleBack() {
        Main.setRoot("gymapp-home.fxml", backButton);
    }

    private String buildPlanText(User user,
                                 String firstName,
                                 String lastName,
                                 double weightLbs) {

        int age          = user.getAge();
        double heightCm  = user.getHeight();
        double weightKg  = user.getWeight();
        String goal      = user.getGoal();

        double heightInFeet = heightCm / 30.48;
        int feetPart = (int) heightInFeet;
        double inchesPart = (heightInFeet - feetPart) * 12;

        double bmi = weightKg / Math.pow(heightCm / 100.0, 2);

        String plan;
        String tip;
        String workouts;

        String goalLower = goal.toLowerCase();
        if (goalLower.contains("lose")) {
            plan = "Focus on calorie deficit and cardio 4–5x/week.";
            tip = "Try eating more whole foods and tracking your steps.";
            workouts = "Cardio (30 mins)\nStretching\nCycling";
        } else if (goalLower.contains("gain")) {
            plan = "Eat in a calorie surplus and lift weights 3–4x/week.";
            tip = "Increase protein intake and focus on compound lifts.";
            workouts = "Weight Training\nHigh Protein Meals\nProper Rest";
        } else {
            plan = "Maintain a balanced diet and exercise 3x/week.";
            tip = "Stay consistent and get enough sleep.";
            workouts = "Walking\nYoga\nLight Strength Training";
        }

        String workoutPlan = goalPlanner.getWorkoutPlan(goal);

        return "--- Personalized Workout Plan for "
                + firstName + " " + lastName + " ---\n\n"
                + "Age: " + age + "\n"
                + "Height: " + String.format("%.1f", heightCm) + " cm ("
                + feetPart + " ft " + String.format("%.1f", inchesPart) + " in)\n"
                + "Weight: " + String.format("%.1f", weightLbs) + " lbs ("
                + String.format("%.1f", weightKg) + " kg)\n"
                + "Goal: " + goal + "\n\n"
                + String.format("Your BMI: %.1f\n\n", bmi)
                + "Calories intake per day (approx): "
                + String.format("%.0f kcal\n\n", calorieCalculator.calculateCalories(user))
                + "Workout plan summary:\n" + workoutPlan
                + "\n\nRecommended Plan:\n" + plan
                + "\n\nHealth Tip:\n" + tip
                + "\n\nWorkout Suggestions:\n" + workouts;
    }
}
