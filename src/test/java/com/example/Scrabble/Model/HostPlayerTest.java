package com.example.Scrabble.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostPlayerTest {

    HostPlayer hostPlayer;

    @BeforeEach
    void setUp() {
        hostPlayer = new HostPlayer(new GuestPlayer("test", 1, "localhost:8080"));
        System.out.println("HostPlayerTest: setUp()");
    }

    @AfterEach
    void tearDown() {
        hostPlayer = null;
        System.out.println("HostPlayerTest: tearDown()");
    }

    @Test
    void getHostgameServer() {
        assertNotNull(hostPlayer.getHostgameServer());
    }

    @Test
    void stopGame() {
        hostPlayer.stopGame();
        assertEquals(true, hostPlayer.getHostgameServer().isStop());
    }

    @Test
    void testToString() {
        assertEquals("HostPlayer|test|1|localhost:", hostPlayer.toString().substring(0, 28));
    }
}