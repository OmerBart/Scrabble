package com.example.Scrabble.Model;

import com.example.Scrabble.Game.GameManager;
import com.example.Scrabble.Model.Command;
import com.example.Scrabble.Model.CommandFactory;
import com.example.Scrabble.ScrabbleServer.ClientHandler;

import java.io.*;

public class PlayerHandler implements ClientHandler {

    private GameManager GM;
    private BufferedReader in;
    private PrintWriter out;
    private CommandFactory commandFactory;

    public PlayerHandler() {
        GM = GameManager.get();
        commandFactory = new CommandFactory();
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            String s;
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String line = in.readLine();
            Command command = commandFactory.createCommand(line);
            if (command != null) {
                String result = command.execute();
                out.println(result);
            } else {
                out.println("Invalid command");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}