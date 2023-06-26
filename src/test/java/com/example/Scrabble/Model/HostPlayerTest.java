package com.example.Scrabble.Model;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class HostPlayerTest {

    static HostPlayer hostPlayer;

    @BeforeAll
    static void setUpAll() {
        hostPlayer = HostPlayer.get(new GuestPlayer("test.txt", 1));
        hostPlayer.startGame();
    }

    @AfterAll
    static void tearDownAll() {
        hostPlayer.stopGame();
    }

    @Test
    void getHostgameServer() {
        System.out.println("HostPlayerTest: getHostgameServer()");
        assertNotNull(hostPlayer.getHostGameServer());
    }

    @Test
    void stopGame() {
        System.out.println("HostPlayerTest: stopGame()");
        hostPlayer.stopGame();
        assertEquals(true, hostPlayer.getHostGameServer().isStop());
    }

    @Test
    void testToString() {
        System.out.println("HostPlayerTest: testToString()");
        assertEquals("test.txt:1", hostPlayer.toString());
    }
}