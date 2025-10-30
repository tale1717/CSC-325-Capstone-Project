public class WorkoutPlanning {
    public static void main(String[] args) {
        // example user
        User user = new User(25, 175, 70, "Lose Weight");

        CalorieCalculator calculator = new CalorieCalculator();
        GoalPlanner planner = new GoalPlanner();

        double calories = calculator.calculateCalories(user);
        String workout = planner.getWorkoutPlan(user.getGoal());

        System.out.println("Goal: " + user.getGoal());
        System.out.println("Calories intake per day: " + calories);
        System.out.println("Workout plan: " + workout);
    }
}
