package com.example.gymloginpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GymLoginController implements Initializable {

    @FXML
    private VBox mainContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label orLabel;

    @FXML
    private Button googleButton;

    @FXML
    private Label termsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Firebase
        FirebaseService.initializeFirebase();
        setupUI();
    }

    private void setupUI() {
        // Set up the terms and conditions text
        String termsText = "By clicking continue, you agree to our Terms of Service and Privacy Policy";
        termsLabel.setText(termsText);
        termsLabel.setWrapText(true);

        // Add event handlers for buttons
        signUpButton.setOnAction(event -> handleSignUp());
        googleButton.setOnAction(event -> handleGoogleSignIn());
    }

    private void handleSignUp() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email != null && !email.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            // Validate email format
            if (isValidEmail(email)) {
                // Validate password
                if (isValidPassword(password)) {
                    System.out.println("Signing up with email: " + email);

                    // Create user in Firebase
                    String userId = FirebaseService.createUserWithEmail(email, password);
                    if (userId != null) {
                        System.out.println("User created successfully with ID: " + userId);
                        // Show success message to user
                        showSuccessMessage("Account created successfully!");
                        clearFields();
                    } else {
                        System.out.println("Failed to create user");
                        // Show error message to user
                        showErrorMessage("Failed to create account. Please try again.");
                    }
                } else {
                    System.out.println("Password must be at least 6 characters");
                    showErrorMessage("Password must be at least 6 characters long.");
                }
            } else {
                System.out.println("Invalid email format");
                showErrorMessage("Please enter a valid email address.");
            }
        } else {
            System.out.println("Please enter both email and password");
            showErrorMessage("Please enter both email and password.");
        }
    }

    private void handleGoogleSignIn() {
        System.out.println("Google sign in clicked");
        // Add Google OAuth integration here
        showInfoMessage("Google sign-in feature coming soon!");
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPassword(String password) {
        // Firebase requires at least 6 characters
        return password.length() >= 6;
    }

    private void showSuccessMessage(String message) {
        // You can implement a proper dialog or status label here
        System.out.println("SUCCESS: " + message);
        // For now, we'll just print to console
        // In a real application, you might want to show an alert or update a status label
    }

    private void showErrorMessage(String message) {
        // You can implement a proper dialog or status label here
        System.out.println("ERROR: " + message);
        // For now, we'll just print to console
        // In a real application, you might want to show an alert or update a status label
    }

    private void showInfoMessage(String message) {
        // You can implement a proper dialog or status label here
        System.out.println("INFO: " + message);
        // For now, we'll just print to console
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
        emailField.requestFocus();
    }
}