package com.example.Scrabble.Model.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Scrabble game.
 * Implements the Player interface.
 *
 *  @author Omer Bartfeld
 *  @version 1.0
 */
public class GuestPlayer extends java.util.Observable implements Player {
    private volatile String name;
    private volatile int playerID;
    private volatile String serverAddress; // format "ip:port"
    private volatile Socket serverSocket;
    private volatile List<String> playerTiles;
    private volatile boolean listening;
    private volatile PrintWriter out;
    private volatile BufferedReader in;
    private volatile Thread listeningThread;
    private volatile boolean isMyTurn;

    /**
     * The GuestPlayer function is a constructor that takes in a Player object and creates
     * an instance of GuestPlayer. The name and playerID are set to the values of the Player
     * object passed into it.

     *
     * @param player Get the name and playerid of the player
     *
     * @author Omer Bartfeld
     *
     */
    public GuestPlayer(Player player) {
        this.name = player.getName().split(":")[0];
        this.playerID = player.getPlayerID();
    }

    /**
     * The GuestPlayer function is a constructor for the GuestPlayer class.
     * It takes in two parameters, name and playerID, and sets them to the
     * corresponding instance variables of this class. This function is used
     * when a new guest player logs into the game. The name parameter represents
     * their username, while the playerID parameter represents their unique ID number.  		   	  	    	    	      *
     *
     * @param  name Set the name of the player
     * @param  playerID Identify the player
     *
     * @author Omer Bartfeld
     *
     */
    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }



    /**
     * The GuestPlayer function is a constructor that creates a GuestPlayer object.
     *
     *
     * @param  name Set the name of the player
     * @param  serverAddress Connect to the server
     *
     *
     * @author Omer Bartfeld
     *
     *
     */
    public GuestPlayer(String name, String serverAddress) {
        this.name = name;
        this.serverAddress = serverAddress;
    }

    /**
     * The GuestPlayer function is a constructor for the GuestPlayer class.
     * It takes in a String name and assigns it to the player's name, as well as assigning them an ID of 0.

     *
     * @param  name Set the name of the guestplayer
     *
     *
     * @author Omer Bartfeld
     *
     */
    public GuestPlayer(String name) {
        this.name = name;
        this.playerID = 0;
    }

    /**
     * The setServerAddress function sets the server address to a new value.
     *
     *
     * @param  serverAddress Set the server address
     * @param  port Set the port number for the server
     *
     * @author Omer Bartfeld
     *
     */
    public void setServerAddress(String serverAddress, int port) {
        this.serverAddress = serverAddress + ":" + port;
    }

    /**
     * The getServerAddress function returns the serverAddress variable.
     *
     *
     *
     * @return The serveraddress variable
     * @author Omer Bartfeld
     */
    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getName() {
        return name + ":" + playerID;
    }

    @Override
    public int getPlayerID() {
        return playerID;
    }

    /**    
     * The setSocket function sets the socket for this server.
     *
     *
     * @param  socket Set the serversocket variable to the socket passed in
     *
     *
     *
     * @author Omer Bartfeld
     */
    public void setSocket(Socket socket) {
        this.serverSocket = socket;
    }

    /**
     * The joinGame function is used to join a game.
     * It sends the server a request to join the game, and then starts listening for messages from the server.

     *
     *
     * @return A string of the format &quot;joingame,playerName:ID&quot;
     *
     * @author Omer Bartfeld
     */
    public String joinGame() {
        String response;
        openSocketIfClosed();
        response = sendRequestToServer("joinGame," + name + ":" + playerID);
        setID(Integer.parseInt(response.split(":")[1].trim()));
        setTurn(false); // Set everyone's turn to false until the game starts
        startListeningToServer();
        //System.out.println("Joined game successfully" + getName());
        return response;
    }

    /**
     * The getScore function returns the score of a player.
     *
     *
     *
     * @return The score of the player
     *
     * @author Omer Bartfeld
     */
    public int getScore() {
        openSocketIfClosed();
        return Integer.parseInt(sendRequestToServer("getScore:" + name + ":" + playerID));
    }



    /**
     * The getNumberOfPlayers function returns the number of players currently in the game.
     *
     *
     *
     * @return The number of players in the game
     *
     * @author Omer Bartfeld
     */
    public int getNumberOfPlayers() {
        String response = getPlayerList();
        // System.out.println("getNumberOfPlayers:: response: " + response);
        return response.split(",").length;
    }

    /**
     * The getTile function is used to get a random tile from the bag.
     *
     *
     *
     * @return A string, which is a tile
     *
     * @author Omer Bartfeld
     */
    public String getTile() {
        System.out.println("getTile:: guest player");
        openSocketIfClosed();
        if (playerTiles == null)
            playerTiles = new ArrayList<>();
        String tile = sendRequestToServer("getTile:" + name + ":" + playerID);
        playerTiles.add(tile);
        return tile;
    }

    public List<String> getPlayerTiles() {
        return playerTiles;
    }

    /**
     * The placeWord function is used to place a word on the board.
     *
     *
     * @param  word Pass in the word that is being placed
     * @param  x Specify the x coordinate of the first letter in a word
     * @param  y Specify the y coordinate of the first letter in a word
     * @param  isHorizontal Determine whether the word is placed horizontally or vertically
     *
     * @return A string with score of player placed a word successfully, otherwise return an error message
     *
     * @author Omer Bartfeld
     */
    public String placeWord(Character[] word, int x, int y, boolean isHorizontal) {
        openSocketIfClosed();
        String sWord = "";
        for (Character c : word) {
            sWord += c == null ? "_" : c;
        }
        return sendRequestToServer(
                "placeWord:" + name + ":" + playerID + ":" + sWord + ":" + x + ":" + y + ":" + isHorizontal);
    }

    /**
     * The disconnectFromServer function closes the serverSocket and stops listening to the server.
     *
     * @author Omer Bartfeld
     */
    public void disconnectFromServer() {
        stopListeningToServer();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing socket: " + e.getMessage(), e);
            }
        }
    }

    /**
     * The openSocketIfClosed function checks if the socket is closed, and if it is, opens a new one.
     *
     * @author Omer Bartfeld
     */
    private void openSocketIfClosed() {
        if (serverSocket == null || serverSocket.isClosed()) {
            try {
                serverSocket = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
                out = new PrintWriter(serverSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException("Error opening socket: " + e.getMessage(), e);
            }
        }
    }

    /**
     * The sendRequestToServer function sends a request to the server and returns the response.
     *
     *
     * @param  request Send a request to the server
     *
     * @return A string containing the response from the server
     *
     * @author Omer Bartfeld
     */
    private String sendRequestToServer(String request) {
        try {
            openSocketIfClosed();
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    /**
     * The printTiles function prints the tiles in a player's hand.
     *
     *
     * @return A string containing the tiles in player's rack
     *
     * @author Omer Bartfeld
     */
    public String printTiles() {
        return sendRequestToServer("printTiles," + name + ":" + playerID);
    }

    /**
     * The getCurrentBoard function returns the current state of the board as a string.
     *
     *
     * @return A string of the current board state
     *
     * @author Omer Bartfeld
     */
    public String getCurrentBoard() {
        return sendRequestToServer("boardState");
    }

    /**
     * The startListeningToServer function is used to start a thread that listens for messages from the server.
     * The function opens a socket if it is closed, and then creates a BufferedReader object called listenerIn.
     * It then starts the listeningThread thread.
     *
     * @author Omer Bartfeld
     */
    private void startListeningToServer() {
        try {
            openSocketIfClosed();
            BufferedReader listenerIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            listeningThread = new Thread(() -> listeningToServer(listenerIn));
            listeningThread.start();
        } catch (IOException e) {
            throw new RuntimeException("Error setting up listening thread: " + e.getMessage(), e);
        }
    }

    /**
     * The listeningToServer function is a helper function that listens to the server for updates.
     * It will notify all observers of any changes in the game state, and it will also set turn to true if it receives a message from the server saying that it's this client's turn.
     *
     * @param  listenerIn BufferedReader object that is used to read messages from the server
     *
     * @author Omer Bartfeld
     */
    private void listeningToServer(BufferedReader listenerIn) {
        try {
            while (listening) {
                if (listenerIn.ready()) {
                    String response = listenerIn.readLine();
                    if (response != null) {
                        System.out.println("Got update from server: " + response);
                        setChanged();
                        notifyObservers(response); // create an event and notify the observers
                        clearChanged();
                        if(response.contains("T:true"))
                            setTurn(true);
                    }

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading server update: " + e.getMessage());
        }
    }

    /**
     * The getPlayerList function returns a string containing the names of all players currently in the game.
     *
     *
     * @return A string containing the names of all players currently in the game:
     *
     * @author Omer Bartfeld
     */
    public String getPlayerList() {
        return sendRequestToServer("getPlayerList");
    }

    /**
     * The stopListeningToServer function stops the listening thread.
     *
     * @author Omer Bartfeld
     */
    private void stopListeningToServer() {
        listening = false;
        if (listeningThread != null) {
            try {
                listeningThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error stopping listening thread: " + e.getMessage());
            }
        }
    }


    /**
     * The queryDictionaryServer function sends a request to the server and returns
     * whether or not the word is in the dictionary.
     *
     *
     * @param  request Send a request to the server
     *
     * @return A boolean value
     * @author Omer Bartfeld
     *
     */
    public boolean queryDictionaryServer(String request) {
        return Boolean.parseBoolean(sendRequestToServer("Q:" + request));
    }

//    public boolean challengeIO(String request) {
//        return Boolean.parseBoolean(sendRequestToServer("C:" + request));
//    }

    /**
     * The setTurn function is used to set the turn of the player.
     *
     * @param  turn Set the ismyturn variable to true or false
     *
     * @author Omer Bartfeld
     *
     */
    private void setTurn(boolean turn) {
        isMyTurn = turn;
        setListening(!turn);
    }

    /**
     * The isMyTurn function returns a boolean value that indicates whether or not it is the player's turn.
     *
     * @return A boolean value
     * @author Omer Bartfeld
     *
     */
    public boolean isMyTurn() {
        return isMyTurn;
    }

    /**
     * The startGame function is used to start the game.
     *
     * @return On success, returns a string and updates all players that the game started successfully.
     * @author Omer Bartfeld
     *
     */
    public String startGame() {
        if (this instanceof HostPlayer)
            setTurn(true); // Host is the first player and starts the game
        return sendRequestToServer("startGame," + name + ":" + playerID);
    }

    @Override
    public String toString() {
        return name + ":" + playerID;
    }

    /**
     * The endTurn function is used to end the current player's turn. It sends a request to the server to end the turn.
     * Calls the setTurn function to set the turn to false.
     * Starts listening to the server again.
     *
     * @return Always returns true
     * @author Omer Bartfeld
     *
     */
    @Override
    public boolean endTurn() {
        sendRequestToServer("endTurn" + name + ":" + playerID);
        setTurn(false);
        startListeningToServer();
        return true;
    }

    /**
     * The setListening function sets the listening variable to true or false.
     *
     * @param listening Determine whether the app is listening for a voice command or not
     *
     * @author Omer Bartfeld
     *
     */
    private void setListening(boolean listening) {
        this.listening = listening;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GuestPlayer) {
            GuestPlayer player = (GuestPlayer) obj;
            return player.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public void setID(int ID) {
        this.playerID = ID;
    }
}
