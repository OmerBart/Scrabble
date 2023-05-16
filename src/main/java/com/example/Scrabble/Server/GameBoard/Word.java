package com.example.Scrabble.Server.GameBoard;

import java.util.Arrays;

public class Word {

    Tile[] tiles;
    int row, col;
    boolean vertical;


    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }


    public void printWord() {
        System.out.println();
        for (Tile t : this.getTiles()){
            System.out.print(t);
        }

    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }


}
