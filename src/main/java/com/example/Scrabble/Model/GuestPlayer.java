package com.example.Scrabble.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class GuestPlayer implements Player{
    private String name;
    private int playerID;
    private int score;
    private String serverAddress; // format "ip:port"

    public void connectTohost(){
        String[] address = this.serverAddress.split(":");
        try {
            System.out.println("Connecting to port " + address[1]);
            Socket server = new Socket(address[0], Integer.parseInt(address[1]));
            String connectMessage = "CONNECT|"+this.getName()+"|"+this.getPlayerID();
            askServer(connectMessage, server);

        } catch (IOException e) {
            System.out.println("IOException in runGame");
        }

    }
    private void askServer(String query, Socket serverSocket) throws IOException {
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream());
        Scanner in = new Scanner(serverSocket.getInputStream());
        out.println(query);
        out.flush();
        String res = in.next();
        //System.out.println(Hostname + " your results are: " + res);
        in.close();
        out.close();
        serverSocket.close();
    }

    public GuestPlayer(){};

    public GuestPlayer (Player player){
        this.name = player.getName();
        this.playerID = player.getPlayerID();
        this.score = player.getScore();
    }

    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
        this.score = 0;
    }

    public void setServerAddress(String serverAddress,int port) {
        this.serverAddress = serverAddress+":"+port;
        System.out.println("server address set to: "+this.serverAddress);
    }

    public String getServerAddress() {
        return serverAddress;
    }
    @Override
    public String getName() {
        return name + " " + playerID;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getPlayerID() {
        return playerID;
    }

    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }


        @Override
    public String toString() {
        return "GuestPlayer|"+name+"|"+playerID+ "|"+score+"|"+serverAddress+"|" ;
    }


}
