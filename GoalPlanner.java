public class GoalPlanner {

    public String getWorkoutPlan(String goal) {
        goal = goal.toLowerCase();

        if (goal.contains("lose")) {
            return "Focus on cardio and light weights.";
        } else if (goal.contains("gain")) {
            return "Do strength training and eat a protein-rich diet.";
        } else if (goal.contains("maintain")) {
            return "Balance cardio with moderate weight training.";
        } else {
            return "General fitness: mix of cardio, weights, and flexibility.";
        }
    }
}
