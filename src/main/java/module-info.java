module com.example.firebaseadminjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires firebase.admin;          // firebase-admin JAR
    requires com.google.auth;
    requires com.google.auth.oauth2;  // google-auth-library-oauth2-http
    requires google.cloud.firestore;

    requires com.google.api.apicommon; // com.google.api.core.ApiFuture
    requires google.cloud.core;

    requires java.logging; // adds updates to intellij logs

    opens com.example.firebaseadminjavafx to javafx.fxml;
    exports com.example.firebaseadminjavafx;
}