package com.example.Scrabble.Model;

import com.example.Scrabble.Game.GameManager;
import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.Random;

public class HostPlayer extends GuestPlayer{

    private final MyServer HostgameServer;
    private GameManager GM;





    public HostPlayer(Player player) {
        super(player);

        Random r = new Random();
        int port = 6000 + r.nextInt(6000);
        HostgameServer = new MyServer(port, new PlayerHandler());
        setServerAddress("localhost", port);
        HostgameServer.start();
        GM = GameManager.get();
        GM.setHost(HostgameServer, this);
    }
    public MyServer getHostgameServer() {
        return HostgameServer;
    }
    public void stopGame(){
        GM.stopGame();
        HostgameServer.close();
    }

    @Override
    public String toString() {
        return "HostPlayer|"+getName()+"|"+getPlayerID()+"|"+getServerAddress()+"|" ;
    }


}
