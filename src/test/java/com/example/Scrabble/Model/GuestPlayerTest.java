package com.example.Scrabble.Model;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;

public class GuestPlayerTest {
    static GuestPlayer guestPlayer;
    static HostPlayer hostPlayer;
    static String joinGameResult;

    @BeforeAll
    static void setUpAll() {
        System.out.println("GuestPlayerTest.setUpAll() 1");
        hostPlayer = HostPlayer.get(new GuestPlayer("test", 1));
        guestPlayer = new GuestPlayer("test", 1, "localhost:8080");
        guestPlayer.setServerAddress("localhost", 65432);
        joinGameResult = guestPlayer.joinGame();
        hostPlayer.startGame();
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("GuestPlayerTest.tearDownAll() --END--");
        hostPlayer.stopGame();
    }

    @Test
    void setServerAddress() {
        assertEquals("localhost:65432", guestPlayer.getServerAddress());
    }

    @Test
    void getPlayerID() {
        assertEquals(1, guestPlayer.getPlayerID());
    }

    @Test
    void getName() {
        assertEquals("test:1", guestPlayer.getName());
    }

    @Test
    void getServerAddress() {
        assertEquals("localhost:", guestPlayer.getServerAddress().substring(0, 10));
    }

    @Test
    void testToString() {
        assertEquals("test:1", guestPlayer.toString());
    }

    @Test
    void joinGame() {
        assertEquals("Player added to game successfully", joinGameResult);
    }

    @Test
    void getScore() {
        assertEquals(10, guestPlayer.getScore());
    }

    @Test
    void getTile() {
        guestPlayer.getTile();
        assertEquals(1, guestPlayer.getPlayerTiles().size());
    }

    @Test
    void queryIOTest() {
        assertEquals(false, guestPlayer.queryIO("s1.txt", "s2.txt", "kaka"));
    }

    @Test
    void queryIOTest2() {
        assertEquals(true, guestPlayer.queryIO("s1.txt", "s2.txt", "18878"));
    }

    @Test
    void challangeIOTest() {
        assertEquals(true, guestPlayer.challengeIO("s1.txt", "s2.txt", "18878"));
    }

    @Test
    void challangeIOTest2() {
        assertEquals(false, guestPlayer.challengeIO("s1.txt", "s2.txt", "kaka"));
    }

}