package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.Game.Board;

import com.example.Scrabble.Model.Game.Tile;
import com.example.Scrabble.Model.Game.Word;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.Player;
import com.example.Scrabble.Model.ScrabbleDictionary.IOserver.BookScrabbleHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static java.lang.Thread.sleep;

public class GameManager {
    private List<GuestPlayer> playersList;
    private LinkedHashMap<String, Integer> playerScores; // key: name+ID, value: score
    private LinkedHashMap<String, List<Tile>> playerTiles; // key: name+ID, value: tiles
    private MyServer hostServer;
    private MyServer IOserver;
    private Board gameBoard;
    private Tile.Bag bag;
    private int turn;
    private boolean hasGameStarted;
    private String[] gameBooks;

    private static GameManager single_instance = null;
    private int numOfTurns;

    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    private GameManager() {
        Random r = new Random();
        playersList = new ArrayList<>();
        gameBoard = Board.getBoard();
        this.IOserver = new MyServer(6000 + r.nextInt(6000), new BookScrabbleHandler());
        bag = Tile.Bag.getBag();
        playerScores = new LinkedHashMap<>();
        playerTiles = new LinkedHashMap<>();
        hasGameStarted = false;
        gameBooks = new String[] { "search_books/The Matrix.txt", "search_books/test.txt" };
        turn = 0;
        numOfTurns = 50;
    }

    public synchronized void setHost(MyServer hostServer) {
        this.hostServer = hostServer;
    }

    public synchronized String getPlayerTiles(String playerName) {
        StringBuilder tiles = new StringBuilder();
        for (Tile tile : playerTiles.get(playerName)) {
            tiles.append(tile).append(" ");
        }
        return tiles.toString();
    }

    public synchronized String addPlayer(GuestPlayer player) {
        if (playersList.contains(player) || playersList.size() > 3) {
            return "Player already in the game or game is full!";
        } else {
            player.setID(generateUniqueID());
            playersList.add(player);
            playerScores.put(player.getName(), 0);
            playerTiles.put(player.getName(), new ArrayList<>());
            if (playersList.size() > 1) {
                updatePlayer("player added with ID: " + player.getPlayerID(), 0);
            }
            return "Player added to the game successfully with ID: " + player.getPlayerID();
        }
    }

    private synchronized int generateUniqueID() {
        Random r = new Random();
        return r.nextInt(1000 + playersList.size());
    }

    public synchronized GuestPlayer getPlayer(String name) {
        for (GuestPlayer p : GameManager.get().playersList) {
            if (p.getName().split(":")[0].equals(name))
                return p;
        }
        return null;
    }

    public synchronized String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    public synchronized void myTurn() {
        updatePlayer(getGameState(), turn % playersList.size());
    }

    public synchronized String startGame(String playerName) {
        IOserver.start();
        System.out.println("IO server started successfully at: " + IOserver.getPort());
        System.out.println("Number of players: " + playersList.size());
        System.out.println("Number of turns: " + numOfTurns);
        for (int i = 0; i < 7; i++) {
            for (Player p : playersList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }
        updatePlayers("game started!" + getGameState());

        hasGameStarted = true;
        return "Started Game!" + getGameState();
    }

    public String getTilefromBag(String playerName) {
        Tile t = bag.getRand();
        if (t == null) {
            System.out.println("Bag is empty!");
            return "Bag is empty!";
        } else {
            playerTiles.get(playerName).add(t);
            return "Got: " + t;
        }
    }

    public String endTurn() {
        turn++;
        updatePlayers(getGameState());
        if (turn == numOfTurns)
            endGame();
        else
            myTurn();
        return getGameState();
    }

    public synchronized void endGame() {
        try {
            sleep(1000);
            updatePlayer("Game has Ended!", turn % playersList.size());
            updatePlayers("Game has Ended!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        hostServer.close();
        IOserver.close();
    }

    public synchronized String printPlayers() {
        return playersList.toString();
    }

    public synchronized String getScore(String playerName) {
        return Integer.toString(playerScores.getOrDefault(playerName, 0));
    }

    public void setNumOfTurns(int numOfTurns) {
        this.numOfTurns = numOfTurns;
    }

    public String placeWord(String playerName, String word, int x, int y, boolean isHorizontal) {
        char[] carr = word.toUpperCase().toCharArray();
        Tile[] wordTiles = new Tile[word.length()];
        int index = 0;
        for (char c : carr) {
            if (c == '_')
                wordTiles[index] = null;
            else {
                wordTiles[index] = playerTiles.get(playerName).stream().filter(t -> t.getLetter() == c)
                        .findFirst().orElse(null);
                playerTiles.get(playerName).remove(wordTiles[index]);
            }
            index++;
        }
        Word w = new Word(wordTiles, x, y, !isHorizontal);
        int score = gameBoard.tryPlaceWord(w);
        if (score < 1) {
            for (Tile t : wordTiles) {
                if (t != null)
                    playerTiles.get(playerName).add(t);
            }
            if(score == -1)
                return "Error word isnt in game dictionary!";
            else
                return "Error invalid move!";
        }
        while (playerTiles.get(playerName).size() < 7) {
            Tile t = bag.getRand();
            if (t == null) {
                System.out.println("Bag is empty!");
                break;
            }
            playerTiles.get(playerName).add(t);
        }
        playerScores.put(playerName, playerScores.get(playerName) + score);

        String gameState = getGameState();
        updatePlayers(gameState);
        return gameState;
    }

    public synchronized String getPlayerList() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersList) {
            sb.append(p.getName()).append(":Score:").append(playerScores.get(p.getName())).append(",");
        }
        return sb.toString();
    }

    /**
     *
     * @param gameBooks the gameBooks to set for the game dictionary must be in
     *                  "search_books/The Matrix.txt" format see
     *                  and search_books folder to see available books
     */
    public void setGameBooks(String... gameBooks) {
        this.gameBooks = gameBooks;
    }

    public synchronized String queryIOserver(String qword) {
        try {
            Socket socket = new Socket("localhost", IOserver.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            if (qword.startsWith("Q")) {
                String args = "Q,";
                for (String book : gameBooks)
                    args += book + ",";
                out.println(args + qword.split(":")[1]);
                out.flush();
            } else if (qword.startsWith("C")) {
                String args = "C,";
                for (String book : gameBooks)
                    args += book + ",";
                out.println(args + qword.split(":")[1]);
                out.flush();
            }

            return in.nextLine();
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    private void updatePlayer(String msg, int playerKeyIndex) {
        hostServer.sendToOne(msg, hostServer.getPlayerNames().get(playerKeyIndex));

    }

    private void updatePlayers(String msg) {
        hostServer.sendToAllButOne(msg, hostServer.getPlayerNames().get(turn % playersList.size()));
    }

    public synchronized LinkedHashMap<String, List<Tile>> getPlayerTiles() {
        return playerTiles;
    }

    private String getGameState() {
        StringBuilder gameState = new StringBuilder();
        String playerTurn = playersList.get(turn % playersList.size()).getName();
        gameState.append(playerTurn).append(";");
        gameState.append(gameBoard.getPrintableBoard()).append(";");
        gameState.append(getPlayerList()).append(";");
        gameState.append(String.valueOf(numOfTurns - turn)).append(";");
        
        return gameState.toString();
    }
}