import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    static class UserData {
        String name;
        double height;
        double weight;
        int age;
        String goal;

        public UserData(String name, double height, double weight, int age, String goal) {
            this.name = name;
            this.height = height;
            this.weight = weight;
            this.age = age;
            this.goal = goal;
        }
    }

    static class InputPage extends JFrame implements ActionListener {

        private JTextField nameField, heightField, weightField, ageField;
        private JComboBox<String> goalBox;
        private JButton submitButton, calorieButton;
        private UserData user;

        public InputPage() {
            this(null);
        }

        public InputPage(UserData savedUser) {
            this.user = savedUser;

            setTitle("Fitness Planner - Step 1");
            setSize(350, 360);
            setLayout(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JLabel title = new JLabel("Enter Your Info", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            title.setBounds(60, 10, 220, 30);
            add(title);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setBounds(40, 60, 100, 25);
            add(nameLabel);

            nameField = new JTextField();
            nameField.setBounds(150, 60, 120, 25);
            add(nameField);

            JLabel heightLabel = new JLabel("Height (cm):");
            heightLabel.setBounds(40, 90, 100, 25);
            add(heightLabel);

            heightField = new JTextField();
            heightField.setBounds(150, 90, 120, 25);
            add(heightField);

            JLabel weightLabel = new JLabel("Weight (kg):");
            weightLabel.setBounds(40, 120, 100, 25);
            add(weightLabel);

            weightField = new JTextField();
            weightField.setBounds(150, 120, 120, 25);
            add(weightField);

            JLabel ageLabel = new JLabel("Age:");
            ageLabel.setBounds(40, 150, 100, 25);
            add(ageLabel);

            ageField = new JTextField();
            ageField.setBounds(150, 150, 120, 25);
            add(ageField);

            JLabel goalLabel = new JLabel("Goal:");
            goalLabel.setBounds(40, 180, 100, 25);
            add(goalLabel);

            String[] goals = {"Lose Weight", "Gain Muscle", "Maintain"};
            goalBox = new JComboBox<>(goals);
            goalBox.setBounds(150, 180, 120, 25);
            add(goalBox);

            submitButton = new JButton("See My Plan");
            submitButton.setBounds(100, 220, 130, 30);
            submitButton.addActionListener(this);
            add(submitButton);

            calorieButton = new JButton("Calorie Intake");
            calorieButton.setBounds(100, 260, 130, 30);
            calorieButton.addActionListener(e -> openCaloriePage());
            add(calorieButton);

            if (user != null) {
                nameField.setText(user.name);
                heightField.setText(String.valueOf(user.height));
                weightField.setText(String.valueOf(user.weight));
                ageField.setText(String.valueOf(user.age));
                goalBox.setSelectedItem(user.goal);
            }

            setVisible(true);
        }

        private UserData readUserInput() {
            return new UserData(
                    nameField.getText(),
                    Double.parseDouble(heightField.getText()),
                    Double.parseDouble(weightField.getText()),
                    Integer.parseInt(ageField.getText()),
                    (String) goalBox.getSelectedItem()
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                user = readUserInput();
                setVisible(false);
                new ResultPage(user, this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            }
        }

        private void openCaloriePage() {
            try {
                user = readUserInput();
                setVisible(false);
                new CaloriePage(user, this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            }
        }
    }

    static class ResultPage extends JFrame {
        public ResultPage(UserData user, InputPage inputPage) {
            setTitle("Fitness Planner - Results");
            setSize(400, 380);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            double bmi = user.weight / Math.pow(user.height / 100.0, 2);
            String plan;
            String tip;
            String workouts;

            if (user.goal.equals("Lose Weight")) {
                plan = "Calorie deficit and cardio 4–5x/week.";
                tip = "Eat more whole foods and track steps.";
                workouts = "Cardio (30 mins)\nStretching\nCycling";
            } else if (user.goal.equals("Gain Muscle")) {
                plan = "Calorie surplus and lift 3–4x/week.";
                tip = "Increase protein and focus on compound lifts.";
                workouts = "Weight Training\nHigh Protein Meals\nRest";
            } else {
                plan = "Balanced diet and exercise 3x/week.";
                tip = "Consistency and sleep matter.";
                workouts = "Walking\nYoga\nLight Strength Training";
            }

            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            resultArea.setText(
                    "Name: " + user.name + "\n\n" +
                            "Height: " + user.height + " cm\n" +
                            "Weight: " + user.weight + " kg\n" +
                            "Age: " + user.age + "\n\n" +
                            String.format("Your BMI: %.1f\n\n", bmi) +
                            "Recommended Plan:\n" + plan +
                            "\n\nHealth Tip:\n" + tip +
                            "\n\nWorkout Suggestions:\n" + workouts
            );

            add(new JScrollPane(resultArea), BorderLayout.CENTER);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                setVisible(false);
                inputPage.setVisible(true);
            });
            add(backButton, BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    static class CaloriePage extends JFrame {
        public CaloriePage(UserData user, InputPage inputPage) {
            setTitle("Calorie Intake");
            setSize(300, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            double bmr = (10 * user.weight) + (6.25 * user.height) - (5 * user.age) + 5;
            double calories = bmr * 1.55;

            if (user.goal.equals("Lose Weight")) calories -= 400;
            if (user.goal.equals("Gain Muscle")) calories += 400;

            JLabel label = new JLabel("Daily Calories: " + String.format("%.0f", calories), SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);

            JButton back = new JButton("Back");
            back.addActionListener(e -> {
                setVisible(false);
                inputPage.setVisible(true);
            });
            add(back, BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new InputPage();
    }
}
