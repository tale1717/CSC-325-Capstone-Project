package com.example.firebaseadminjavafx;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public final class FirebaseService {
    private static boolean initialized = false;

    public static synchronized void initialize() {
        if (initialized) return;
        try {
            // Use your service-account key. You said it is named key.json in project root.
            FileInputStream serviceAccount = new FileInputStream("key.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;
            System.out.println("Firebase Admin SDK initialized.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
        }
    }

    private FirebaseService() {}
}