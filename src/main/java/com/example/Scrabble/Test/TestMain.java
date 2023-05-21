package com.example.Scrabble.Test;


import com.example.Scrabble.Model.GameManager;
import com.example.Scrabble.Model.GuestPlayer;
import com.example.Scrabble.Model.Player;
import com.example.Scrabble.tmp.HostModel;
import com.example.Scrabble.Model.HostPlayer;

// import java.util.Random;

public class TestMain {

    public static void main(String[] args) {
            HostPlayer host = new HostPlayer(new GuestPlayer("TheHost", 1));
            GuestPlayer guest = new GuestPlayer("TheGuest", 2);
            GuestPlayer guest2 = new GuestPlayer("TheGuest2", 3);
            GuestPlayer guest3 = new GuestPlayer("TheGuest3", 4);
            GuestPlayer guest4 = new GuestPlayer("TheGuest4", 5);
            GameManager GM = GameManager.get();

            guest.setServerAddress("localhost",host.getHostgameServer().getPort());
            guest2.setServerAddress("localhost",host.getHostgameServer().getPort());
            guest3.setServerAddress("localhost",host.getHostgameServer().getPort());
            guest4.setServerAddress("localhost",host.getHostgameServer().getPort());
            System.out.println("trying to join game...." + guest.joinGame());
            System.out.println("trying to join game...." + guest2.joinGame());
            System.out.println("trying to join game...." + guest3.joinGame());
            System.out.println("trying to join game...." + guest4.joinGame());



            System.out.println("giving each player 7 tiles");
            for(int i = 0; i<7;i++){
                guest.getTile();
                guest2.getTile();
                guest3.getTile();
                guest4.getTile();
            }



            System.out.println(GM.printPlayers());
            host.stopGame();
            guest.disconnectFromServer();
            guest2.disconnectFromServer();
            guest3.disconnectFromServer();
            guest4.disconnectFromServer();

            System.out.println("Done!");



    }


}
