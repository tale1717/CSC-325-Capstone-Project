package com.example.imagedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class MyImgControllerTest {
    @FXML
    private ImageView myImageView;
    @FXML
    private Label labelBench;
    @FXML
    private Label labelTreadmill;

    @FXML
    public void initialize() {
        try {
            InputStream stream = getClass().getResourceAsStream("gym-design-floor-plan.png");
            if (stream != null) {
                Image image = new Image(stream);
                myImageView.setImage(image);
            } else {
                System.err.println("Image not found");
            }

            labelBench.setVisible(true);
            labelBench.setManaged(true);
            labelTreadmill.setVisible(true);
            labelTreadmill.setManaged(true);

            labelBench.setTranslateX(50);
            labelBench.setTranslateY(-80);

            labelTreadmill.setTranslateX(-160);
            labelTreadmill.setTranslateY(-25);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Example toggle methods - triggered alter
    public void showLabels() {
        labelBench.setVisible(true);
        labelBench.setManaged(true);
        labelTreadmill.setVisible(true);
        labelTreadmill.setManaged(true);
    }

    public void hideLabels() {
        labelBench.setVisible(false);
        labelBench.setManaged(false);
        labelTreadmill.setVisible(false);
        labelTreadmill.setManaged(false);
    }
}