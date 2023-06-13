package com.example.Scrabble;

import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    private String letter;
    private int score;
    private int row;
    private int col;
    public boolean selected = false;
    private Rectangle rect;
    private Label label;

    public Tile() {
        Random r = new Random();
        String letter = String.valueOf((char) (r.nextInt(26) + 'A'));
        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40, 80);
        rect.getStyleClass().add("tile");

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(rect, label);
        this.letter = letter;
        this.label = label;
        this.score = 0;
        this.row = 0;
        this.col = 0;
        this.rect = rect;
    }

    public String getLetter() {
        return this.letter;
    }

    public int getScore() {
        return this.score;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public void setCol(int c) {
        this.col = c;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public Rectangle getRect() {
        return this.rect;
    }

    public Label getLabel() {
        return this.label;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
        this.getChildren().clear();
        this.getChildren().addAll(this.rect, this.label);
    }

    public void setLabel(Label label) {
        this.label = label;
        this.getChildren().clear();
        this.getChildren().addAll(this.rect, this.label);
    }
}
