package com.example.Scrabble.Model;

import com.example.Scrabble.Model.Game.GameManager;
import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.Random;

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

<<<<<<< HEAD
        Random r = new Random();
        // int port = 6000 + r.nextInt(6000);
=======


        //Random r = new Random();
        //int port = 6000 + r.nextInt(6000);
>>>>>>> 7512844badcbaa4010623f9ceb972d4e79210701
        int port = 65432;
        HostgameServer = new MyServer(port, new PlayerHandler());
        setServerAddress("localhost", port);
        HostgameServer.start();
        GM = GameManager.get();
        GM.setHost(HostgameServer, this);
    }

    public MyServer getHostgameServer() {
        return HostgameServer;
    }

    public void stopGame() {
        GM.stopGame();
        HostgameServer.close();
    }
    @Override
    public String getName() {
        return this.toString();
    }
<<<<<<< HEAD
=======

//    @Override
//    public String toString() {
//        return this.getName();
//    }

>>>>>>> 7512844badcbaa4010623f9ceb972d4e79210701
}
