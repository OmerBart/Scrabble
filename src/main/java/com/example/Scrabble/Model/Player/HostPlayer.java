package com.example.Scrabble.Model.Player;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

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
    //Alice In Wonderland.txt
    //Frank Herbert_Dune.txt
    //Harry Potter.txt
    //Moby Dick.txt
    //Pg10.txt
    //Shakespeare.txt
    //The Matrix.txt

    private final MyServer hostGameServer;
    private GameManager gameManager;
    private static HostPlayer hostPlayerInstance = null;
    private final String localServerAddress;
    private final String publicServerAddress;



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
        Random r = new Random();
        //int port = 65432;
        int port = 6000 + r.nextInt(6000);
        hostGameServer = new MyServer(port, new PlayerHandler());
        localServerAddress = hostGameServer.getIPAddress() + ":" + port;
        System.out.println("Host IP: " + localServerAddress);
        setServerAddress(localServerAddress, port);
        publicServerAddress = getPublicIPAddress();
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
    public String getLocalServerAddress() {
        return localServerAddress;
    }

    /**
     * Sets the books to be used in the game.
     *
     * @param books The books to be used in the game.
     *              Must be in the format of "search_books/BookName.txt" or "search_books/BookName.txt search_books/BookName2.txt...etc".
     */
    public void setBooks(String... books) {
        String[] booksFullPaths = new String[books.length];
        for(String book : books)
            booksFullPaths[books.length - 1] = "search_books/" + book+".txt";

        //System.out.println("booksFullPaths: " + Arrays.toString(booksFullPaths));
        gameManager.setGameBooks(booksFullPaths);
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
    public String getPublicServerAddress() {
        return publicServerAddress;
    }

    private String getPublicIPAddress(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine(); //you get the IP as a String
            //System.out.println("Public IP Address: " + ip);
            return ip;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error getting Public IP Address";
    }




}
