package com.example.Scrabble.Model.Player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
    boolean listening;
    PrintWriter out = null;
    BufferedReader in = null;
    Thread listeningThread = null;

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
    public void setSocket(Socket socket){
        this.serverSocket = socket;
    }

    public String joinGame() {
        String response;
        try {
            serverSocket = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
            serverSocket.setSoTimeout(5000);
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            //startListeningToServer();

            if(!(this instanceof HostPlayer)) {
                response = sendRequestToServer("joinGame," + name.get() + ":" + playerID);
                startListeningToServer();
            }
            else {
                response = "Connected to server";
                //startListeningToServer();
            }
//            if (!isMyTurn()) {
//                startListeningToServer();
//            }
            //startListeningToServer();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;


//        openSocketIfClosed();
//        new Thread(() -> startListeningToServer()).start();
//        return sendRequestToServer("joinGame," + name.get() + ":" + playerID);
    }

    public int getScore() {
        //openSocketIfClosed();
        return Integer.parseInt(sendRequestToServer("getScore:" + name.get() + ":" + playerID));
    }

    public String getTile() {
        //openSocketIfClosed();
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
        //openSocketIfClosed();
        return sendRequestToServer(
                "placeWord:" + name.get() + ":" + playerID + ":" + word + ":" + x + ":" + y + ":" + isHorizontal);
    }

    public void disconnectFromServer() {
        stopListeningToServer();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                this.listeningThread.stop();
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
            //serverSocket == null || serverSocket.isClosed()
            if ( serverSocket == null || serverSocket.isClosed()) {
                System.out.println("socket is null or closed");
                serverSocket = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
                serverSocket.setSoTimeout(5000);
                out = new PrintWriter(serverSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            }
        } catch (IOException e) {
            System.out.println("Error opening socket: " + e.getMessage());
            throw new RuntimeException("Error opening socket: " + e.getMessage(), e);
        }
    }

    private String sendRequestToServer(String request) {
        try {
            openSocketIfClosed();
//            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
//            Scanner in = new Scanner(serverSocket.getInputStream());

            // Send the request to the server

            out.println(request);
            //out.flush();

            // Receive the response from the server
            String res = in.readLine();
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
       // openSocketIfClosed();
        return sendRequestToServer("printTiles," + name.get() + ":" + playerID);
    }

    private void startListeningToServer() {
        listening = true;

        this.listeningThread = new Thread(() -> {
            openSocketIfClosed();
            while (listening) {
                try {
                    Scanner in = new Scanner(serverSocket.getInputStream());
                    while (in.hasNextLine()) {
                        String response = in.nextLine();
                        System.out.println("Response from server: " + response);
                        if(response.equals("true")){
                            stopListeningToServer();
                            System.out.println("my turn now");
                            //isMyTurn();
                        }
                        else if(response.contains("over!")) {
                            stopListeningToServer();
                            in.close();
                            break;
                        }
                        in.close();
                        //in.close();
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Error listening to server: " + e.getMessage(), e);
                }
            }

        });
        listeningThread.start();
    }

    private void stopListeningToServer() {
        listening = false;
//        if(listeningThread != null)
//            listeningThread.interrupt();

    }

    public boolean queryIO(String... Args) {
       // openSocketIfClosed();
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("Q," + request));
    }

    public boolean challangeIO(String... Args) {
        //openSocketIfClosed();
        String request = String.join(",", Args);
        return Boolean.parseBoolean(sendRequestToServer("C," + request));
    }

    public boolean isMyTurn() {
        //openSocketIfClosed();
        boolean turn = Boolean.parseBoolean(sendRequestToServer("isMyTurn," + name.get() + ":" + playerID));
        if (turn)
            stopListeningToServer();
        return turn;


    }

    public String startGame() {
        //openSocketIfClosed();
        return sendRequestToServer("startGame," + name.get() + ":" + playerID);
    }

    @Override
    public String toString() {
        return name.get() + ":" + playerID;
    }

    @Override
    public boolean endTurn() {
        //openSocketIfClosed();
        sendRequestToServer("endTurn" + name.get() + ":" + playerID);
        startListeningToServer();
        return true;
    }

}

