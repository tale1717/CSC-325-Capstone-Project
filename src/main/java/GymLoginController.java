package com.example.gymloginpage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Button signUpButton;

    @FXML
    private Label orLabel;

    @FXML
    private Button googleButton;

    @FXML
    private Label termsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        if (email != null && !email.trim().isEmpty()) {
            // Validate email format
            if (isValidEmail(email)) {
                System.out.println("Signing up with email: " + email);
                // Add your signup logic here
            } else {
                System.out.println("Invalid email format");
                // Show error message to user
            }
        } else {
            System.out.println("Please enter an email address");
            // Show error message to user
        }
    }

    private void handleGoogleSignIn() {
        System.out.println("Google sign in clicked");
        // Add Google OAuth integration here
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}