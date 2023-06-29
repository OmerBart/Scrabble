package com.example.Scrabble.Model.Game;

import java.util.Arrays;

/**
 * The Word class represents a word formed in the game of Scrabble.
 * It contains information about the tiles used, the starting row and column coordinates,
 * and whether the word is vertical or horizontal.
 */
public class Word {

    /**
     * An array of Tile objects representing the tiles used to form the word.
     */
    private Tile[] tiles;

    /**
     * The row index of the starting position of the word on the game board.
     */
    private int row;

    /**
     * The column index of the starting position of the word on the game board.
     */
    private int col;

    /**
     * Indicates whether the word is vertical (true) or horizontal (false).
     */
    private boolean vertical;

    /**
     * Constructs a Word object with the specified tiles, starting row and column indices, and orientation.
     *
     * @param tiles    An array of Tile objects representing the tiles used to form the word.
     * @param row      The row index of the starting position of the word on the game board.
     * @param col      The column index of the starting position of the word on the game board.
     * @param vertical Indicates whether the word is vertical (true) or horizontal (false).
     */
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    /**
     * Prints the word by iterating over the tiles and displaying their string representation.
     */
    public void printWord() {
        System.out.println();
        for (Tile tile : this.tiles) {
            System.out.print(tile);
        }
    }

    /**
     * Retrieves the array of Tile objects representing the tiles used to form the word.
     *
     * @return An array of Tile objects representing the tiles used in the word.
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * Retrieves the row index of the starting position of the word on the game board.
     *
     * @return The row index of the starting position of the word.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column index of the starting position of the word on the game board.
     *
     * @return The column index of the starting position of the word.
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if the word is vertical.
     *
     * @return true if the word is vertical, false if it is horizontal.
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Compares the current Word object with another object for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }

    /**
     * Returns a string representation of the word, with all tiles concatenated together.
     *
     * @return The string representation of the word.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Tile tile : tiles) {
            sb.append(isFirst ? tile : tile.toString().toLowerCase());
            isFirst = false;
        }
        return sb.toString();
    }
}
