package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements ClientHandler {
    private BufferedReader reader;
    private PrintWriter writer;
    private CommandFactory commandFactory;
    private Socket clientSocket;
    private boolean alive;

    public PlayerHandler() {
        commandFactory = new CommandFactory();
    }

    public PlayerHandler(Socket client) {
        this();
        this.clientSocket = client;
        this.alive = true;
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            reader = new BufferedReader(new InputStreamReader(inFromClient));
            writer = new PrintWriter(outToClient, true);
            String line;
            while (alive && (line = reader.readLine()) != null) {
                Command command = commandFactory.createCommand(line);
                if (command != null) {
                    String result = command.execute();
                    writer.println(result);
                    writer.flush();
                } else {
                    writer.println("Invalid command");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if (writer != null) {
            writer.println("update:" + msg);
            writer.flush();
        }
    }

    @Override
    public void close() {
        alive = false;
        try {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            if (clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return reader == null && writer == null && clientSocket == null;
    }
}
