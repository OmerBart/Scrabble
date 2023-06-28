package com.example.Scrabble.Model.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public GuestPlayer(Player player) {
        this.name = player.getName().split(":")[0];
        this.playerID = player.getPlayerID();
    }

    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }

    public GuestPlayer(String name, String serverAddress) {
        this.name = name;
        this.serverAddress = serverAddress;
    }

    public GuestPlayer(String name) {
        this.name = name;
        this.playerID = 0;
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

    public void setSocket(Socket socket) {
        this.serverSocket = socket;
    }

    public String joinGame() {
        String response;
        openSocketIfClosed();
        response = sendRequestToServer("joinGame," + name + ":" + playerID);
        setID(Integer.parseInt(response.split(":")[1].trim()));
        setTurn(false); // Set everyone's turn to false until the game starts
        startListeningToServer();
        return response;
    }

    public int getScore() {
        openSocketIfClosed();
        return Integer.parseInt(sendRequestToServer("getScore:" + name + ":" + playerID));
    }

    public String getPlayerList() {
        return sendRequestToServer("getPlayerList");
    }

    public int getNumberOfPlayers() {
        String response = getPlayerList();
        // System.out.println("getNumberOfPlayers:: response: " + response);
        return response.split(",").length;
    }

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

    public String placeWord(Character[] word, int x, int y, boolean isHorizontal) {
        openSocketIfClosed();
        String sWord = "";
        for (Character c : word) {
            sWord += c == null ? "_" : c;
        }
        System.out.println("placeWord:" + name + ":" + playerID + ":" + sWord + ":" + x + ":" + y + ":" + isHorizontal);
        return sendRequestToServer(
                "placeWord:" + name + ":" + playerID + ":" + sWord + ":" + x + ":" + y + ":" + isHorizontal);
    }

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

    private String sendRequestToServer(String request) {
        try {
            openSocketIfClosed();
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    public String printTiles() {
        return sendRequestToServer("printTiles," + name + ":" + playerID);
    }

    public String getCurrentBoard() {
        return sendRequestToServer("boardState");
    }

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
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading server update: " + e.getMessage());
        }
    }

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

    public boolean queryIO(String request) {
        return Boolean.parseBoolean(sendRequestToServer("Q:" + request));
    }

    public boolean challengeIO(String request) {
        return Boolean.parseBoolean(sendRequestToServer("C:" + request));
    }

    private void setTurn(boolean turn) {
        isMyTurn = turn;
        setListening(!turn);
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public String startGame() {
        if (this instanceof HostPlayer)
            setTurn(true); // Host is the first player and starts the game
        return sendRequestToServer("startGame," + name + ":" + playerID);
    }

    @Override
    public String toString() {
        return name + ":" + playerID;
    }

    @Override
    public boolean endTurn() {
        sendRequestToServer("endTurn" + name + ":" + playerID);
        setTurn(false);
        startListeningToServer();
        return true;
    }

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
