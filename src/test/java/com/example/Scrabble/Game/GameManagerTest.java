package com.example.Scrabble.Game;

import org.junit.jupiter.api.Test;

import com.example.Scrabble.Model.GuestPlayer;
import com.example.Scrabble.Model.HostPlayer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class GameManagerTest {

    GameManager gameManager;
    HostPlayer hostPlayer;

    @BeforeEach
    public void setUp() {
        hostPlayer = new HostPlayer(new GuestPlayer("test", 1, "localhost"));
        gameManager = GameManager.get();
        gameManager.setHost(hostPlayer.getHostgameServer(), hostPlayer);
    }

    @AfterEach
    public void tearDown() {
        gameManager.reset();
    }

    @Test
    public void testGameManager() {
        System.out.println("Test GameManager constructor");
        assertNotNull(gameManager);
    }

    @Test
    public void testGet() {
        System.out.println("Test if GameManager is a singleton");
        GameManager gameManager1 = GameManager.get();
        GameManager gameManager2 = GameManager.get();
        assertEquals(gameManager1, gameManager2);
    }

    @Test
    public void testSetHost() {
        System.out.println("Test setHost");
        gameManager.setHost(hostPlayer.getHostgameServer(), hostPlayer);
        assertNotNull(gameManager.hostServer);
        // need to test if the hostPlayer is added to the playerList @Omer add
        // getPlayerList()
    }

    @Test
    public void testAddPlayer() {
        System.out.println("Test addPlayer");
        gameManager.addPlayer(new GuestPlayer("test", 1, "localhost"));
        // need to test if the player is added to the playerList @Omer add
        // getPlayerList()
    }

    @Test
    public void testGetGameBoard() {
        System.out.println("Test getGameBoard");
        assertNotNull(gameManager.getGameBoard());
    }

    @Test
    public void testStartGame() {
        System.out.println("Test startGame");
        // add some players to the playerList
        gameManager.addPlayer(new GuestPlayer("test1", 1, "localhost"));
        gameManager.addPlayer(new GuestPlayer("test2", 2, "localhost"));
        gameManager.addPlayer(new GuestPlayer("test3", 3, "localhost"));

        gameManager.startGame("test1");

        // check if players got their tiles
    }
    
}