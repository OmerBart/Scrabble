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

/**
 * The GameManager class is a singleton class that manages the game. It is
 * responsible for
 * managing the game state, the players, the board, the bag, and the turns.
 *
 */
public class GameManager {
    private List<GuestPlayer> playersList;
    private LinkedHashMap<String, Integer> playerScores; // key: name+ID, value: score
    private LinkedHashMap<String, List<Tile>> playerTiles; // key: name+ID, value: tiles
    private MyServer hostServer;
    private MyServer IOserver;
    private Board gameBoard;
    private final String DEFAULT_BOOK = "search_books/The Matrix.txt";
    private Tile.Bag bag;
    private int turn;
    private boolean hasGameStarted;
    private String[] gameBooks;

    private static GameManager single_instance = null;
    private int numOfTurns;

    /**
     * The get function is a static function that returns the singleton instance of
     * GameManager.
     * If no instance exists, it creates one and then returns it.
     *
     *
     * @author Omer Bartfeld
     */
    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    /**
     * The GameManager function is a singleton class that manages the game.
     * It keeps track of all the players, their scores and tiles, as well as
     * the board and bag. It also handles all of the turns in a game.
     *
     * @author Omer Bartfeld
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
        gameBooks = new String[] {};
        turn = 0;
        numOfTurns = 50; // default value for number of turns
    }

    /**
     * The setHost function sets the hostserver variable to the given localhost
     * server.(essentially the injection of the game server)
     *
     * @param hostServer Set the hostserver variable
     *
     * @author Omer Bartfeld
     */
    public synchronized void setHost(MyServer hostServer) {
        this.hostServer = hostServer;
    }

    /**
     * The getPlayerTiles function returns a string of the tiles that are currently
     * in the player's hand.
     *
     *
     * @param playerName Identify the player
     *
     * @return A string of the tiles in a player's hand
     *
     * @author Omer Bartfeld
     */
    public synchronized String getPlayerTiles(String playerName) {
        StringBuilder tiles = new StringBuilder();
        for (Tile tile : playerTiles.get(playerName)) {
            tiles.append(tile).append(" ");
        }
        return tiles.toString();
    }

    /**
     * The addPlayer function adds a player to the game. It also sets the player's
     * ID and adds them to the playerScores and playerTiles HashMaps.
     * It will update the game host regarding each player that joined the game.
     * If the player is already in the game or the game is full, it will return a
     * message indicating why.
     * <p>
     *
     *
     * @param player To be added to the game
     *
     * @return A string indicating if the player was added successfully or not. If
     *         not, it will return a message indicating why.
     *         If the player was added successfully, it will return a message
     *         indicating the player's ID.
     *
     * @author Omer Bartfeld
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
     * The generateUniqueID function generates a unique ID for each player.
     *
     *
     * @return A unique id for each player
     *
     * @author Omer Bartfeld
     */
    private synchronized int generateUniqueID() {
        Random r = new Random();
        return r.nextInt(1000 + playersList.size());
    }

    /**
     * The getPlayer function is used to get a player from the playersList
     * ArrayList.
     *
     *
     * @param name Find the player in the game with the given name
     *
     * @return A GuestPlayer object
     *
     * @author Omer Bartfeld
     */
    public synchronized GuestPlayer getPlayer(String name) {
        for (GuestPlayer p : GameManager.get().playersList) {
            if (p.getName().split(":")[0].equals(name))
                return p;
        }
        return null;
    }

    /**
     * The getGameBoard function returns a string representation of the game board.
     *
     * @return A string representation of the game board
     *
     * @author Omer Bartfeld
     */
    public synchronized String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    /**
     * The myTurn function is called when a player's turn starts. It will update the
     * player who's turn it is.
     *
     * @author Omer Bartfeld
     */
    public synchronized void myTurn() {
        updatePlayer(getGameState(), turn % playersList.size());
    }

