public class CalorieCalculator {
    public double calculateCalories(User user) {
        double bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161;
        String goal = user.getGoal().toLowerCase();
        if (goal.contains("lose")) {
            bmr -= 500; // calorie deficit
        } else if (goal.contains("gain")) {
            bmr += 500; // calorie surplus
        }

        return bmr * 1.2;
    }
}
