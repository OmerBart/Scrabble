package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.Objects;
import java.util.Random;

public class HostPlayer extends GuestPlayer{

    private final MyServer gameServer;





    public HostPlayer(Player player) {
        super(player);
        Random r = new Random();
        int port = 6000 + r.nextInt(1000);
        gameServer = new MyServer(port, new PlayerHandler());
        setServerAddress("localhost", port);
        gameServer.start();
    }
    public void stopGame(){
        gameServer.close();
    }

    @Override
    public String toString() {
        return "HostPlayer|"+getName()+"|"+getPlayerID()+ "|"+getScore()+"|"+getServerAddress()+"|" ;
    }

}
