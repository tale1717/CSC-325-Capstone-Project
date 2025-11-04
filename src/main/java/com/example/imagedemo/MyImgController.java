package com.example.imagedemo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.InputStream;

public class MyImgController {
    @FXML
    private ImageView myImageView;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}