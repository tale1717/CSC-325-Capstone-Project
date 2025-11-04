import java.util.Scanner;

public class WorkoutPlanning {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.print("Enter your height (in cm): ");
        double heightCm = scanner.nextDouble();

        System.out.print("Enter your weight (in pounds): ");
        double weightPounds = scanner.nextDouble();

        scanner.nextLine(); // consume newline left-over
        System.out.print("Enter your goal (e.g., Lose Weight / Gain Muscle / Maintain): ");
        String goal = scanner.nextLine();

        // Convert height & weight to metric units
        double heightInFeet = heightCm / 30.48; // convert cm → feet
        int feetPart = (int) heightInFeet; // extract whole feet
        double inchesPart = (heightInFeet - feetPart) * 12;
        double weightKg = weightPounds * 0.453592;

        User user = new User(age, heightCm, weightKg, goal);

        CalorieCalculator calculator = new CalorieCalculator();
        GoalPlanner planner = new GoalPlanner();

        double calories = calculator.calculateCalories(user);
        String workout = planner.getWorkoutPlan(user.getGoal());


        System.out.println("\n--- Personalized Workout Plan for " + firstName + " " + lastName + " ---");
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
