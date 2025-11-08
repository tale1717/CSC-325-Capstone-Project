package com.example.imagedemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Stack;

public class MyImgInvisibleExmp extends Application{

    @Override
    public void start(Stage primaryStage){
        //Load the image
        Image image = new Image("gym-design-floor-plan.png");
        ImageView imageView = new ImageView(image);

        //Create label
        Label hiddenLabel = new Label("This is a hidden label");

        //Initially set label to be invisible and unmanaged
        hiddenLabel.setVisible(false);
        hiddenLabel.setManaged(false);

        //Use stackPane to layer the image and the label
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, hiddenLabel);

        //Create scene and stage
        Scene scene = new Scene(stackPane, 400, 300);
        primaryStage.setTitle("Invisible Label Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Make it appear later (via button click, event handler = eventually
        // connect this to database to laod gym equipment status on image
        // with labes showing green as vacant or red as not vacant)

        //showButton.setOnAction(event -> {
        // hiddenLabel.setVisible(true);
        // hiddenLabel.setManaged(true);
        // });
    }

    public static void main(String[] args){
        launch(args);
    }
}
