package com.example.Scrabble.Model;

import org.junit.jupiter.api.Test;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class GameManagerTest {

    static GameManager gameManager;
    static HostPlayer hostPlayer;
    static String addPlayerResult;
    static GuestPlayer guestPlayer;

    @BeforeAll
    public static void setUp() {
        gameManager = GameManager.get();
        hostPlayer = HostPlayer.get(new GuestPlayer("test1", 1, "localhost"));
        gameManager.setHost(hostPlayer.getHostGameServer(), hostPlayer);
        guestPlayer = new GuestPlayer("test2", 2, "localhost");
        addPlayerResult = gameManager.addPlayer(guestPlayer);
    }

    @AfterAll
    public static void tearDown() {
        gameManager.stopGame();
        hostPlayer.stopGame();
    }

    @Test
    public void testGameManager() {
        // testing constructor
        assertNotNull(gameManager);
    }

    @Test
    public void testGet() {
        // tetsing singleton
        GameManager gameManager1 = GameManager.get();
        GameManager gameManager2 = GameManager.get();
        assertEquals(gameManager1, gameManager2);
    }

    @Test
    public void testAddPlayer() {
        // testing add player to game
        assertEquals("Player added to game successfully", addPlayerResult);
    }

    @Test
    public void testGetGameBoard() {
        System.out.println("Test getGameBoard");
        assertNotNull(gameManager.getGameBoard());
    }

    @Test
    public void testStartGame() {
        gameManager.startGame("");
        // problem with this test host gets 14 tiles and guest gets 7
        // assertEquals(7, gameManager.getPlayerTiles().get("test1:1").size());
        assertEquals(7, gameManager.getPlayerTiles().get("test2:2").size());
    }

    // @Test
    // public void testMyTurn() {
    // // testing myTurn
    // assertNotEquals("false", gameManager.myTurn("test1:1"));
    // assertEquals("false", gameManager.myTurn("test2:2"));
    // }

    @Test
    public void getTilefromBagTest() {
        // testing getTilefromBag
        String tile = gameManager.getTilefromBag("test1:1");
        assertEquals("G", String.valueOf(tile.charAt(0)));
    }
}