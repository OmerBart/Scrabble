package com.example.Scrabble.Model.Player;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ServerUtils.ClientHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

import java.io.IOException;
import java.net.Socket;

// import java.util.Random;

public class HostPlayer extends GuestPlayer {

    private final MyServer HostgameServer;
    private GameManager GM;
    private static HostPlayer hostPlayer_instance = null;

    public static HostPlayer get(Player player) {
        if (hostPlayer_instance == null)
            hostPlayer_instance = new HostPlayer(player);
        return hostPlayer_instance;
    }

    private HostPlayer(Player player) {

        super(player);

        // Random r = new Random();
        // int port = 6000 + r.nextInt(6000);
        int port = 65432;
        HostgameServer = new MyServer(port,new PlayerHandler());
        setServerAddress("localhost", port);
        HostgameServer.start();
        GM = GameManager.get();
        GM.setHost(HostgameServer, this);
        joinGame();

    }

    public MyServer getHostgameServer() {
        return HostgameServer;
    }

    public void stopGame() {
        GM.stopGame();
        HostgameServer.close();
    }


    // @Override
    // public String getName() {
    // return this.toString();
    // }

    // @Override
    // public String toString() {
    // return this.getName();
    // }

}
