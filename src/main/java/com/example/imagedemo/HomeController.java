package com.example.imagedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class HomeController {

    @FXML
    private void handleGymLayoutClick(ActionEvent event){
        try{
            //load GYM Layout FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view-test.fxml"));
            Parent root = loader.load();

            //Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Scene content replacement
            stage.setScene(new Scene(root));
            stage.setTitle("Gym Layout Map Display");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminClick(ActionEvent event) throws IOException {
        try {
            //load Admin Layout FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Admin View of Gym Equip");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
