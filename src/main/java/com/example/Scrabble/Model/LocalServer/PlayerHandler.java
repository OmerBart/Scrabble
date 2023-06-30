package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;
import java.net.Socket;

/**
 * The `PlayerHandler` class handles communication with a player connected to the game server.
 * It implements the `ClientHandler` interface and is responsible for receiving commands from the player,
 * executing them, and sending back the results.
 */
public class PlayerHandler implements ClientHandler {
    private BufferedReader reader;
    private PrintWriter writer;
    private CommandFactory commandFactory;
    private Socket clientSocket;
    private boolean alive;

    /**
     * Constructs a new `PlayerHandler` object.
     * It initializes the `CommandFactory` for creating command instances.
     */
    public PlayerHandler() {
        commandFactory = new CommandFactory();
    }

    /**
     * Constructs a new `PlayerHandler` object with the specified client socket.
     * It initializes the `CommandFactory`, sets the client socket, and sets the alive flag to true.
     *
     * @param client The client socket associated with the player.
     */
    public PlayerHandler(Socket client) {
        this();
        this.clientSocket = client;
        this.alive = true;
    }

    /**
     * Handles the client's input and output streams.
     * It reads commands from the player, executes them, and sends back the results.
     *
     * @param inFromClient  The input stream from the client.
     * @param outToClient   The output stream to the client.
     */
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

    /**
     * Sends a message to the player.
     *
     * @param msg The message to send.
     */
    public void sendMsg(String msg) {
        if (writer != null) {
            writer.println(msg);
            writer.flush();
        }
    }

    /**
     * Closes the player handler and releases associated resources.
     * It sets the alive flag to false and closes the reader, writer, and client socket if they are not null.
     */
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

    /**
     * Checks if the player handler is closed.
     *
     * @return true if the player handler is closed, false otherwise.
     */
    public boolean isClosed() {
        return reader == null && writer == null && clientSocket == null;
    }
}
