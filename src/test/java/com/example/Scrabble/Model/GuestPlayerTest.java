package com.example.Scrabble.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;

public class GuestPlayerTest {
    static GuestPlayer guestPlayer;
    static HostPlayer hostPlayer;

    @BeforeAll
    static void setUpAll() {
        System.out.println("GuestPlayerTest.setUpAll() 1");
        hostPlayer = HostPlayer.get(new GuestPlayer("test", 1));
        guestPlayer = new GuestPlayer("test", 1, "localhost:8080");
        guestPlayer.setServerAddress("localhost", 65432);
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("GuestPlayerTest.tearDownAll() 9");
        hostPlayer.stopGame();
    }

    @Test
    void setServerAddress() {
        System.out.println("guestPlayer.setServerAddress() 2");
        guestPlayer.setServerAddress("localhost", 65432);
        assertEquals("localhost:65432", guestPlayer.getServerAddress());
    }

    @Test
    void getPlayerID() {
        System.out.println("guestPlayer.getPlayerID() 3");
        assertEquals(1, guestPlayer.getPlayerID());
    }

    @Test
    void getName() {
        System.out.println("guestPlayer.getName() 4");
        assertEquals("test:1", guestPlayer.getName());
    }

    @Test
    void getServerAddress() {
        System.out.println("guestPlayer.getServerAddress() 5");
        assertEquals("localhost:", guestPlayer.getServerAddress().substring(0, 10));
    }

    @Test
    void testToString() {
        System.out.println("guestPlayer.toString() 6");
        assertEquals("test:1", guestPlayer.toString());
    }

    @Test
    void joinGame() {
        System.out.println("guestPlayer.joinGame() 7");
        assertEquals("Player added to game successfully", guestPlayer.joinGame());
        hostPlayer.startGame();
    }

    @Test
    void getScore() {
        System.out.println("guestPlayer.getScore() 8");
        System.out.println(guestPlayer.getScore());
        assertEquals(10, guestPlayer.getScore());
    }

    @Test
    void getTile() {
        System.out.println("guestPlayer.getTile() 9");
        guestPlayer.getTile();
        assertEquals(1, guestPlayer.getPlayerTiles().size());
    }

}