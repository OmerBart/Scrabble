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

            guest.setHostServer(host.getHostgameServer());
            guest2.setHostServer(host.getHostgameServer());
            guest3.setHostServer(host.getHostgameServer());
            guest4.setHostServer(host.getHostgameServer());
            System.out.println("trying to join game...." + guest.joinGame());
//            System.out.println("trying to get tile..... " + guest.getTile()); TODO: fix this




    }


}
