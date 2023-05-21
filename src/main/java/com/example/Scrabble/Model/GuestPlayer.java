package com.example.Scrabble.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GuestPlayer implements Player {
    private String name;
    private int playerID;
    private String serverAddress; // format "ip:port"
    private Socket serverSocket;

    public GuestPlayer() {
    }

    public GuestPlayer(Player player) {
        this.name = player.getName();
        this.playerID = player.getPlayerID();
    }

    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
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
        return sendRequestToServer("Join:" + name + ":" + playerID);
    }

    public String getTile() {
        openSocketIfClosed();
        return sendRequestToServer("GetTile:" + name + ":" + playerID);
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
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "GuestPlayer|" + name + "|" + playerID + "|" + serverAddress + "|";
    }
}
