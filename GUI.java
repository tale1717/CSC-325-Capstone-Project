import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    // Login / Input page
    static class InputPage extends JFrame implements ActionListener {
        private JTextField nameField, heightField, weightField, ageField;
        private JComboBox<String> goalBox;
        private JButton submitButton, calorieButton;

        public InputPage() {
            setTitle("Fitness Planner - Step 1");
            setSize(350, 340);
            setLayout(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JLabel title = new JLabel("Enter Your Info", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            title.setBounds(60, 10, 220, 30);
            add(title);

            // Name
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setBounds(40, 60, 100, 25);
            add(nameLabel);

            nameField = new JTextField();
            nameField.setBounds(150, 60, 120, 25);
            add(nameField);

            // Height
            JLabel heightLabel = new JLabel("Height (cm):");
            heightLabel.setBounds(40, 90, 100, 25);
            add(heightLabel);

            heightField = new JTextField();
            heightField.setBounds(150, 90, 120, 25);
            add(heightField);

            // Weight
            JLabel weightLabel = new JLabel("Weight (kg):");
            weightLabel.setBounds(40, 120, 100, 25);
            add(weightLabel);

            weightField = new JTextField();
            weightField.setBounds(150, 120, 120, 25);
            add(weightField);

            // Age
            JLabel ageLabel = new JLabel("Age:");
            ageLabel.setBounds(40, 150, 100, 25);
            add(ageLabel);

            ageField = new JTextField();
            ageField.setBounds(150, 150, 120, 25);
            add(ageField);

            // Goal
            JLabel goalLabel = new JLabel("Goal:");
            goalLabel.setBounds(40, 180, 100, 25);
            add(goalLabel);

            String[] goals = {"Lose Weight", "Gain Muscle", "Maintain"};
            goalBox = new JComboBox<>(goals);
            goalBox.setBounds(150, 180, 120, 25);
            add(goalBox);

            // Buttons
            submitButton = new JButton("See My Plan");
            submitButton.setBounds(100, 220, 130, 30);
            submitButton.addActionListener(this);
            add(submitButton);

            calorieButton = new JButton("Calorie Intake");
            calorieButton.setBounds(100, 260, 130, 30);
            calorieButton.addActionListener(e -> calculateCalories());
            add(calorieButton);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText();
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());
                int age = Integer.parseInt(ageField.getText());
                String goal = (String) goalBox.getSelectedItem();

                dispose();
                new ResultPage(name, height, weight, age, goal);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void calculateCalories() {
            try {
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());
                int age = Integer.parseInt(ageField.getText());
                String goal = (String) goalBox.getSelectedItem();

                double bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
                double calories = bmr * 1.55;

                // Adjust based on goal
                if (goal.equals("Lose Weight")) calories -= 400;
                else if (goal.equals("Gain Muscle")) calories += 400;

                JOptionPane.showMessageDialog(this,
                        String.format("Estimated Daily Calorie Intake: %.0f kcal", calories),
                        "Calorie Intake",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers before calculating.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Result page
    static class ResultPage extends JFrame {
        public ResultPage(String name, double height, double weight, int age, String goal) {
            setTitle("Fitness Planner - Results");
            setSize(400, 380);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            double bmi = weight / Math.pow(height / 100.0, 2);
            String plan;
            String tip;
            String workouts;

            if (goal.equals("Lose Weight")) {
                plan = "Focus on calorie deficit and cardio 4–5x/week.";
                tip = "Try eating more whole foods and tracking your steps.";
                workouts = "Cardio (30 mins)\nStretching\nCycling";
            } else if (goal.equals("Gain Muscle")) {
                plan = "Eat in a calorie surplus and lift weights 3–4x/week.";
                tip = "Increase protein intake and focus on compound lifts.";
                workouts = "Weight Training\nHigh Protein Meals\nProper Rest";
            } else {
                plan = "Maintain a balanced diet and exercise 3x/week.";
                tip = "Stay consistent and get enough sleep.";
                workouts = "Walking\nYoga\nLight Strength Training";
            }

            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            resultArea.setText(
                    "Name: " + name + "\n\n" +
                            "Height: " + height + " cm\n" +
                            "Weight: " + weight + " kg\n" +
                            "Age: " + age + "\n\n" +
                            String.format("Your BMI: %.1f\n\n", bmi) +
                            "Recommended Plan:\n" + plan +
                            "\n\nHealth Tip:\n" + tip +
                            "\n\nWorkout Suggestions:\n" + workouts
            );

            add(new JScrollPane(resultArea), BorderLayout.CENTER);

            // Back Button
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                dispose();
                new InputPage();
            });
            add(backButton, BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new InputPage();
    }
}
