package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

// import com.example.Scrabble.BoardCanvas;

public class ScrabbleController {
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