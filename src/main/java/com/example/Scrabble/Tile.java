package com.example.Scrabble;

import java.util.HashMap;
import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Tile extends StackPane {
    private String letter;
    private int score;
    private int row;
    private int col;
    public boolean selected = false;
    private Rectangle rect;
    private Label label;
    public static final HashMap<String, Integer> scores = new HashMap<String, Integer>() {
        {
            put("A", 1);
            put("B", 3);
            put("C", 3);
            put("D", 2);
            put("E", 1);
            put("F", 4);
            put("G", 2);
            put("H", 4);
            put("I", 1);
            put("J", 8);
            put("K", 5);
            put("L", 1);
            put("M", 3);
            put("N", 1);
            put("O", 1);
            put("P", 3);
            put("Q", 10);
            put("R", 1);
            put("S", 1);
            put("T", 1);
            put("U", 1);
            put("V", 4);
            put("W", 4);
            put("X", 8);
            put("Y", 4);
            put("Z", 10);
        }
    };

    public Tile() {
        Random r = new Random();
        String letter = String.valueOf((char) (r.nextInt(26) + 'A'));
        Label label = new Label(letter);
        Rectangle rect = new Rectangle(60, 80);
        VBox scoreAndLetter = new VBox(10);
        scoreAndLetter.setAlignment(Pos.CENTER);
        this.score = scores.get(letter);
        this.letter = letter;
        this.label = label;
        this.row = 0;
        this.col = 0;
        this.rect = rect;

        scoreAndLetter.getChildren().addAll(label, new Text("score: " + this.score));
        rect.getStyleClass().add("tile");
        this.getChildren().addAll(rect, scoreAndLetter);
        this.setAlignment(Pos.CENTER);

    }

    public Tile(String letter) {
        Label label = new Label(letter);
        Rectangle rect = new Rectangle(60, 80);
        VBox scoreAndLetter = new VBox(10);
        scoreAndLetter.setAlignment(Pos.CENTER);
        this.letter = letter;
        this.label = label;
        this.score = scores.get(letter);
        this.row = 0;
        this.col = 0;
        this.rect = rect;

        rect.getStyleClass().add("tile");
        scoreAndLetter.getChildren().addAll(label, new Text("score: " + this.score));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(rect, scoreAndLetter);
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
