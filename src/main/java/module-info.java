module com.example.firebaseadminjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires firebase.admin;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires com.google.api.apicommon;
    requires google.cloud.core;

    requires java.logging;
    requires java.desktop;
    requires org.checkerframework.checker.qual;

    opens com.example.firebaseadminjavafx.controllers to javafx.fxml;
    opens com.example.firebaseadminjavafx.logic to com.google.gson, google.cloud.firestore, javafx.fxml;
    opens com.example.firebaseadminjavafx.models to com.google.gson, google.cloud.firestore;

    exports com.example.firebaseadminjavafx.logic;
    exports com.example.firebaseadminjavafx.controllers;
    exports com.example.firebaseadminjavafx.models;
}
