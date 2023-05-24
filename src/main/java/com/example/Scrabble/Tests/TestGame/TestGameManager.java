package com.example.Scrabble.Tests.TestGame;

import com.example.Scrabble.Game.GameManager;

import com.example.Scrabble.Model.GuestPlayer;
import com.example.Scrabble.Model.HostPlayer;

// import java.util.Random;

public class TestGameManager {

    public static void main(String[] args) {
        HostPlayer host = new HostPlayer(new GuestPlayer("TheHost", 1));
        GuestPlayer guest = new GuestPlayer("TheGuest", 2);
        GuestPlayer guest2 = new GuestPlayer("TheGuest2", 3);
        GuestPlayer guest3 = new GuestPlayer("TheGuest3", 4);
        GuestPlayer guest4 = new GuestPlayer("TheGuest4", 5);
        GameManager GM = GameManager.get();

        // TEST 1: addPlayer() and setHost()
        System.out.println("TEST 1: addPlayer() and setHost()");
        GM.setHost(host.getHostgameServer(), host);
        System.out.println(GM.addPlayer(host));

        // TEST 2: getGameBoard()
        System.out.println("TEST 2: getGameBoard()");
        System.out.println(GM.getGameBoard());

        // TEST 3: getTilefromBag()
        System.out.println("TEST 3:  getTilefromBag()");
        System.out.println(GM.getTilefromBag());

        // TEST 4: printPlayers()
        System.out.println("TEST 4: printPlayers()");
        System.out.println(GM.printPlayers());

        // TEST 5: PrintPlayers()
        System.out.println("TEST 5: PrintPlaters()");
        System.out.println(GM.printPlayers());
    }
}