    /**
     * The `startGame` function is called when the first player joins the game. It
     * starts a new thread for IOserver,
     * which will be used to run Dictionary/IO actions in this game. The function
     * also initializes each player's tiles
     * and sends them an update of their current state.
     *
     * @param playerName The name of the player who is starting the game.
     * @return A string indicating if the game started successfully, followed by the
     *         game state.
     * @author Omer Bartfeld
     */
    public synchronized String startGame(String playerName) {
        if (gameBooks.length == 0)
            setGameBooks(DEFAULT_BOOK);
        // return "Please select a book to play with!";
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

    /**
     * The getTilefromBag function is used to get a random tile from the bag and add
     * it to the player's hand.
     *
     *
     * @param playerName Identify which player is making the move
     *
     * @return A tile from the bag or a message indicating that the bag is empty
     *
     * @author Omer Bartfeld
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
     * The endTurn function increments the turn counter, updates the players with
     * the current game state, and checks to see if it is time to end the game. If
     * so, it calls endGame(). Otherwise, it calls myTurn() which will determine
     * who's turn
     * is next. It returns a string containing the game state.
     * <p>
     *
     * @return The game state
     *
     * @author Omer Bartfeld
     */
    public String endTurn() {
        turn++;
        updatePlayers(getGameState());
        if (turn == numOfTurns)
            endGame();
        else
            myTurn();
        return getGameState();
    }

    /**
     * The endGame function is called when the game has ended.
     * It sends a message to all players that the game has ended, and then closes
     * both servers.
     *
     * @author Omer Bartfeld
     */
    public synchronized void endGame() {
        try {
            sleep(10);
            updatePlayer("Game has Ended!", turn % playersList.size());
            updatePlayers("Game has Ended!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        IOserver.close();
        hostServer.close();
    }

    /**
     * The printPlayers function returns a printable list of players in the game.
     *
     * @return A string that contains the names of all players in the game
     *
     * @author Omer Bartfeld
     */
    public synchronized String printPlayers() {
        return playersList.toString();
    }

    /**
     * The getScore function returns the score of a player as a string.
     *
     * @param playerName Get the score of a player
     *
     * @return The score of the player
     *
     * @author Omer Bartfeld
     */
    public synchronized String getScore(String playerName) {
        return Integer.toString(playerScores.getOrDefault(playerName, 0));
    }

    /**
     * The setNumOfTurns function sets the number of turns that will be played in a
     * game
     *
     * @param numOfTurns Set the number of turns in a game
     *
     *
     * @author Omer Bartfeld
     */
    public void setNumOfTurns(int numOfTurns) {
        this.numOfTurns = numOfTurns;
    }

    /**
     * The placeWord function takes in a player's name, the word they want to place
     * on the board,
     * and where they want to place it. It then checks if that move is valid by
     * calling tryPlaceWord
     * from the GameBoard(Board Class). If it is valid, then we update all of our
     * data structures accordingly (the gameboard itself,
     * the players' tiles and scores). We also check if any of our players have
     * under 7 tiles; if so we end the game.
     *
     * 
     * @param playerName   Identify the player who is placing a word
     * @param word         Get the word that the player wants to place on the board
     * @param x            Specify the x coordinate of the first letter in a word
     * @param y            Determine the y-coordinate of the first letter in a word
     * @param isHorizontal Determine whether the word is placed horizontally or
     *                     vertically
     *
     * @return On success, return the score of the word. Otherwise, return an error
     *         message.
     *
     * @author Omer Bartfeld
     */
    public String placeWord(String playerName, String word, int x, int y, boolean isHorizontal) {
        char[] carr = word.toUpperCase().toCharArray();
        Tile[] wordTiles = new Tile[word.length()];
        int index = 0;
        for (char c : carr) {
            if (c == '_') {
                wordTiles[index] = null;
            } else {
                wordTiles[index] = playerTiles.get(playerName).stream().filter(t -> t.getLetter() == c)
                        .findFirst().orElse(null);
                playerTiles.get(playerName).remove(wordTiles[index]);
            }
            index++;
        }
        Word w = new Word(wordTiles, x, y, !isHorizontal);
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // copy of wordTiles beacuse board.tryPlaceWord() replaces nulls with board already existing tiles
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Tile[] wordTilesCopy = new Tile[wordTiles.length];

        for (int i = 0; i < wordTiles.length; i++) {
            wordTilesCopy[i] = wordTiles[i];
        }

        int score = gameBoard.tryPlaceWord(w);
        if (score < 1) {
            for (Tile t : wordTilesCopy) {
                if (t != null) {
                    playerTiles.get(playerName).add(t);
                }
            }
            if (score == -1)
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

    /**
     * The getPlayerList function returns a string of all the players in the game,
     * along with their scores. This is used to update clients during
     * a game.
     * <p>
     *
     * @return A string formatted like this:
     *         &quot;PlayerName:Score,PlayerName2:Score,&quot; etc...
     *
     * @author Omer Bartfeld
     */
    public synchronized String getPlayerList() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersList) {
            sb.append(p.getName()).append(":Score:").append(playerScores.get(p.getName())).append(",");
        }
        return sb.toString();
    }

    /**
     *
     * @param gameBooks the gameBooks method is used to set for the game dictionary
     *                  must be in the format of:
     *                  &quot;search_books/The
     *                  Matrix.txt,search_books/book.txt&quot; etc...
     *
     */
    public void setGameBooks(String... gameBooks) {
        this.gameBooks = gameBooks;
    }

    /**
     * The queryIOserver function is used to send a query/challenge to the IOserver.
     * The function takes in a string, which is either begins with &quot;Q&quot; or
     * &quot;C&quot;, followed by the word that needs to be queried.
     * It then sends this string over TCP/IP connection and returns the result from
     * IOserver as a String.
     *
     * 
     * @param qword String that contains the query/challenge
     *
     * @return A string that contains the result of the query/challenge
     *
     * @author Omer Bartfeld
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

            return in.nextLine();
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    /**
     * The updatePlayer function is used to send a message to a specific player.
     *
     * @param msg            Send the message to the client
     * @param playerKeyIndex Index of the player that is being updated
     *
     * @author Omer Bartfeld
     */
    private void updatePlayer(String msg, int playerKeyIndex) {
        hostServer.sendToOne(msg, hostServer.getPlayerNames().get(playerKeyIndex));

    }

    /**
     * The updatePlayers function is used to send a message to all players except
     * the one whose turn it currently is.
     *
     * @param msg Message to send
     *
     *
     * @author Omer Bartfeld
     */
    private void updatePlayers(String msg) {
        hostServer.sendToAllButOne(msg, hostServer.getPlayerNames().get(turn % playersList.size()));
    }

    /**
     * The getPlayerTiles function returns a LinkedHashMap of the player's tiles.
     *
     *
     *
     * @return A linkedhashmap of string and list&lt;tile&gt;
     *
     * @author Omer Bartfeld
     */
    public synchronized LinkedHashMap<String, List<Tile>> getPlayerTiles() {
        return playerTiles;
    }

    /**
     * The getGameState function returns a string that contains all the information
     * needed to display the game state.
     * The first element of this string is the name of the player whose turn it is,
     * followed by a semicolon.
     * The second element is a printable version of the board, followed by another
     * semicolon.
     * The third element is a list containing all players' names and scores in
     * order, separated by commas and spaces;
     * each player's name and score are separated from one another with colons (:).
     * This list ends with another semicolon.
     * Finally, there's an integer representing how many turns are left in the game,
     * followed by a semicolon.
     *
     * @return A string with the following format:
     *         &quot;PlayerName;Board;PlayerName:Score,PlayerName2:Score;TurnsLeft;&quot;
     *
     */
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