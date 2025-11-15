package com.example.firebaseadminjavafx;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class Main extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static String currentUserUid;
    public static String currentUserEmail;

    @Override
    public void start(Stage stage) throws Exception {
        FirebaseService.initialize();
        FirestoreContext.init();

        fstore = FirestoreContext.fstore;
        fauth = FirestoreContext.fauth;
        currentUserUid = FirestoreContext.currentUserUid;
        currentUserEmail = FirestoreContext.currentUserEmail;

        FXMLLoader fxmlLoader =
                new FXMLLoader(Main.class.getResource("/com/example/firebaseadminjavafx/welcome-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("GymApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String viewName, Control anyControlInScene) {
        try {
            String resourcePath;
            if (viewName.endsWith(".fxml")) {
                resourcePath = "/com/example/firebaseadminjavafx/" + viewName;
            } else {
                resourcePath = "/com/example/firebaseadminjavafx/"
                        + viewName.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase()
                        + ".fxml";
            }

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resourcePath));
            Parent newRoot = loader.load();
            anyControlInScene.getScene().setRoot(newRoot);
        } catch (Exception e) {
            throw new RuntimeException("Failed to switch to view: " + viewName, e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}