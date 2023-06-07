package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class BoardController implements Initializable {

    @FXML
    private GridPane board;

    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeText.setText("Welcome to Scrabble!");
        welcomeText.getStyleClass().add("welcome-text");
        boardBuild();
    }

    public void boardBuild() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                Rectangle rect = new Rectangle(40, 40);
                StackPane stack = new StackPane(rect, new Label(""));
                stack.setAlignment(Pos.CENTER);
                rect.getStyleClass().add("board-cell");
                board.add(rect, i, j);
            }
        }
        boardBordersBuild();
        drawStar();
    }

    public void boardBordersBuild() {
        board.getChildren().get(0).getStyleClass().clear();
        board.getChildren().get(0).getStyleClass().add("hide");
        for (int i = 1; i < 16; i++) {
            String letter = String.valueOf((char) (i + 64));
            Label label = new Label(letter);
            Rectangle rect = new Rectangle(40, 40);
            rect.getStyleClass().add("board-border");
            StackPane stack = new StackPane(rect, label);
            stack.setAlignment(Pos.CENTER);
            stack.getStyleClass().add("board-border");
            board.add(stack, i, 0);
            board.getChildren().get(i).getStyleClass().clear();
            board.getChildren().get(i).getStyleClass().add("board-border");
        }
        for (int i = 1; i < 16; i++) {
            Label label = new Label(String.valueOf(i));
            label.setRotate(270);
            Rectangle rect = new Rectangle(40, 40);
            rect.getStyleClass().add("board-border");
            StackPane stack = new StackPane(rect, label);
            stack.setAlignment(Pos.CENTER);
            board.add(stack, 0, i);
        }
    }

    private void drawStar() {
        Label label = new Label("★");
        Rectangle rect = new Rectangle(40, 40);
        rect.getStyleClass().add("star");
        StackPane stack = new StackPane(rect, label);
        stack.setAlignment(Pos.CENTER);
        board.add(stack, 8, 8);
    }
}