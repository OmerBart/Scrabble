package com.example.Scrabble;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    ArrayList<StackPane> tilesList = new ArrayList<>();

    @FXML
    private GridPane board;

    @FXML
    private HBox tiles;

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
                newCellBuilder(i, j);
            }
        }
        boardBordersBuild();
        drawStar();
        drawBonus();
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

    public void drawBonus() {
        newCellBuilder("3W", 1, 1);
        newCellBuilder("3W", 1, 8);
        newCellBuilder("3W", 1, 15);
        newCellBuilder("3W", 8, 1);
        newCellBuilder("3W", 8, 15);
        newCellBuilder("3W", 15, 1);
        newCellBuilder("3W", 15, 8);
        newCellBuilder("3W", 15, 15);

        newCellBuilder("2W", 2, 2);
        newCellBuilder("2W", 3, 3);
        newCellBuilder("2W", 4, 4);
        newCellBuilder("2W", 5, 5);
        newCellBuilder("2W", 11, 11);
        newCellBuilder("2W", 12, 12);
        newCellBuilder("2W", 13, 13);
        newCellBuilder("2W", 14, 14);
        newCellBuilder("2W", 2, 14);
        newCellBuilder("2W", 3, 13);
        newCellBuilder("2W", 4, 12);
        newCellBuilder("2W", 5, 11);
        newCellBuilder("2W", 11, 5);
        newCellBuilder("2W", 12, 4);
        newCellBuilder("2W", 13, 3);
        newCellBuilder("2W", 14, 2);

        newCellBuilder("2L", 1, 4);
        newCellBuilder("2L", 4, 1);
        newCellBuilder("2L", 1, 12);
        newCellBuilder("2L", 12, 1);
        newCellBuilder("2L", 3, 7);
        newCellBuilder("2L", 7, 3);
        newCellBuilder("2L", 4, 8);
        newCellBuilder("2L", 8, 4);
        newCellBuilder("2L", 9, 3);
        newCellBuilder("2L", 3, 9);
        newCellBuilder("2L", 15, 4);
        newCellBuilder("2L", 4, 15);
        newCellBuilder("2L", 7, 7);
        newCellBuilder("2L", 7, 9);
        newCellBuilder("2L", 9, 7);
        newCellBuilder("2L", 9, 9);
        newCellBuilder("2L", 12, 8);
        newCellBuilder("2L", 8, 12);
        newCellBuilder("2L", 7, 13);
        newCellBuilder("2L", 13, 7);
        newCellBuilder("2L", 9, 13);
        newCellBuilder("2L", 13, 9);
        newCellBuilder("2L", 4, 15);
        newCellBuilder("2L", 15, 4);
        newCellBuilder("2L", 12, 15);
        newCellBuilder("2L", 15, 12);

        newCellBuilder("3L", 2, 6);
        newCellBuilder("3L", 6, 2);
        newCellBuilder("3L", 2, 10);
        newCellBuilder("3L", 10, 2);
        newCellBuilder("3L", 6, 6);
        newCellBuilder("3L", 6, 10);
        newCellBuilder("3L", 10, 6);
        newCellBuilder("3L", 10, 10);
        newCellBuilder("3L", 6, 14);
        newCellBuilder("3L", 14, 6);
        newCellBuilder("3L", 10, 14);
        newCellBuilder("3L", 14, 10);
    }

    private void newCellBuilder(int r, int c) {
        Label label = new Label("");
        Rectangle rect = new Rectangle(40, 40);
        if (r == 0 && c == 0) {
            rect.getStyleClass().add("hide");
        } else {
            rect.getStyleClass().add("board-cell");
        }
        StackPane stack = new StackPane(rect, label);
        stack.setAlignment(Pos.CENTER);
        board.add(stack, c, r);
    }

    private void newCellBuilder(String bonus, int r, int c) {
        Label label = new Label("");
        Rectangle rect = new Rectangle(40, 40);
        switch (bonus) {
            case "2L":
                label = new Label("2L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2L");
                break;

            case "3L":
                label = new Label("3L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3L");
                break;

            case "2W":
                label = new Label("2W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2W");
                break;

            case "3W":
                label = new Label("3W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3W");
                break;

            default:
                break;
        }
        StackPane stack = new StackPane(rect, label);
        stack.setAlignment(Pos.CENTER);
        board.add(stack, c, r);
    }

    public void getTile() {
        StackPane tile = buildTile();
        tiles.getChildren().add(tile);
    }

    private StackPane buildTile() {
        // random letter A-Z
        Random r = new Random();
        String letter = String.valueOf((char) (r.nextInt(26) + 'A'));
        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40, 80);
        rect.getStyleClass().add("tile");
        StackPane stack = new StackPane(rect, label);
        stack.setAlignment(Pos.CENTER);
        return stack;
    }
}