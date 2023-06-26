package com.example.Scrabble.Model.Game;

import java.util.ArrayList;

// singltone class
public class Board {
   private static Board instance;
   private BoardBox[][] board = new BoardBox[15][15];
   private static boolean isStarBonusHasBeenUsed = false;
   private static ArrayList<Word> allWords = new ArrayList<Word>();

   private Board() {
       for (int i = 0; i < 15; i++) {
           for (int j = 0; j < 15; j++) {
               board[i][j] = new BoardBox();
           }
       }
   }

   public static Board getBoard() {
       if (instance == null) {
           instance = new Board();
       }

       return instance;
   }

   // get tiles
   public Tile[][] getTiles() {
       Tile[][] tiles = new Tile[15][15];
       for (int i = 0; i < 15; i++) {
           for (int j = 0; j < 15; j++) {
               if (board[i][j].getTile() != null) {
                   tiles[i][j] = board[i][j].getTile();
               }
           }
       }
       return tiles;
   }

   public boolean boardLegal(Word word) {
       // check if all tiles are on the board
       if (!allTilesOnBoard(word)) {
           return false;
       }
       // check if placing the word on the board changes other words
       if (wordChangesOtherWords(word)) {
           return false;
       }
       // check if the word is connected to another word
       if (!wordConnectedToCenterTile(word) && !wordConnectedToOtherWord(word)) {
           return false;
       }

       return true;
   }

   public boolean dictionaryLegal(Word word) {
       // return true for now
       return true;
   }

   // check if adding the word to the board creates other words
   public ArrayList<Word> getWords(Word word) {
       ArrayList<Word> words = new ArrayList<Word>();
       if (!boardLegal(word)) {
           return words;
       }
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       // check if the word is horizontal
       if (!vertical) {
           for (int i = 0; i < length; i++) {
               ArrayList<Tile> newWordTiles = new ArrayList<Tile>();
               newWordTiles.add(tiles[i]);
               checkUp(row, col + i, newWordTiles);
               int newRow = row - newWordTiles.size() + 1;
               checkDown(row, col + i, newWordTiles);
               Word newWord = new Word(newWordTiles.toArray(new Tile[newWordTiles.size()]), newRow, col + i, true);
               if (newWord.getTiles().length > 1 && boardLegal(newWord) && dictionaryLegal(newWord)
                       && !wordAlreadyOnTheBoard(newWord)) {
                   words.add(newWord);
                   allWords.add(newWord);
               }
           }
       } else {
           for (int i = 0; i < length; i++) {
               ArrayList<Tile> newWordTiles = new ArrayList<Tile>();
               newWordTiles.add(tiles[i]);
               checkLeft(row + i, col, newWordTiles);
               int newCol = col - newWordTiles.size() + 1;
               checkRight(row + i, col, newWordTiles);
               Word newWord = new Word(newWordTiles.toArray(new Tile[newWordTiles.size()]), row + i, newCol, false);
               if (newWord.getTiles().length > 1 && boardLegal(newWord) && dictionaryLegal(newWord)
                       && !wordAlreadyOnTheBoard(newWord)) {
                   words.add(newWord);
                   allWords.add(newWord);
               }
           }
       }

       return words;
   }

   public int getScore(Word word) {
       int score = 0;
       int multiplier = 1;
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       // check if the word is horizontal
       if (!vertical) {
           for (int i = 0; i < length; i++) {
               if (tiles[i] != null) {
                   int letterScore = tiles[i].score;
                   if (tripleBonusLetter(row, col + i)) {
                       letterScore *= 3;
                   } else if (doubleBonusLetter(row, col + i)) {
                       letterScore *= 2;
                   }
                   if (tripleBonusWord(row, col + i)) {
                       multiplier *= 3;
                   } else if (doubleBonusWord(row, col + i)) {
                       multiplier *= 2;
                   } else if (isItAStar(row, col + i)) {
                       multiplier *= 2;
                   }
                   score += letterScore;
               } else if (board[row][col + i].getTile() != null) {
                   int letterScore = board[row][col + i].getTile().score;
                   if (tripleBonusLetter(row, col + i)) {
                       letterScore *= 3;
                   } else if (doubleBonusLetter(row, col + i)) {
                       letterScore *= 2;
                   }
                   if (tripleBonusWord(row, col + i)) {
                       multiplier *= 3;
                   } else if (doubleBonusWord(row, col + i)) {
                       multiplier *= 2;
                   } else if (isItAStar(row, col + i)) {
                       multiplier *= 2;
                   }
                   score += letterScore;
               }
           }
       } else if (vertical) {
           for (int i = 0; i < length; i++) {
               if (tiles[i] != null) {
                   int letterScore = tiles[i].score;
                   if (tripleBonusLetter(row + i, col)) {
                       letterScore *= 3;
                   } else if (doubleBonusLetter(row + i, col)) {
                       letterScore *= 2;
                   }
                   if (tripleBonusWord(row + i, col)) {
                       multiplier *= 3;
                   } else if (doubleBonusWord(row + i, col)) {
                       multiplier *= 2;
                   } else if (isItAStar(row + i, col)) {
                       multiplier *= 2;
                   }
                   score += letterScore;
               } else if (board[row + i][col].getTile() != null) {
                   int letterScore = board[row + i][col].getTile().score;
                   if (tripleBonusLetter(row + i, col)) {
                       letterScore *= 3;
                   } else if (doubleBonusLetter(row + i, col)) {
                       letterScore *= 2;
                   }
                   if (tripleBonusWord(row + i, col)) {
                       multiplier *= 3;
                   } else if (doubleBonusWord(row + i, col)) {
                       multiplier *= 2;
                   } else if (isItAStar(row + i, col)) {
                       multiplier *= 2;
                   }
                   score += letterScore;
               }
           }
       }

       return score * multiplier;
   }

