package com.example.Scrabble.Tests.TestModel;

import com.example.Scrabble.Model.GuestPlayer;


public class TestGuestPlayer {

    public static void main(String[] args) {

        // TEST 1: GuestPlayer() CONSTRUCTOR
        System.out.println("TEST 1: GuestPlayer() CONSTRUCTOR");
        GuestPlayer guest = new GuestPlayer();
        System.out.println(guest.toString());

        // TEST 2: GuestPlayer(Player player) CONSTRUCTOR
        System.out.println("TEST 2: GuestPlayer(Player player) CONSTRUCTOR");
        GuestPlayer guest2 = new GuestPlayer(guest);
        System.out.println(guest2.toString());

        // TEST 3: GuestPlayer(String name, int playerID) CONSTRUCTOR
        System.out.println("TEST 3: GuestPlayer(String name, int playerID) CONSTRUCTOR");
        GuestPlayer guest3 = new GuestPlayer("Guest3", 3);
        System.out.println(guest3.toString());

        // TEST 4: setServerAddress(String serverAddress, int port)
        System.out.println("TEST 4: setServerAddress(String serverAddress, int port)");
        guest3.setServerAddress("localhost", 5000);
        System.out.println(guest3.toString());

        // TEST 5: getServerAddress()
        System.out.println("TEST 5: getServerAddress()");
        System.out.println(guest3.getServerAddress());
        
        // TEST 6: getName()
        System.out.println("TEST 6: getName()");
        System.out.println(guest3.getName());

        // TEST 7: getPlayerID()
        System.out.println("TEST 7: getPlayerID()");
        System.out.println(guest3.getPlayerID());

        // TEST 8: joinGame()
        System.out.println("TEST 8: joinGame()");
        System.out.println(guest3.joinGame());

        


    }
}
