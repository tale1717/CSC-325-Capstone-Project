public class GoalPlanner {
    public String getWorkoutPlan(String goal) {
        switch (goal.toLowerCase()) {
            case "lose weight":
                return "Focus on cardio and light weights.";
            case "gain muscle":
                return "Focus on heavy lifting and protein intake.";
            case "maintain":
                return "Do a balanced mix of cardio and strength.";
            default:
                return "Set a clear goal first.";
        }
    }
}
