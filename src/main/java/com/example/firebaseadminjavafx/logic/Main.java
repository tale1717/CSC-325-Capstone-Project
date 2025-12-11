package com.example.firebaseadminjavafx.logic;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;

    public static String currentUserUid;
    public static String currentUserEmail;

    private static Stage mainStage;

    private static final Logger log = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        initFirebase();

        try {
            Parent root = loadRoot("welcome-view.fxml");
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setTitle("GymApp");
            mainStage.sizeToScene();
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load initial view.", e);
        }
    }

    private void initFirebase() {
        if (fstore != null) return;

        try {
            InputStream serviceAccount =
                    Main.class.getResourceAsStream("/key.json");

            if (serviceAccount == null) {
                log.severe("key.json not found on classpath.");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("gymapp-e38b5.appspot.com")
                    .build();

            FirebaseApp app = FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(options)
                    : FirebaseApp.getInstance();

            fstore = FirestoreClient.getFirestore(app);
            fauth = FirebaseAuth.getInstance(app);

            log.info("Firebase initialized successfully.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to initialize Firebase.", e);
        }
    }

    private static Parent loadRoot(String fxmlName) throws IOException {
        if (!fxmlName.endsWith(".fxml")) {
            fxmlName = fxmlName + ".fxml";
        }

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/com/example/firebaseadminjavafx/" + fxmlName)
        );
        return loader.load();
    }

    public static void setRoot(String fxmlName) {
        try {
            Parent root = loadRoot(fxmlName);

            if (mainStage == null) {
                log.severe("mainStage is null.");
                return;
            }

            if (mainStage.getScene() == null) {
                mainStage.setScene(new Scene(root));
            } else {
                mainStage.getScene().setRoot(root);
            }

            mainStage.sizeToScene();
            mainStage.centerOnScreen();

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to switch to view: " + fxmlName, e);
        }
    }

    public static void setRoot(String fxmlName, Control anyControlInScene) {
        try {
            Parent root = loadRoot(fxmlName);

            if (anyControlInScene != null &&
                    anyControlInScene.getScene() != null) {

                anyControlInScene.getScene().setRoot(root);

                if (mainStage != null) {
                    mainStage.sizeToScene();
                    mainStage.centerOnScreen();
                }
            } else {
                setRoot(fxmlName);
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to switch to view: " + fxmlName, e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
