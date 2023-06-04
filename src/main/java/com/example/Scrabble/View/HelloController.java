package com.example.Scrabble.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onJoinGameButtonClick() {
        welcomeText.setText("Welcome to Scrabble!");
    }

    @FXML
    protected void onHostGameButtonClick() {
        welcomeText.setText("Welcome to Scrabble! (Host)");
    }
}