   public int tryPlaceWord(Word word) {
       if (!boardLegal(word)) {
           return 0;
       }
       replceNullTilesWithBoardTiles(word);

       int score = 0;
       ArrayList<Word> words = new ArrayList<Word>();
       if (wordAlreadyOnTheBoard(word)) {
           return 0;
       }
       allWords.add(word);
       words.add(word);
       words.addAll(getWords(word));

       for (Word w : words) {
            System.out.println(w);
           if (!dictionaryLegal(w) || !boardLegal(w)) {
               return 0;
           } else {
               setWordOnTheBoard(w);
               score += getScore(w);
               System.out.println(score);
           }
       }

       return score;
   }

   public String getPrintableBoard(){
       StringBuilder stringBuilder = new StringBuilder();
       Tile t;
       for(BoardBox[] abb : board){
           for(BoardBox bb : abb){
               t = bb.tile;
               if(t == null)
                   stringBuilder.append("|~|");
               else
                   stringBuilder.append(t);
           }
           //stringBuilder.append(System.getProperty("line.separator"));
           stringBuilder.append(":");
       }
       return stringBuilder.toString();
   }


   /*------------------------------- private methodes ------------------------------- */

   private boolean allTilesOnBoard(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();

       if (row < 0 || row > 14 || col < 0 || col > 14) {
           return false;
       }

       if (tiles == null || tiles.length == 0) {
           return false;
       }

       int length = tiles.length;
       if (vertical) {
           if (row + length > 15) {
               return false;
           }
       } else {
           if (col + length > 15) {
               return false;
           }
       }

       return true;
   }

   private boolean wordConnectedToOtherWord(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       if (vertical) {
           for (int i = 0; i < length; i++) {
               if (checkNeighbour(row + i, col)) {
                   return true;
               }
           }
       } else {
           for (int i = 0; i < length; i++) {
               if (checkNeighbour(row, col + i)) {
                   return true;
               }
           }
       }

       return false;
   }

   private boolean checkNeighbour(int row, int col) {
       if (board[row + 1][col].getTile() != null || (row != 0 && board[row - 1][col].getTile() != null)
               || board[row][col + 1].getTile() != null
               || (col != 0 && board[row][col - 1].getTile() != null)) {
           return true;
       }
       return false;
   }

   private boolean wordChangesOtherWords(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       if (vertical) {
           for (int i = 0; i < length; i++) {
               if (board[row + i][col].getTile() == tiles[i] || board[row + i][col].getTile() == null) {
                   return false;
               }
           }
       } else {
           for (int i = 0; i < length; i++) {
               if (board[row][col + i].getTile() == tiles[i] || board[row][col + i].getTile() == null) {
                   return false;
               }
           }
       }

       return true;
   }

   private boolean wordConnectedToCenterTile(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       if (vertical && col == 7) {
           for (int i = 0; i < length; i++) {
               if (row + i == 7) {
                   return true;
               }
           }
       } else if (!vertical && row == 7) {
           for (int i = 0; i < length; i++) {
               if (col + i == 7) {
                   return true;
               }
           }
       }

       return false;
   }

   private void checkUp(int row, int col, ArrayList<Tile> tiles) {
       if (row == 0) {
           return;
       }
       if (board[row - 1][col].getTile() != null) {
           // put the tile in the arraylist at the beginning
           tiles.add(0, board[row - 1][col].getTile());
           checkUp(row - 1, col, tiles);
       }
   }

   private void checkDown(int row, int col, ArrayList<Tile> tiles) {
       if (row == 14) {
           return;
       }
       if (board[row + 1][col].getTile() != null) {

           // push the tile to the arraylist at the end
           tiles.add(board[row + 1][col].getTile());
           checkDown(row + 1, col, tiles);
       }
   }

   private void checkLeft(int row, int col, ArrayList<Tile> tiles) {
       if (col == 0) {
           return;
       }
       if (board[row][col - 1].getTile() != null) {
           // put the tile in the arraylist at the end
           tiles.add(0, board[row][col - 1].getTile());
           checkLeft(row, col - 1, tiles);
       }
   }

