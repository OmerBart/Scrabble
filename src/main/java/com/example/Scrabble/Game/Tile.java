package com.example.Scrabble.Game;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
public class Tile {
    final char letter;
    final int score;


    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    @Override
    public String toString() {
        return "|" + letter + "|" + score; // changed from " |" + letter + score + "| "
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }
    // A 1, B 3, C 3, D 2, E 1, F 4, G 2, H 4, I 1, J 8, K 5, L 1, M 3, N 1, O 1, P 3, Q 10, R 1, S 1, T 1, U 1, V 4, W 4, X 8, Y 4, Z 10
//    {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10}
    public static class Bag {
        int[] tileCount;
        Tile[] tileArray;

        private static Bag single_instance = null;

        @Override
        public String toString() {
            return "Bag{" +
                    "tileCount=" + Arrays.toString(tileCount) +
                    ", tileArray=" + Arrays.toString(tileArray) +
                    '}';
        }

        private Bag() {
            int[] tileScores = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
            this.tileCount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            this.tileArray = new Tile[26];
            //int i = 0;
            for(int i = 0; i<26;i++)
                this.tileArray[i] = new Tile((char) ('A'+i),tileScores[i]);
        }

        public static Bag getBag(){
            if (single_instance == null)
                single_instance = new Bag();
            return single_instance;
        }
        private boolean isEmpty() {
            // Check if all tile types have been exhausted (count = 0)
            for (int count : tileCount) {
                if (count > 0) {
                    return false; // At least one type of tile is still available
                }
            }
            return true; // All tile types have been exhausted
        }
        private boolean maxTile(Tile tile){ //returns true if max legal amount of tile already in bag
            int[] legalTileCount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            return this.tileCount[tile.letter - 'A'] >= legalTileCount[tile.letter - 'A'];
        }
        private boolean legalLetter(char letter){//returns true is letter is in legal range
            return letter >= 'A' && letter <= 'Z';
        }
        public Tile getRand() {
            Random r = new Random(); // Create a new random object r
            int t; // Random tile index

            // Check if there are any available tiles
            while (!isEmpty()) {
                t = r.nextInt(26); // Generate a random index between 0 and 25

                // Check if the randomly selected tile type has available tiles
                if (tileCount[t] > 0) {
                    tileCount[t]--; // Decrement the count of available tiles for the selected type
                    return tileArray[t]; // Return the corresponding tile object
                }
            }

            return null; // Return null if there are no available tiles
        }
        public Tile getTile(char letter){
            if(!legalLetter(letter))
                return null;
            if(tileCount[letter - 'A'] > 0){
                tileCount[letter - 'A']--;
                return tileArray[letter - 'A'];
            }
            else
                return null;


        }
        public void put(Tile tile){
            if(!maxTile(tile))
                this.tileCount[tile.letter-'A']++;
//            else
//                System.out.println("Can't add: Max amount of tile already in bag!");
        }

        public int[] getQuantities () {
            int[] tmp = new int[26];
            System.arraycopy(this.tileCount, 0, tmp, 0, this.tileCount.length);
            return tmp;
        }



    }
}
