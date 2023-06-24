package com.example.Scrabble.Model.Game;

import com.example.Scrabble.Model.LocalServer.GameManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Board board = (Board) o;
        return Arrays.deepEquals(initialBoard, board.initialBoard) && Arrays.deepEquals(boardState, board.boardState);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(initialBoard);
        result = 31 * result + Arrays.deepHashCode(boardState);
        return result;
    }

    private static final ArrayList<Word> boardWords = new ArrayList<>();
    private static boolean isStar = false;
    private static Board single_instance = null;
    private final char[][] initialBoard;
    private final Tile[][] boardState;

    private Board() {
        // initialBoard is a reference for the initial state of the board
        // 0 = blank, 1 = star, 2 = double letter, 3 = triple letter, 4 = double word, 6
        // = triple word
        initialBoard = new char[][] { { 6, 0, 0, 2, 0, 0, 0, 6, 0, 0, 0, 2, 0, 0, 6 },
                { 0, 4, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 4, 0 },
                { 0, 0, 4, 0, 0, 0, 2, 0, 2, 0, 0, 0, 4, 0, 0 },
                { 2, 0, 0, 4, 0, 0, 0, 2, 0, 0, 0, 4, 0, 0, 2 },
                { 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 },
                { 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0 },
                { 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0 },
                { 6, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 6 },
                { 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0 },
                { 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0 },
                { 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 },
                { 2, 0, 0, 4, 0, 0, 0, 2, 0, 0, 0, 4, 0, 0, 2 },
                { 0, 0, 4, 0, 0, 0, 2, 0, 2, 0, 0, 0, 4, 0, 0 },
                { 0, 4, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 4, 0 },
                { 6, 0, 0, 2, 0, 0, 0, 6, 0, 0, 0, 2, 0, 0, 6 } };
        boardState = new Tile[15][15];
        for (int i = 0; i < boardState.length; i++)
            for (int j = 0; j < boardState.length; j++)
                boardState[i][j] = null;

    }

    public void printBoardWords() {
        for (Word w : boardWords) {
            w.printWord();
        }
    }

    public String getPrintableBoard() {
        StringBuilder s = new StringBuilder();
        for (Tile[] t : boardState) {

            for (Tile tt : t) {
                if (tt == null)
                    s.append(" |~| ");
                else
                    s.append(tt);
            }
            s.append(":");
            //s.append(":");
        }
        return s.toString();
    }

    public Tile[][] getTiles() {
        Tile[][] tmpTiles = new Tile[15][15];
        for (int i = 0; i < boardState.length; i++)
            System.arraycopy(boardState[i], 0, tmpTiles[i], 0, boardState.length);
        return tmpTiles;
    }

    public boolean boardLegal(Word word) {
        // need to overlap/adjacent with other words
        // can't override existing tiles on board
        // must fit inside board
        // first word must be on star

        Tile[] x = word.getTiles();
        boolean flag = false;

        int row = 0;
        int column = 0;
        if (word.getRow() < 0 && word.getRow() > 14 || word.getCol() < 0 && word.getCol() > 14)
            return false;

        else if (boardState[7][7] == null) {
            if ((word.isVertical() && word.getCol() == 7 && word.getRow() <= 7 && word.getRow() >= 0
                    && word.getRow() <= 14)
                    || (!word.isVertical() && word.getRow() == 7 && word.getCol() <= 7) && word.getCol() >= 0
                    && word.getCol() <= 14)
                flag = (word.getRow() + x.length >= 7 && word.getRow() + x.length <= 14)
                        || (word.getCol() + x.length >= 7 && word.getCol() + x.length <= 14);
        } else {
            if (word.isVertical())
                row++;
            else
                column++;
            for (Tile t : x) {
                if ((word.isVertical() && word.getRow() + x.length > 14)
                        || (!word.isVertical() && word.getCol() + x.length > 14))
                    flag = false;
                else if (t == null && boardState[word.getRow() + row][word.getRow() + column] != null
                        || t != null && boardState[word.getRow() + row][word.getRow() + column] == null)
                    flag = true;
            }

        }
        return flag;

    }

    public boolean dictionaryLegal(Word word) {
        GameManager gm = GameManager.get();
        return Boolean.parseBoolean(gm.queryIOserver("Q:"+word.toString())); // send to IO server
        //return true;
    }

    private Word getFull(Word word, int index, int adj) {
        // 8
        // 4 6
        // 2
        // 5 tile is already on board
        ArrayList<Tile> tileArrayList = new ArrayList<>();
        int head_row = 0;
        int head_column = 0;
        int c = 0; // column iterator
        int r = 0; // row iterator
        boolean foo = false;
        boolean flag = false;

        while (!flag) {

            if (adj == 5) {
                if (word.isVertical())
                    r = index;
                else
                    c = index;
                for (Tile x : word.getTiles()) {
                    if (x == null) {
                        // System.out.println("$*$* - r: " + (word.getRow() + r) + " c: " +
                        // (word.getCol() + c) + " " + boardState[word.getRow() + r][word.getCol() +
                        // c]);
                        tileArrayList.add(boardState[word.getRow() + r][word.getCol() + c]);
                    } else
                        tileArrayList.add(x);
                }
                Tile[] tt = new Tile[tileArrayList.size()];
                int i = 0;
                for (Tile t : tileArrayList) {
                    tt[i] = t;
                    i++;
                }

                return new Word(tt, word.getRow(), word.getCol(), word.isVertical());

            } else if (adj == 2) {
                if (tileArrayList.isEmpty()) {
                    head_row = word.getRow();
                    head_column = word.getCol() + index;
                } else if (boardState[head_row + r + 1][head_column] == null) {
                    // if 2 cases word tile not on board
                    tileArrayList.add(boardState[head_row + r][head_column]);
                    return getWord(word, tileArrayList, head_row, head_column);
                }

                if (boardState[head_row + r][head_column] != null) {
                    tileArrayList.add(boardState[head_row + r][head_column]);
                } else {
                    tileArrayList.add(word.getTiles()[index]);
                }

                r++;

            } else if (adj == 6) {
                if (tileArrayList.isEmpty()) {
                    head_row = word.getRow() + index;
                    head_column = word.getCol();
                }
                if (boardState[head_row][head_column + c + 1] == null) {
                    // System.out.println("Bout to return ");
                    tileArrayList.add(boardState[head_row][head_column + c]);
                    return getWord(word, tileArrayList, head_row, head_column);
                }
                if (boardState[head_row][head_column + c] != null) {
                    tileArrayList.add(boardState[head_row][head_column + c]);
                } else {
                    tileArrayList.add(word.getTiles()[index]);
                }

                c++;

            } else if (adj == 4) {
                if (boardState[word.getRow() - r - 1][word.getCol() + index] == null || foo) {
                    if (tileArrayList.isEmpty()) {
                        head_column = word.getCol() - c;
                        head_row = word.getRow() + index;
                        foo = true;
                        tileArrayList.add(boardState[head_row][head_column]);
                        c = 0;
                    } else if (boardState[head_row][head_column + c + 1] == null) {
                        if (word.getTiles()[index] != null && head_column + c + 1 == word.getCol()) {
                            tileArrayList.add(word.getTiles()[index]);
                        }
                        Tile[] tt = new Tile[tileArrayList.size()];
                        int i = 0;
                        for (Tile t : tileArrayList) {
                            tt[i] = t;
                            i++;
                        }
                        return new Word(tt, head_row, head_column, !word.isVertical());
                    }

                    r++;
                    if (boardState[head_row][head_column + c] != null)
                        tileArrayList.add(boardState[head_row][head_column + c]);
                    else
                        tileArrayList.add(word.getTiles()[index]);
                }

                else
                    c++;

            } else if (adj == 8) {
                if (boardState[word.getRow() - r - 1][word.getCol() + index] == null || foo) {
                    if (tileArrayList.isEmpty()) {
                        head_column = word.getCol() + index;
                        head_row = word.getRow() - r;
                        foo = true;
                        tileArrayList.add(boardState[head_row][head_column]);
                        r = 0;
                    } else if (boardState[head_row + r + 1][head_column] == null) {
                        if (word.getTiles()[index] != null && head_row + r + 1 == word.getRow()) {
                            tileArrayList.add(word.getTiles()[index]);
                        }
                        Tile[] tt = new Tile[tileArrayList.size()];
                        int i = 0;
                        for (Tile t : tileArrayList) {
                            tt[i] = t;
                            i++;
                        }
                        return new Word(tt, head_row, head_column, !word.isVertical());
                    }

                    r++;
                    if (boardState[head_row + r][head_column] != null)
                        tileArrayList.add(boardState[head_row + r][head_column]);
                    else
                        tileArrayList.add(word.getTiles()[index]);
                }

                else
                    r++;
            } else
                flag = true;

        }

        return null;
    }

    private Word getWord(Word word, ArrayList<Tile> tileArrayList, int head_row, int head_column) {
        Tile[] tt = new Tile[tileArrayList.size()];
        int i = 0;
        for (Tile t : tileArrayList) {
            tt[i] = t;
            i++;
        }
        return new Word(tt, head_row, head_column, !word.isVertical());
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> wordArray = new ArrayList<>();
        int[] adj = getAdj(word);
        // word.printWord();
        Word w;
        int count = 0;
        int count1 = 0;

        for (int i = 0; i < adj.length; i++) {
            if (adj[i] == 0) {
                count++;
                continue;
            } else if (adj[i] == 5)
                count1 = 5;

            w = getFull(word, i, adj[i]);

            if (!boardWords.contains(w)) {

                wordArray.add(w);
            }
            // }

        }
        if (count == word.getTiles().length || count1 != 5)
            wordArray.add(word);

        return wordArray;
    }

    private int[] getAdj(Word word) {
        int head_row = word.getRow();
        int head_col = word.getCol();

        int c = 0; // column iterator
        int r = 0; // row iterator

        int[] adj = new int[word.getTiles().length];

        for (int i = 0; i < word.getTiles().length; i++) {
            boolean flag = word.getCol() + c > 0 && word.getCol() + c < 14 && word.getRow() + r > 0
                    && word.getRow() + r < 14;

            if (word.isVertical()) {
                if (word.getTiles()[i] == null || boardState[head_row + i][head_col] == word.getTiles()[i]) {
                    adj[i] = 5;
                    r++;
                    continue;
                } else if (r == 0 && flag && boardState[head_row - 1][head_col] != null) {
                    adj[i] = 8;
                    r++;
                    continue;
                }
                r++;
            } else {
                if (word.getTiles()[i] == null || boardState[head_row][head_col + i] == word.getTiles()[i]) {
                    adj[i] = 5;
                    c++;
                    continue;
                } else if (c == 0 && flag && boardState[head_row][head_col - 1] != null) {
                    adj[i] = 4;
                    c++;
                    continue;
                }
                c++;
            }

            boolean foo;
            if (!word.isVertical()) {
                foo = boardState[head_row - 1][head_col + i] != null || boardState[head_row + 1][head_col + i] != null;
                adj[i] = foo ? 8 : 2;
            } else {
                foo = boardState[head_row + i][head_col - 1] != null || boardState[head_row + i][head_col + 1] != null;
                adj[i] = foo ? 4 : 6;
            }
        }

        return adj;
    }

    public int getScore(Word word) {
        // for(Tile xx : word.getTiles())
        // System.out.println(xx);
        int row = 0;
        int column = 0;
        int score = 0;
        boolean doubleScore = false;
        boolean tripleScore = false;
        if (word == null)
            return 0;
        for (Tile x : word.getTiles()) {

            switch (initialBoard[word.getRow() + row][word.getCol() + column]) {

                case 0:
                    if (x == null) {
                        // System.out.println("row = " + word.getRow() + "Column = " + word.getCol());
                        // System.out.println(word.getCol());
                        if (boardState[word.getRow() + row][word.getCol() + column] != null)
                            score += boardState[word.getRow() + row][word.getCol() + column].score;
                    } else
                        score += x.score;
                    break;
                case 1:
                    if (!isStar) {
                        if (x == null)
                            score += boardState[word.getRow() + row][word.getCol() + column].score;
                        else {
                            doubleScore = true;
                            score += x.score;
                        }
                        isStar = true;
                    } else {
                        if (x == null)
                            score += boardState[word.getRow() + row][word.getCol() + column].score;
                        else
                            score += x.score;
                    }
                    break;

                case 4:
                    if (x == null)
                        score += boardState[word.getRow() + row][word.getCol() + column].score;
                    else {
                        doubleScore = true;
                        score += x.score;
                    }
                    break;
                case 2:
                    if (x == null)
                        score += boardState[word.getRow() + row][word.getCol() + column].score;
                    else
                        score += x.score * 2;
                    break;
                case 3:
                    if (x == null)
                        score += boardState[word.getRow() + row][word.getCol() + column].score;
                    else
                        score += x.score * 3;
                    break;
                case 6:
                    if (x == null)
                        score += boardState[word.getRow() + row][word.getCol() + column].score;
                    else {
                        tripleScore = true;
                        score += x.score;
                    }
                    break;

            }
            if (word.isVertical())
                row++;
            else
                column++;

        }
        if (boardWords.contains(word)) {
            // System.out.println("INSIDE" + score);
            return 0;
        } else if (doubleScore)
            score = score * 2;
        else if (tripleScore)
            score = score * 3;
        // boardWords.add()
        // System.out.println("OUTSIDE" + score);
        return score;
    }

    public int tryPlaceWord(Word word) {

        ArrayList<Word> wordArray;
        int totalScore = 0;
        int row = 0; // row iterator
        int column = 0; // column iterator

        if (boardLegal(word) && dictionaryLegal(word)) {
            wordArray = getWords(word);
            for (Tile x : word.getTiles()) {
                if (x != null && word.isVertical()) {
                    boardState[word.getRow() + row][word.getCol() + column] = x;
                    row++;
                }

                // if (word.isVertical())
                // row++;
                else
                    column++;

            }

            for (Word w : wordArray) {
                // // System.out.println(w);

                totalScore += getScore(w);
                boardWords.add(w);

            }
        } else
            return 0;

        // Tile.Bag b = Tile.Bag.getBag();

        return totalScore;

    }

    public static Board getBoard() {
        if (single_instance == null)
            single_instance = new Board();
        return single_instance;
    }

    // reset the board
    public void reset() {
        boardWords.clear();
        isStar = false;
        single_instance = null;
    }
}