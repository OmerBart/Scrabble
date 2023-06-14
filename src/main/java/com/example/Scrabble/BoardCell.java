package com.example.Scrabble;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class BoardCell extends StackPane {
    int row;
    int col;
    String letter;
    int score;
    boolean isBonus;
    boolean isStar;
    public boolean isOccupied;
    Rectangle rect;
    Label label;
    boolean sequence = false;

    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.letter = "";
        this.score = 0;
        this.isBonus = false;
        this.isStar = false;
        this.isOccupied = false;

        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40, 40);
        if (row == 0 && col == 0) {
            rect.getStyleClass().add("hide");
        } else {
            rect.getStyleClass().add("board-cell");
        }
        this.getChildren().addAll(rect, label);
        this.setAlignment(Pos.CENTER);

        this.label = label;
        this.rect = rect;
    }

    public BoardCell(String letter, int r, int c) {
        this.row = r;
        this.col = c;
        this.letter = "";
        this.score = 0;
        this.isBonus = false;
        this.isStar = false;
        this.isOccupied = false;

        Label label = new Label("");
        Rectangle rect = new Rectangle(40, 40);
        switch (letter) {
            case "star":
                label = new Label("★");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("star");
                this.label = label;
                this.rect = rect;
                this.isStar = true;
                break;
            case "2L":
                label = new Label("2L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2L");
                this.label = label;
                this.rect = rect;
                break;

            case "3L":
                label = new Label("3L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3L");
                this.label = label;
                this.rect = rect;
                break;

            case "2W":
                label = new Label("2W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2W");
                this.label = label;
                this.rect = rect;
                break;

            case "3W":
                label = new Label("3W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3W");
                this.label = label;
                this.rect = rect;
                break;

            default:
                label = new Label(letter);
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("board-cell");
                this.label = label;
                this.rect = rect;
                break;
        }
        this.getChildren().addAll(rect, label);
        this.setAlignment(Pos.CENTER);
    }

    public void setLetter(String letter) {
        this.letter = letter;
        this.label.setText(letter);
    }

    public Label getLabel() {
        return this.getChildren().get(1) instanceof Label ? (Label) this.getChildren().get(1) : null;
    }

    public void setLabel(Label label) {
        this.label = label;
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    public String getLetter() {
        return this.letter;
    }

    public Rectangle getRect() {
        return this.getChildren().get(0) instanceof Rectangle ? (Rectangle) this.getChildren().get(0) : null;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    @Override
    public String toString() {
        return "col=" + col + ", row=" + row + "isOccupied=" + isOccupied;
    }
}
