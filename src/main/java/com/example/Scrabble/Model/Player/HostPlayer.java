package com.example.Scrabble.Model.Player;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

/**
 * Represents a host player in the Scrabble game.
 * Extends the GuestPlayer class.
 *
 * This class is responsible for managing the game as a host player.
 * It creates and starts a server, sets it as the host for the GameManager,
 * and provides methods for configuring the game settings.
 *
 * @author Omer Bartfeld
 * @version 1.0
 */
public class HostPlayer extends GuestPlayer {
    // books to choose from:
    //alice_in_wonderland.txt
    //Frank Herbert_Dune.txt
    //Harry Potter.txt
    //mobydick.txt
    //pg10.txt
    //shakespeare.txt
    //The Matrix.txt

    private final MyServer hostGameServer;
    private GameManager gameManager;
    private static HostPlayer hostPlayerInstance = null;

    /**
     * Retrieves the instance of the HostPlayer.
     *
     * @param player The player object associated with the host.
     * @return The HostPlayer instance.
     */
    public static HostPlayer get(Player player) {
        if (hostPlayerInstance == null) {
            hostPlayerInstance = new HostPlayer(player);
        }
        return hostPlayerInstance;
    }

    /**
     * Constructs a new HostPlayer object.
     *
     * When a HostPlayer is constructed, a new MyServer object is created and started.
     * The server is then set as the host for the GameManager.
     * Finally, the player joins the game.
     *
     * @param player The player object associated with the host.
     */
    private HostPlayer(Player player) {
        super(player);
        int port = 65432;
        hostGameServer = new MyServer(port, new PlayerHandler());
        setServerAddress("localhost", port);
        hostGameServer.start();
        gameManager = GameManager.get();
        gameManager.setHost(hostGameServer);
        System.out.println(joinGame());
    }

    /**
     * Sets the number of turns for the game.
     *
     * @param numOfTurns The number of turns.
     */
    public void setNumOfTurns(int numOfTurns) {
        gameManager.setNumOfTurns(numOfTurns);
    }

    /**
     * Sets the books to be used in the game.
     *
     * @param books The books to be used in the game.
     *              Must be in the format of "search_books/BookName.txt" or "search_books/BookName.txt search_books/BookName2.txt...etc".
     */
    public void setBooks(String... books) {
        gameManager.setGameBooks(books);
    }

    /**
     * Retrieves the host game server.
     *
     * @return The MyServer object representing the host game server.
     */
    public MyServer getHostGameServer() {
        return hostGameServer;
    }

    /**
     * Stops the game and ends the current session.
     */
    public void stopGame() {
        gameManager.endGame();
    }

}
