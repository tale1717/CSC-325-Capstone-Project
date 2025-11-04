package com.example.CSC-325-Capstone-Project;

import java.io.IOException;
import java.awt.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GymLayout extends Application {
    @Override
    public void start(Stage stage) throws IOException {
       //FXMLLoader fxmlLoader = new FXMLLoader(GymLayout.class.getResource("GymController.fxml"));
        Parent root =
                FXMLLoader.load(getClass().getResource("GymController.fxml"));
      //Scene scene = new Scene(fxmlLoader.load(), 320, 400);
        Scene scene = new Scene(root, 320, 400);
        stage.setTitle("Gym Layout App");
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}


//public class GymLayout {



   /*     public void start(Stage stage) throws Exception {
            Parent root =
                    FXMLLoader.load(getClass().getResource("GymController.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Gym Machine Status");
            stage.setScene(scene);
            stage.show();
        }

         */

//Items for GymLayoutController.java
    /*private String treadmills;
    private String bikemachines;
    private String smithmachines;

    //Click to find out status of each machine
            //Treadmills, BikeMachines, Smithmachines

    //If user clicks each equipment, they will be shown which machine is available or not in a small GUI based on logic below


    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_RESET = "\u001B[0m";


    public static void main(String[] args){
        boolean isMachineAvailable = checkMachineAvailability("Treadmill 1");

        if (isMachineAvailable) {
            System.out.println(ANSI_GREEN + "Treadmill 1 is available!" + ANSI_RESET);
        }
        else {
            System.out.println(ANSI_RED + "Treadmill 1 is currently occupied." + ANSI_RESET);
        }
    }

    public static boolean checkMachineAvailability(String machineName) {

        return Math.random() > 0.5;
    }

} */
