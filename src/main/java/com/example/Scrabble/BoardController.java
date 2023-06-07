package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
        boardBordersBuild();
    }

    public void boardBuild() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                Rectangle rect = new Rectangle(40, 40);
                rect.styleProperty().set(
                        "-fx-fill: 'green'; -fx-stroke: #000000; -fx-stroke-width: 1; -fx-arc-width: 5; -fx-arc-height: 5; -fx-alignment: center;");
                board.add(rect, i, j);
            }
        }
    }

    public void boardBordersBuild() {
        board.getChildren().get(0).styleProperty().set("-fx-display: 'none';");
        for (int i = 1; i < 16; i++) {
            String letter = String.valueOf((char) (i + 64));
            Label label = new Label(letter);
            label.styleProperty()
                    .set("-fx-font-size: 20px; -fx-text-fill: 'black'; -fx-margin-left:5px;");
            board.add(label, i, 0);
            board.getChildren().get(i).styleProperty().set(
                    "-fx-fill: 'white'; -fx-stroke: #000000; -fx-stroke-width: 1; -fx-arc-width: 5; -fx-arc-height: 5;");
        }
        for (int i = 1; i < 16; i++) {
            Label label = new Label(String.valueOf(i));
            label.setRotate(270);
            label.styleProperty()
                    .set("-fx-font-size: 20px; -fx-text-fill: 'black'; -fx-margin-left:105px;");
            board.add(label, 0, i);
            board.getChildren().get(i * 16).styleProperty().set(
                    "-fx-fill: 'white'; -fx-stroke: #000000; -fx-stroke-width: 1; -fx-arc-width: 5; -fx-arc-height: 5;");
        }
    }
}
