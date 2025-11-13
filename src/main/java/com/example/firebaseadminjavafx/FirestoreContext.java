package com.example.firebaseadminjavafx;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

public final class FirestoreContext {
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static String currentUserUid;
    public static String currentUserEmail;

    private FirestoreContext() {}

    public static void init() {
        if (fstore != null && fauth != null) return;
        if (FirebaseApp.getApps().isEmpty()) {
            throw new IllegalStateException("Firebase not initialized. Call FirebaseService.initialize() first.");
        }
        fstore = FirestoreClient.getFirestore();
        fauth = FirebaseAuth.getInstance();
    }
}