package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class GuestPlayer implements Player{
    private String name;
    private int playerID;
    private String serverAddress; // format "ip:port"
    private Socket serverSocket;


//    public void setHostServer(MyServer hostServer) {
//        this.hostServer = hostServer;
//    }


    private String askServer(String query, Socket serverSocket) {
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            Scanner in = new Scanner(serverSocket.getInputStream());
            out.println(query);
            out.flush();
            String res = in.nextLine();
            //System.out.println(Hostname + " your results are: " + res);
            in.close();
            out.close();
            //serverSocket.close();
            return res;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public GuestPlayer(){};

    public GuestPlayer (Player player){
        this.name = player.getName();
        this.playerID = player.getPlayerID();

    }

    public GuestPlayer(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }

    public void setServerAddress(String serverAddress,int port) {
        this.serverAddress = serverAddress+":"+port;
        //System.out.println("server address set to: "+this.serverAddress);
    }
    public void setHost(String hostname, int port){
        setServerAddress(hostname, port);
    }

    public String getServerAddress() {
        return serverAddress;
    }
    @Override
    public String getName() {
        return name + ":" + playerID;
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

    public String joinGame() {
        try {
            if(this.serverSocket == null || this.serverSocket.isClosed())
                this.serverSocket = new Socket(serverAddress.split(":")[0],Integer.parseInt(serverAddress.split(":")[1]));
//            assert serverSocket != null;
            return askServer("Join:"+name+":"+playerID, serverSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


        @Override
    public String toString() {
        return "GuestPlayer|"+name+"|"+playerID+"|"+serverAddress+"|" ;
    }


    public String getTile() {
        try {
            if(this.serverSocket == null || this.serverSocket.isClosed())
                this.serverSocket = new Socket(serverAddress.split(":")[0],Integer.parseInt(serverAddress.split(":")[1]));

            return askServer("GetTile:"+name+":"+playerID, serverSocket);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }
}