   private void checkRight(int row, int col, ArrayList<Tile> tiles) {
       if (col == 14) {
           return;
       }
       if (board[row][col + 1].getTile() != null) {
           // put the tile in the arraylist at the start
           tiles.add(board[row][col + 1].getTile());
           checkRight(row, col + 1, tiles);
       }
   }

   private void setWordOnTheBoard(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       if (vertical) {
           for (int i = 0; i < length; i++) {
               if (board[row + i][col].getTile() == null) {
                   board[row + i][col].setTile(tiles[i]);
               }
           }
       } else {
           for (int i = 0; i < length; i++) {
               if (board[row][col + i].getTile() == null) {
                   board[row][col + i].setTile(tiles[i]);
               }
           }
       }
   }

   private void replceNullTilesWithBoardTiles(Word word) {
       int row = word.getRow();
       int col = word.getCol();
       boolean vertical = word.isVertical();
       Tile[] tiles = word.getTiles();
       int length = tiles.length;

       if (vertical) {
           for (int i = 0; i < length; i++) {
               if (tiles[i] == null) {
                   tiles[i] = board[row + i][col].getTile();
               }
           }
       } else {
           for (int i = 0; i < length; i++) {
               if (tiles[i] == null) {
                   tiles[i] = board[row][col + i].getTile();
               }
           }
       }
   }

   private boolean wordAlreadyOnTheBoard(Word word) {
       for (Word w : allWords) {
           if (w.equals(word)) {
               return true;
           }
       }
       return false;
   }

   // check for bonus points
   private boolean tripleBonusWord(int row, int col) {
       if (row == 0 && (col == 0 || col == 7 || col == 14)) {
           return true;
       } else if (row == 7 && (col == 0 || col == 14)) {
           return true;
       } else if (row == 14 && (col == 0 || col == 7 || col == 14)) {
           return true;
       }
       return false;
   }

   private boolean doubleBonusWord(int row, int col) {
       if (row == 1 && (col == 1 || col == 13)) {
           return true;
       } else if (row == 2 && (col == 2 || col == 12)) {
           return true;
       } else if (row == 3 && (col == 3 || col == 11)) {
           return true;
       } else if (row == 4 && (col == 4 || col == 10)) {
           return true;
       } else if (row == 10 && (col == 4 || col == 10)) {
           return true;
       } else if (row == 11 && (col == 3 || col == 11)) {
           return true;
       } else if (row == 12 && (col == 2 || col == 12)) {
           return true;
       } else if (row == 13 && (col == 1 || col == 13)) {
           return true;
       }

       return false;
   }

   private boolean tripleBonusLetter(int row, int col) {
       if (row == 5 && (col == 1 || col == 5 || col == 9 || col == 13)) {
           return true;
       } else if (row == 9 && (col == 1 || col == 5 || col == 9 || col == 13)) {
           return true;
       } else if (row == 1 && (col == 5 || col == 9)) {
           return true;
       } else if (row == 13 && (col == 5 || col == 9)) {
           return true;
       }
       return false;
   }

   private boolean doubleBonusLetter(int row, int col) {
       if (row == 0 && (col == 3 || col == 11)) {
           return true;
       } else if (row == 2 && (col == 6 || col == 8)) {
           return true;
       } else if (row == 3 && (col == 0 || col == 7 || col == 14)) {
           return true;
       } else if (row == 6 && (col == 2 || col == 6 || col == 8 || col == 12)) {
           return true;
       } else if (row == 7 && (col == 3 || col == 11)) {
           return true;
       } else if (row == 8 && (col == 2 || col == 6 || col == 8 || col == 12)) {
           return true;
       } else if (row == 11 && (col == 0 || col == 7 || col == 14)) {
           return true;
       } else if (row == 12 && (col == 6 || col == 8)) {
           return true;
       } else if (row == 14 && (col == 3 || col == 11)) {
           return true;
       }
       return false;
   }

   private boolean isItAStar(int row, int col) {
       if (row == 7 && col == 7 && isStarBonusHasBeenUsed == false) {
           isStarBonusHasBeenUsed = true;
           return true;
       }
       return false;
   }

   public class BoardBox {
       private Tile tile;
       private boolean isOccupied;
       private final int scoreBonus;

       public BoardBox() {
           this.tile = null;
           this.isOccupied = false;
           this.scoreBonus = 1;
       }

       public BoardBox(int scoreBonus) {
           this.tile = null;
           this.scoreBonus = scoreBonus;
           this.isOccupied = false;
       }

       public Tile getTile() {
           return tile;
       }

       public void setTile(Tile tile) {
           this.tile = tile;
           isOccupied = true;
       }

       public boolean isOccupied() {
           return isOccupied;
       }

       public void setOccupied(boolean isOccupied) {
           this.isOccupied = isOccupied;
       }

       public int getScoreBonus() {
           return scoreBonus;
       }

   }

}
