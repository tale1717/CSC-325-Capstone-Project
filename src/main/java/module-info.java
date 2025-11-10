module com.example.firebaseadminjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires firebase.admin;          // firebase-admin JAR
    requires com.google.auth;
    requires com.google.auth.oauth2;  // google-auth-library-oauth2-http
    requires google.cloud.firestore;

    opens com.example.firebaseadminjavafx to javafx.fxml;
    exports com.example.firebaseadminjavafx;
}