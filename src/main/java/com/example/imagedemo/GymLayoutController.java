package com.example.imagedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GymLayoutController {
    @FXML
    private void returnBackToHome(ActionEvent event){
        try{
            //load GYM Layout FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gymlayouthome.fxml"));
            Parent root = loader.load();

            //Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Scene content replacement
            stage.setScene(new Scene(root));
            stage.setTitle("Gym Home");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
