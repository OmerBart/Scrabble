package com.example.Scrabble.Model;

import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GuestPlayer implements Player {
    private String name;
    private int playerID;
    private String serverAddress; // format "ip:port"
    private Socket serverSocket;
    private List<String> playerTiles;


    public GuestPlayer() {
    }
    public String startGame() {
        openSocketIfClosed();
        return sendRequestToServer("startGame:" + name + ":" + playerID);
    }

    public GuestPlayer(Player player) {
        this.name = player.getName();
        this.playerID = player.getPlayerID();
    }
    public GuestPlayer(Player player, String serverAddress) {
        this.name = name;
        this.serverAddress = serverAddress;
    }

    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }
    public GuestPlayer(String name, int playerID, String serverAddress) {
        this.name = name;
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
        return name + ":" + playerID;
    }

    @Override
    public int getPlayerID() {
        return playerID;
    }

    public String joinGame() {
        openSocketIfClosed();
        return sendRequestToServer("joinGame:" + name + ":" + playerID);

    }
    public int getScore(){
        openSocketIfClosed();
        return Integer.parseInt(sendRequestToServer("getScore:"+name+":"+playerID));
    }

    public String getTile() {
        openSocketIfClosed();
        if(playerTiles == null)
            playerTiles = new ArrayList<>();
        String tile = sendRequestToServer("getTile:"+name+":"+playerID);
        playerTiles.add(tile);
        return tile;
    }
    public String placeWord(String word, int x, int y, boolean isHorizontal){
        openSocketIfClosed();
        return sendRequestToServer("placeWord:"+name+":"+playerID+":"+word+":"+x+":"+y+":"+isHorizontal);
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
        try {
            // Only open a new socket connection if it's closed or null
            if (serverSocket == null || serverSocket.isClosed()) {
                serverSocket = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
            }
        } catch (IOException e) {
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
    public String printTiles(){
        StringBuilder tiles = new StringBuilder();
        if(playerTiles == null)
            return "";
           else {
            for (String tile : playerTiles) {
                tiles.append(tile).append(" ");
            }
            return tiles.toString();
        }
    }
    public boolean queryIO(String... Args){
        openSocketIfClosed();
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("Q," + request));
    }

    @Override
    public String toString() {
        return "GuestPlayer|" + name + "|" + playerID + "|" + serverAddress + "|";
    }
}
