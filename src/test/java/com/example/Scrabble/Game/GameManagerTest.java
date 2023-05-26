package com.example.Scrabble.Game;

import org.junit.jupiter.api.Test;

import com.example.Scrabble.Model.GuestPlayer;
import com.example.Scrabble.Model.HostPlayer;
import com.example.Scrabble.Model.Game.GameManager;

import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;

public class GameManagerTest {

    GameManager gameManager = GameManager.get();
    HostPlayer hostPlayer = HostPlayer.get(new GuestPlayer("test", 1, "localhost"));

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
    public void testAddPlayer() {
        System.out.println("Test addPlayer");
        gameManager.addPlayer(new GuestPlayer("test", 1, "localhost"));
        gameManager.printPlayers();
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
        gameManager.printPlayers();
    }

}