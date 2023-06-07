package com.example.Scrabble.Model.Player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// import com.example.Scrabble.Model.Game.Tile;

public class GuestPlayer implements Player {
    private StringProperty name;
    private int playerID;
    private String serverAddress; // format "ip:port"
    private Socket serverSocket;
    private List<String> playerTiles;

    public GuestPlayer(Player player) {
        this.name = new SimpleStringProperty();
        this.name.set(player.getName().split(":")[0]);
        this.playerID = player.getPlayerID();
    }

    public GuestPlayer(String name, int playerID) {
        this.name = new SimpleStringProperty();
        this.name.set(name);
        this.playerID = playerID;
    }

    public GuestPlayer(String name, int playerID, String serverAddress) {
        this.name = new SimpleStringProperty();
        this.name.set(name);
        this.playerID = playerID;
        this.serverAddress = serverAddress;
    }

    public void setServerAddress(String serverAddress, int port) {
        this.serverAddress = serverAddress + ":" + port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getName() {
        return name.get() + ":" + playerID;
    }

    @Override
    public int getPlayerID() {
        return playerID;
    }

    public String joinGame() {
        openSocketIfClosed();
        return sendRequestToServer("joinGame," + name.get() + ":" + playerID);
    }

    public int getScore() {
        openSocketIfClosed();
        return Integer.parseInt(sendRequestToServer("getScore:" + name.get() + ":" + playerID));
    }

    public String getTile() {
        openSocketIfClosed();
        if (playerTiles == null)
            playerTiles = new ArrayList<>();
        String tile = sendRequestToServer("getTile:" + name.get() + ":" + playerID);
        playerTiles.add(tile);
        return tile;
    }

    public List<String> getPlayerTiles() {
        return playerTiles;
    }

    public String placeWord(String word, int x, int y, boolean isHorizontal) {
        openSocketIfClosed();
        return sendRequestToServer(
                "placeWord:" + name.get() + ":" + playerID + ":" + word + ":" + x + ":" + y + ":" + isHorizontal);
    }

    public void disconnectFromServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing socket: " + e.getMessage(), e);
            }
        }
    }

    private void openSocketIfClosed() {
        System.out.println("Opening socket if closed");
        try {
            // Only open a new socket connection if it's closed or null
            if (serverSocket == null || serverSocket.isClosed()) {
                System.out.println("socket is null or closed");
                serverSocket = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
            }
        } catch (IOException e) {
            System.out.println("Error opening socket: " + e.getMessage());
            throw new RuntimeException("Error opening socket: " + e.getMessage(), e);
        }
    }

    private String sendRequestToServer(String request) {
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            Scanner in = new Scanner(serverSocket.getInputStream());

            // Send the request to the server

            out.println(request);
            out.flush();

            // Receive the response from the server
            String res = in.nextLine();
            in.close();
            out.close();
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    // public String printTiles(){
    // StringBuilder tiles = new StringBuilder();
    // if(playerTiles == null)
    // return "";
    // else {
    // for (String tile : playerTiles) {
    // tiles.append(tile).append(" ");
    // }
    // return tiles.toString();
    // }
    // }
    public String printTiles() {
        openSocketIfClosed();
        return sendRequestToServer("printTiles," + name.get() + ":" + playerID);
    }

    public boolean queryIO(String... Args) {
        openSocketIfClosed();
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("Q," + request));
    }

    public boolean challangeIO(String... Args) {
        openSocketIfClosed();
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("C," + request));
    }

    public boolean isMyTurn() {
        openSocketIfClosed();
        return Boolean.parseBoolean(sendRequestToServer("isMyTurn," + name.get() + ":" + playerID));
    }

    public String startGame() {
        openSocketIfClosed();
        return sendRequestToServer("startGame," + name.get() + ":" + playerID);
    }

    @Override
    public String toString() {
        return name.get() + ":" + playerID;
    }

    @Override
    public boolean endTurn() {
        openSocketIfClosed();
        return Boolean.parseBoolean(sendRequestToServer("endTurn" + name.get() + ":" + playerID));
    }

}
