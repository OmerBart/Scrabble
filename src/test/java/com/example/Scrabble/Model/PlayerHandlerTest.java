package com.example.Scrabble.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerHandlerTest {

    @Test
    public void testPlayerHandler() {
        System.out.println("Test PlayerHandler constructor");
        PlayerHandler playerHandler = new PlayerHandler();
        assertNotNull(playerHandler);
    }

    @Test
    public void testHandleClient() {
        System.out.println("Test handleClient");
        PlayerHandler playerHandler = new PlayerHandler();
        playerHandler.handleClient(null, null);
    }

    @Test
    public void testClose() {
        System.out.println("Test close");
        PlayerHandler playerHandler = new PlayerHandler();
        playerHandler.close();
    }

}
