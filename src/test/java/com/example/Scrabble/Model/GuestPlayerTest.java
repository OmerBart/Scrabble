package com.example.Scrabble.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestPlayerTest {
    GuestPlayer guestPlayer;

    @BeforeEach
    void setUp() {
        guestPlayer = new GuestPlayer("test", 1, "localhost:8080");
        System.out.println("GuestPlayer: setUp()");
    }

    @AfterEach
    void tearDown() {
        guestPlayer = null;
        System.out.println("GuestPlayer: tearDown()");
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
    void setServerAddress() {
        guestPlayer.setServerAddress("localhost", 8080);
        assertEquals("localhost:8080", guestPlayer.getServerAddress());
    }

    @Test
    void testToString() {
        assertEquals("test:1", guestPlayer.toString());
    }

}
