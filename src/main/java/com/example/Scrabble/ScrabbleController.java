package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

public class ScrabbleController {
    @FXML
    private Label welcomeText;

    @FXML   
    private Canvas canvas;

    @FXML
    protected void onJoinGameButtonClick() {
        welcomeText.setText("Welcome to Scrabble!");
    }

    @FXML
    protected void onHostGameButtonClick() {
        welcomeText.setText("Welcome to Scrabble! (Host)");
    }

    
}