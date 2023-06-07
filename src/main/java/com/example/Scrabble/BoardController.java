package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    @FXML
    private GridPane board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardBuild();
    }

    public void boardBuild() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Rectangle rect = new Rectangle(40, 40);
                rect.getStyleClass().add("board-tile");
                board.add(rect, i, j);
            }
        }
    }

}
