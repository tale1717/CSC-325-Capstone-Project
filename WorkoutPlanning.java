public class WorkoutPlanning {
    public static void main(String[] args) {
        // example user
        User user = new User(25, 175, 70, "Lose Weight");

        CalorieCalculator calculator = new CalorieCalculator();
        GoalPlanner planner = new GoalPlanner();

        double calories = calculator.calculateCalories(user);
        String workout = planner.getWorkoutPlan(user.getGoal());


        System.out.println("\n--- Personalized Workout Plan ---");
        System.out.println("Age: " + age);
        System.out.println("Height: " + String.format("%.1f", heightCm) + " cm (" + feetPart + " ft " + String.format("%.1f", inchesPart) + " in)");
        System.out.println("Weight: " + weightPounds + " lbs (" + String.format("%.1f", weightKg) + " kg)");
        System.out.println("Goal: " + user.getGoal());
        System.out.println("Calories intake per day: " + String.format("%.1f", calories));
        System.out.println("Workout plan: " + workout);

        scanner.close();
    }
}
