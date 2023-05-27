package com.example.Scrabble.Model;

import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import org.junit.jupiter.api.Test;

import com.example.Scrabble.ServerUtils.MyServer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;

public class PlayerHandlerTest {

    static PlayerHandler playerHandler;

    @BeforeAll
    static void setUpAll() {
        playerHandler = new PlayerHandler();
    }

    public void testPlayerHandler() {
        assertNotNull(playerHandler);
    }

    @Test
    public void testHandleClient() {
        try {
            MyServer server = new MyServer(5050, playerHandler);
            server.start();
            Socket socket = new Socket("localhost", server.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            out.println("joinGame,test:1");
            out.flush();
            String result = in.nextLine();
            assertEquals("Player added to game successfully", result);
            socket.close();
            server.close();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    @Test
    public void testClose() {
        assertTrue(playerHandler.isClosed());
    }

}
