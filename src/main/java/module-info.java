module com.example.firebaseadminjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires firebase.admin;              // Firebase Admin JAR
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;      // <-- no "com."
    requires com.google.api.apicommon;
    requires google.cloud.core;           // <-- no "com."
    requires java.logging;

    // --- opens (for reflection access)
    opens com.example.firebaseadminjavafx to javafx.fxml;
    opens com.example.firebaseadminjavafx.controllers to javafx.fxml, google.cloud.firestore;
    opens com.example.firebaseadminjavafx.firebase    to javafx.fxml, google.cloud.firestore;
    opens com.example.firebaseadminjavafx.logic       to javafx.fxml, google.cloud.firestore;
    opens com.example.firebaseadminjavafx.models      to javafx.fxml, google.cloud.firestore;

    // --- exports (for access by other modules)
    exports com.example.firebaseadminjavafx;
    exports com.example.firebaseadminjavafx.controllers;
    exports com.example.firebaseadminjavafx.firebase;
    exports com.example.firebaseadminjavafx.logic;
    exports com.example.firebaseadminjavafx.models;
}
