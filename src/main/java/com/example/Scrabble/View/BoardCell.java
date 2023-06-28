package com.example.Scrabble.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class BoardCell extends StackPane {
    public int row;
    public int col;
    public String letter;
    public int score;
    public String bonus;
    public boolean isStar;
    public boolean isOccupied;
    public Rectangle rect;
    public Label label;
    public boolean sequence = false;

    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.letter = "";
        this.score = 0;
        this.bonus = "none";
        this.isStar = false;
        this.isOccupied = false;

        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40,40);
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
        this.letter = letter;
        this.score = 0;
        this.bonus = "none";
        this.isStar = false;
        this.isOccupied = false;

        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40,40);
        switch (letter) {
            case "1":
                label = new Label("");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("board-cell");
                this.label = label;
                this.rect = rect;
                this.bonus = "none";
                break;

            case "6":
                label = new Label("★");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("star");
                this.label = label;
                this.rect = rect;
                this.isStar = true;
                this.bonus = "star";
                break;
            case "2":
                label = new Label("2L");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("bonus-2L");
                this.label = label;
                this.rect = rect;
                this.bonus = "2L";
                break;

            case "3":
                label = new Label("3L");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("bonus-3L");
                this.label = label;
                this.rect = rect;
                this.bonus = "3L";
                break;

            case "4":
                label = new Label("2W");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("bonus-2W");
                this.label = label;
                this.rect = rect;
                this.bonus = "2W";
                break;

            case "5":
                label = new Label("3W");
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("bonus-3W");
                this.label = label;
                this.rect = rect;
                this.bonus = "3W";
                break;

            default:
                label = new Label(letter);
                rect = new Rectangle(40,40);
                rect.getStyleClass().add("board-cell-occupied");
                this.label = label;
                this.rect = rect;
                this.isOccupied = true;
                break;
        }
        this.getChildren().addAll(rect, label);
        this.setAlignment(Pos.CENTER);
    }

    public void setLetter(String letter) {
        this.letter = letter;
        this.label.setText(letter);
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
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

    public void changeStyle(String style) {
        this.rect.getStyleClass().clear();
        this.rect.getStyleClass().add(style);
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    public static <T> Integer getRow(T boardCell) {
        return boardCell.getClass().equals(BoardCell.class) ? ((BoardCell) boardCell).row : null;
    }

    public static <T> Integer getCol(T boardCell) {
        return boardCell.getClass().equals(BoardCell.class) ? ((BoardCell) boardCell).col : null;
    }

    @Override
    public String toString() {
        return "col=" + col + ", row=" + row + "bonus= " + bonus;
    }

    public void setDefaultStyle() {
        switch (this.bonus) {
            case "2L":
                System.out.println("2L");
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("bonus-2L");
                this.setLabel(new Label("2L"));
                break;
            case "3L":
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("bonus-3L");
                this.setLabel(new Label("3L"));
                break;
            case "2W":
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("bonus-2W");
                this.setLabel(new Label("2W"));
                break;
            case "3W":
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("bonus-3W");
                this.setLabel(new Label("3W"));
                break;
            case "star":
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("star");
                this.setLabel(new Label("★"));
                break;
            case "none":
                this.rect.getStyleClass().clear();
                this.rect.getStyleClass().add("board-cell");
                this.setLabel(new Label(""));
                break;
            default:
                break;
        }
    }
}
