package com.example.Scrabble.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * The {@code BoardCell} class represents a cell on the Scrabble board.
 * It extends the {@link javafx.scene.layout.StackPane} class and contains properties
 * and methods to manage the cell's state and appearance.
 */
public class BoardCell extends StackPane {

    /**
     * The row index of the cell on the board.
     */
    public int row;

    /**
     * The column index of the cell on the board.
     */
    public int col;

    /**
     * The letter placed on the cell.
     */
    public String letter;

    /**
     * The score value of the letter placed on the cell.
     */
    public int score;

    /**
     * The bonus type of the cell.
     */
    public String bonus;

    /**
     * Indicates whether the cell is a star cell.
     */
    public boolean isStar;

    /**
     * Indicates whether the cell is occupied by a letter.
     */
    public boolean isOccupied;

    /**
     * The rectangular shape representing the cell.
     */
    public Rectangle rect;

    /**
     * The label displaying the letter or bonus on the cell.
     */
    public Label label;

    /**
     * Indicates whether the cell is part of a sequence.
     */
    public boolean sequence = false;

    /**
     * Constructs a new {@code BoardCell} object with the specified row and column indices.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.letter = "";
        this.score = 0;
        this.bonus = "none";
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

    /**
     * Constructs a new {@code BoardCell} object with the specified letter, row, and column indices.
     *
     * @param letter the letter placed on the cell
     * @param r      the row index of the cell
     * @param c      the column index of the cell
     */
    public BoardCell(String letter, int r, int c) {
        this.row = r;
        this.col = c;
        this.letter = letter;
        this.score = 0;
        this.bonus = "none";
        this.isStar = false;
        this.isOccupied = false;

        Label label = new Label(letter);
        Rectangle rect = new Rectangle(40, 40);
        switch (letter) {
            case "1":
                label = new Label("");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("board-cell");
                this.label = label;
                this.rect = rect;
                this.bonus = "none";
                break;
            case "6":
                label = new Label("★");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("star");
                this.label = label;
                this.rect = rect;
                this.isStar = true;
                this.bonus = "star";
                break;
            case "2":
                label = new Label("2L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2L");
                this.label = label;
                this.rect = rect;
                this.bonus = "2L";
                break;
            case "3":
                label = new Label("3L");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3L");
                this.label = label;
                this.rect = rect;
                this.bonus = "3L";
                break;
            case "4":
                label = new Label("2W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-2W");
                this.label = label;
                this.rect = rect;
                this.bonus = "2W";
                break;
            case "5":
                label = new Label("3W");
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("bonus-3W");
                this.label = label;
                this.rect = rect;
                this.bonus = "3W";
                break;
            default:
                label = new Label(letter);
                rect = new Rectangle(40, 40);
                rect.getStyleClass().add("board-cell-occupied");
                this.label = label;
                this.rect = rect;
                this.isOccupied = true;
                break;
        }
        this.getChildren().addAll(rect, label);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Sets the letter on the cell and updates the label.
     *
     * @param letter the letter to be set on the cell
     */
    public void setLetter(String letter) {
        this.letter = letter;
        this.label.setText(letter);
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    /**
     * Returns the label associated with the cell.
     *
     * @return the label associated with the cell, or {@code null} if no label is found
     */
    public Label getLabel() {
        return this.getChildren().get(1) instanceof Label ? (Label) this.getChildren().get(1) : null;
    }

    /**
     * Sets the label associated with the cell.
     *
     * @param label the label to be set on the cell
     */
    public void setLabel(Label label) {
        this.label = label;
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    /**
     * Returns the letter on the cell.
     *
     * @return the letter on the cell
     */
    public String getLetter() {
        return this.letter;
    }

    /**
     * Returns the rectangular shape representing the cell.
     *
     * @return the rectangular shape representing the cell, or {@code null} if no shape is found
     */
    public Rectangle getRect() {
        return this.getChildren().get(0) instanceof Rectangle ? (Rectangle) this.getChildren().get(0) : null;
    }

    /**
     * Sets the rectangular shape representing the cell.
     *
     * @param rect the rectangular shape to be set on the cell
     */
    public void setRect(Rectangle rect) {
        this.rect = rect;
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    /**
     * Changes the style of the cell by updating its style class and refreshing its appearance.
     *
     * @param style the new style class for the cell
     */
    public void changeStyle(String style) {
        this.rect.getStyleClass().clear();
        this.rect.getStyleClass().add(style);
        this.getChildren().clear();
        this.getChildren().addAll(rect, label);
    }

    /**
     * Returns the row index of the specified board cell.
     *
     * @param boardCell the board cell
     * @param <T>       the type of the board cell
     * @return the row index of the board cell, or {@code null} if the board cell is not of type {@code BoardCell}
     */
    public static <T> Integer getRow(T boardCell) {
        return boardCell.getClass().equals(BoardCell.class) ? ((BoardCell) boardCell).row : null;
    }

    /**
     * Returns the column index of the specified board cell.
     *
     * @param boardCell the board cell
     * @param <T>       the type of the board cell
     * @return the column index of the board cell, or {@code null} if the board cell is not of type {@code BoardCell}
     */
    public static <T> Integer getCol(T boardCell) {
        return boardCell.getClass().equals(BoardCell.class) ? ((BoardCell) boardCell).col : null;
    }

    /**
     * Returns a string representation of the board cell.
     *
     * @return a string representation of the board cell
     */
    @Override
    public String toString() {
        return "col=" + col + ", row=" + row + "bonus= " + bonus;
    }

    /**
     * Sets the default style of the cell based on its bonus type.
     * This method updates the style class and label of the cell to match the bonus type.
     */
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
