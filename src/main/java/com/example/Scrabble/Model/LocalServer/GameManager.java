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

/**
 * The GameManager class is responsible for managing the Scrabble game, including player management, game board,
 * scoring, and turn management.
 */
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

    /**
     * Returns the singleton instance of the GameManager class.
     *
     * @return The singleton instance of the GameManager class.
     */
    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    /**
     * Private constructor to enforce singleton behavior and initialize the game manager.
     */
    private GameManager() {
        Random r = new Random();
        playersList = new ArrayList<>();
        gameBoard = Board.getBoard();
        this.IOserver = new MyServer(6000 + r.nextInt(6000), new BookScrabbleHandler());
        bag = Tile.Bag.getBag();
        playerScores = new LinkedHashMap<>();
        playerTiles = new LinkedHashMap<>();
        hasGameStarted = false;
        gameBooks = new String[]{"search_books/The Matrix.txt", "search_books/test.txt"};
        turn = 0;
    }

    /**
     * Sets the host server for the game.
     *
     * @param hostServer The host server to set.
     */
    public synchronized void setHost(MyServer hostServer) {
        this.hostServer = hostServer;
    }

    /**
     * Retrieves the tiles of a player as a string.
     *
     * @param playerName The name of the player.
     * @return A string representation of the player's tiles.
     */
    public synchronized String getPlayerTiles(String playerName) {
        StringBuilder tiles = new StringBuilder();
        for (Tile tile : playerTiles.get(playerName)) {
            tiles.append(tile).append(" ");
        }
        return tiles.toString();
    }

    /**
     * Adds a player to the game.
     *
     * @param player The guest player to add.
     * @return A message indicating the success or failure of adding the player.
     */
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

    /**
     * Generates a unique ID for a player.
     *
     * @return The generated unique ID.
     */
    private synchronized int generateUniqueID() {
        Random r = new Random();
        return r.nextInt(1000 + playersList.size());
    }

    /**
     * Retrieves the player with the specified name.
     *
     * @param name The name of the player.
     * @return The GuestPlayer object corresponding to the specified name, or null if not found.
     */
    public synchronized GuestPlayer getPlayer(String name) {
        for (GuestPlayer p : GameManager.get().playersList) {
            if (p.getName().split(":")[0].equals(name))
                return p;
        }
        return null;
    }

    /**
     * Retrieves the game board as a string.
     *
     * @return The printable representation of the game board.
     */
    public synchronized String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    /**
     * Signals the start of the current player's turn.
     */
    public synchronized void myTurn() {
        updatePlayer("T:true", turn % playersList.size());
    }

    /**
     * Starts the game and initializes the players and their tiles.
     *
     * @param playerName The name of the player who initiated the game start.
     * @return A message indicating the success or failure of starting the game.
     */
    public synchronized String startGame(String playerName) {
        IOserver.start();
        System.out.println("IO server started successfully at: " + IOserver.getPort());
        System.out.println("Number of players: " + playersList.size());
        for (int i = 0; i < 7; i++) {
            for (Player p : playersList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }
        updatePlayers("game started!");

        hasGameStarted = true;
        return "Game Started!";
    }

    /**
     * Retrieves a tile from the bag and adds it to the player's tiles.
     *
     * @param playerName The name of the player.
     * @return A message indicating the tile obtained from the bag.
     */
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

    /**
     * Ends the current turn and prepares for the next turn.
     */
    public void endTurn() {
        System.out.println(playersList.get((turn) % playersList.size()).getName() + "'s turn ended");
        turn++;
        if (turn == numOfTurns)
            endGame();
        else
            myTurn();
    }

    /**
     * Ends the game and performs cleanup operations.
     */
    public synchronized void endGame() {
        try {
            Thread.sleep(1000);
            updatePlayer("Game has Ended!", turn % playersList.size());
            updatePlayers("Game has Ended!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        hostServer.close();
        IOserver.close();
    }

    /**
     * Retrieves a string representation of the list of players.
     *
     * @return A string representing the list of players.
     */
    public synchronized String printPlayers() {
        return playersList.toString();
    }

    /**
     * Retrieves the score of a player.
     *
     * @param playerName The name of the player.
     * @return The score of the player.
     */
    public synchronized String getScore(String playerName) {
        return Integer.toString(playerScores.getOrDefault(playerName, 0));
    }

    /**
     * Sets the number of turns for the game.
     *
     * @param numOfTurns The number of turns to set.
     */
    public void setNumOfTurns(int numOfTurns) {
        this.numOfTurns = numOfTurns;
    }

    /**
     * Places a word on the game board and updates the player's score.
     *
     * @param playerName   The name of the player.
     * @param word         The word to place on the board.
     * @param x            The x-coordinate of the starting position.
     * @param y            The y-coordinate of the starting position.
     * @param isHorizontal A flag indicating if the word is placed horizontally.
     * @return A message indicating the success or failure of placing the word.
     */
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
            return "Invalid move!";
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
        updatePlayers("Board Updated " + playerName + " got " + score + " points for " + word);
        return Integer.toString(score);
    }

    /**
     * Retrieves a string representation of the players and their scores.
     *
     * @return A string representation of the players and their scores.
     */
    public synchronized String getPlayerList() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersList) {
            sb.append(p.getName()).append(":Score:").append(playerScores.get(p.getName())).append(",");
        }
        return sb.toString();
    }

    /**
     * Sets the game books for the game dictionary.
     *
     * @param gameBooks The game books to set for the game dictionary.
     *                  The books must be in the format "search_books/The Matrix.txt".
     *                  See the "search_books" folder to see available books.
     */
    public void setGameBooks(String... gameBooks) {
        this.gameBooks = gameBooks;
    }

    /**
     * Queries the IO server with a word.
     *
     * @param qword The word to query.
     * @return The result of the query.
     */
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

            String res = in.nextLine();
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a player with a message.
     *
     * @param msg   The message to send.
     * @param playerIdx The index of the player to update.
     */
    private void updatePlayer(String msg, int playerIdx) {
        hostServer.sendToOne(msg, hostServer.getPlayerKeys().get(playerIdx));
    }

    /**
     * Updates all players with a message.
     *
     * @param msg The message to send.
     */
    private void updatePlayers(String msg) {
        hostServer.sendToAllButOne(msg, hostServer.getPlayerKeys().get(turn % playersList.size()));
    }
}
