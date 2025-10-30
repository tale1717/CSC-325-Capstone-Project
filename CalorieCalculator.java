public class CalorieCalculator {
    public double calculateCalories(User user) {
        double bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161;
        //change later
        return bmr * 1.2;
    }
}
