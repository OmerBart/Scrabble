package com.example.Scrabble.Model.Player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuestPlayer implements Player {
    private StringProperty name;
    private int playerID;
    private String serverAddress; // format "ip:port"
    private Socket serverSocket;
    private List<String> playerTiles;
    private boolean listening;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listeningThread;

    public GuestPlayer(Player player) {
        this.name = new SimpleStringProperty();
        this.name.set(player.getName().split(":")[0]);
        this.playerID = player.getPlayerID();
    }

    public GuestPlayer(String name, int playerID) {
        this.name = new SimpleStringProperty(name);
        this.playerID = playerID;
    }

    public GuestPlayer(String name, int playerID, String serverAddress) {
        this.name = new SimpleStringProperty(name);
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

    public void setSocket(Socket socket) {
        this.serverSocket = socket;
    }

    public String joinGame() {
        String response;
        openSocketIfClosed();

        if (!(this instanceof HostPlayer)) {
            response = sendRequestToServer("joinGame," + name.get() + ":" + playerID);
            if (!isMyTurn()) {
                startListeningToServer();
            }
        } else {
            response = "Connected to server";
        }

        return response;
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
        stopListeningToServer();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                listeningThread.interrupt();
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
                serverSocket.setSoTimeout(5000);
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
        return sendRequestToServer("printTiles," + name.get() + ":" + playerID);
    }

    private void startListeningToServer() {
        listening = true;
        listeningThread = new Thread(() -> {
            Scanner sin = new Scanner(this.in);
            while (sin.hasNextLine() && listening) {
                String response = sin.nextLine();
                System.out.println("Response from server: " + response);
                if (response.contains("true")) {
                    stopListeningToServer();
                    System.out.println("my turn now");
                    break;
                } else if (response.contains("over!")) {
                    sin.close();
                    disconnectFromServer();
                    break;
                }
            }
        });
        listeningThread.start();
    }

    private void stopListeningToServer() {
        listening = false;
    }

    public boolean queryIO(String... Args) {
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("Q," + request));
    }

    public boolean challengeIO(String... Args) {
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("C," + request));
    }

    public boolean isMyTurn() {
        boolean turn = Boolean.parseBoolean(sendRequestToServer("isMyTurn," + name.get() + ":" + playerID));
        if (turn)
            stopListeningToServer();
        return turn;
    }

    public String startGame() {
        return sendRequestToServer("startGame," + name.get() + ":" + playerID);
    }

    @Override
    public String toString() {
        return name.get() + ":" + playerID;
    }

    @Override
    public boolean endTurn() {
        sendRequestToServer("endTurn" + name.get() + ":" + playerID);
        startListeningToServer();
        return true;
    }
}
