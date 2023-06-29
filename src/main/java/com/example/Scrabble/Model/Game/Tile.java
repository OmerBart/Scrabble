package com.example.Scrabble.Model.Game;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * The Tile class represents a tile used in the game of Scrabble.
 * It contains information about the letter and score associated with the tile.
 */
public class Tile {
    final char letter;
    final int score;

    /**
     * Constructs a Tile object with the specified letter and score.
     *
     * @param letter The letter associated with the tile.
     * @param score  The score associated with the tile.
     */
    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     * Returns the string representation of the tile, which is the letter.
     *
     * @return The string representation of the tile.
     */
    @Override
    public String toString() {
        return String.valueOf(letter);
    }

    /**
     * Checks if the current Tile object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    /**
     * Returns the hash code value for the tile.
     *
     * @return The hash code value for the tile.
     */
    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    /**
     * The Bag class represents a bag of tiles used in the game of Scrabble.
     * It manages the availability and distribution of tiles.
     */
    public static class Bag {
        int[] tileCount;
        Tile[] tileArray;

        private static Bag single_instance = null;

        /**
         * Constructs a Bag object and initializes the tile counts and tile array.
         * Private constructor to enforce singleton pattern.
         */
        private Bag() {
            int[] tileScores = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
            this.tileCount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            this.tileArray = new Tile[26];
            for (int i = 0; i < 26; i++) {
                this.tileArray[i] = new Tile((char) ('A' + i), tileScores[i]);
            }
        }

        /**
         * Returns the singleton instance of the Bag class.
         *
         * @return The singleton instance of the Bag class.
         */
        public static Bag getBag() {
            if (single_instance == null)
                single_instance = new Bag();
            return single_instance;
        }

        /**
         * Checks if the bag is empty, i.e., all tile types have been exhausted.
         *
         * @return true if the bag is empty, false otherwise.
         */
        private boolean isEmpty() {
            for (int count : tileCount) {
                if (count > 0) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if adding the specified tile would exceed the maximum allowed count for that tile.
         *
         * @param tile The tile to check.
         * @return true if adding the tile exceeds the maximum count, false otherwise.
         */
        private boolean maxTile(Tile tile) {
            int[] legalTileCount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            return this.tileCount[tile.letter - 'A'] >= legalTileCount[tile.letter - 'A'];
        }

        /**
         * Checks if the specified letter is within the legal range.
         *
         * @param letter The letter to check.
         * @return true if the letter is within the legal range, false otherwise.
         */
        private boolean legalLetter(char letter) {
            return letter >= 'A' && letter <= 'Z';
        }

        /**
         * Retrieves a random tile from the bag, removing it from the available tiles.
         *
         * @return The randomly selected tile, or null if there are no available tiles.
         */
        public Tile getRand() {
            Random r = new Random();
            int t;

            while (!isEmpty()) {
                t = r.nextInt(26);
                if (tileCount[t] > 0) {
                    tileCount[t]--;
                    return tileArray[t];
                }
            }

            return null;
        }

        /**
         * Retrieves a specific tile from the bag, removing it from the available tiles.
         *
         * @param letter The letter of the tile to retrieve.
         * @return The specified tile if it is available, or null otherwise.
         */
        public Tile getTile(char letter) {
            if (!legalLetter(letter))
                return null;
            if (tileCount[letter - 'A'] > 0) {
                tileCount[letter - 'A']--;
                return tileArray[letter - 'A'];
            } else
                return null;
        }

        /**
         * Adds a tile back into the bag, if it does not exceed the maximum allowed count.
         *
         * @param tile The tile to add back into the bag.
         */
        public void put(Tile tile) {
            if (!maxTile(tile))
                this.tileCount[tile.letter - 'A']++;
        }

        /**
         * Returns a copy of the current tile count array.
         *
         * @return A copy of the current tile count array.
         */
        public int[] getQuantities() {
            int[] tmp = new int[26];
            System.arraycopy(this.tileCount, 0, tmp, 0, this.tileCount.length);
            return tmp;
        }
    }

    /**
     * Returns the letter associated with the tile.
     *
     * @return The letter associated with the tile.
     */
    public char getLetter() {
        return letter;
    }
}
