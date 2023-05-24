package com.example.Scrabble.Tests.TestModel;

import com.example.Scrabble.Model.HostPlayer;
import com.example.Scrabble.Model.GuestPlayer;

public class TestHostPlayer {
    public static void main(String[] args) {

        // TEST 1: HostPlayer(Player player) CONSTRUCTOR
        System.out.println("TEST 1: HostPlayer(Player player) CONSTRUCTOR");
        HostPlayer host = new HostPlayer(new GuestPlayer("TheHost", 1));
        System.out.println(host.toString());
        
        // TEST 2: getHostgameServer()
        System.out.println("TEST 2: getHostgameServer()");
        System.out.println(host.getHostgameServer());

        // TEST 3: stopGame()
        System.out.println("TEST 3: stopGame()");
        host.stopGame();
    }

}
