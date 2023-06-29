package com.example.Scrabble.Model.ServerUtils;

import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ScrabbleDictionary.IOserver.BookScrabbleHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The MyServer class represents a server that listens for client connections and handles communication with multiple clients.
 * It provides functionality to start the server, accept client connections, and send messages to connected clients.
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * // Create a client handler
 * ClientHandler clientHandler = new PlayerHandler();
 *
 * // Create a server on port 8080 using the client handler
 * MyServer server = new MyServer(8080, clientHandler);
 *
 * // Start the server
 * server.start();
 * }</pre>
 * </p>
 *
 * @author Omer Bartfeld
 */
public class MyServer {
    private final int port;
    private volatile boolean stop;
    private final Map<String, ClientHandler> clients;
    private final ExecutorService threadPool;
    private final ClientHandler clientHandler;
    private int count;
    private List<String> playerNames;
    //private final Map<String, ClientHandler> playerClientMap;

    /**    
     * The MyServer function creates a new server that listens on the specified port.
     * 
     *
     * @param  port Set the port number for the server
     * @param  clientHandler Pass in the clienthandler object that is created in the main method
     *
     * @author Omer Bartfeld
     */
    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        playerNames = new ArrayList<>();
        this.clientHandler = clientHandler;
        this.clients = new ConcurrentHashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        this.count = 0;
        //this.playerClientMap = new ConcurrentHashMap<>();

    }

    /**    
     * The start function starts the server.
     *
     * @author Omer Bartfeld
     */
    public void start() {
        stop = false;
        new Thread(this::startServer).start();
    }

    /**
     * The startServer function is the main function of the server. It creates a ServerSocket object and sets its timeout to 1000 milliseconds.
     * The while loop runs until stop is set to true, which happens when all clients have disconnected from the server.
     * The try-catch block inside of this while loop attempts to accept a client connection and create an input stream for it, as well as an output stream for it.
     * If there are no clients connected yet, then we add them into our HashMap with their IP address as their key (which we get using getClientKey). We also add them into our ArrayList playerNames so that
     *
     * @author Trelent
     */
    private void startServer() {
        try (ServerSocket server = new ServerSocket(port)) {
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    if (!clients.containsKey(clientKey)) {
                        clients.put(clientKey, createHandler(client));
                        count++;
                        playerNames.add(clientKey);
                    }
                    threadPool.submit(() -> {
                        try {
                            clients.get(clientKey).handleClient(client.getInputStream(), client.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (SocketTimeoutException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**    
     * The getClientKey function returns a string that is the client's IP address and port number.
     * This function is used to identify clients in the HashMap of connected clients.
    
     *
     * @param client Identify the client
     *
     * @return A string that is the ip address and port number of a client
     *
     * @author Omer Bartfeld
     */
    private String getClientKey(Socket client) {
        return client.getInetAddress().getHostAddress() + ":" + client.getPort();
    }

    /**    
     * The createHandler function creates a new ClientHandler object based on the type of clientHandler
     * that is passed in. If the clientHandler is an instance of PlayerHandler, then it will create a new
     * Player Handler with the given socket. If it's an instance of BookScrabble, then it will create a 
     * BookScrabble handler. Otherwise, if neither are true (i.e., if there's no valid Client Handler), 
     * then we throw an IllegalArgumentException because we don't know what to do with this invalid input!  
    
    
     *
     * @param  client Create a new clienthandler object
     *
     * @return A new clienthandler object
     *
     * @author Omer Bartfeld
     */
    private ClientHandler createHandler(Socket client) {
        if (client == null)
            throw new IllegalArgumentException("Invalid client socket");

        if (clientHandler instanceof PlayerHandler)
            return new PlayerHandler(client);
        else if (clientHandler instanceof BookScrabbleHandler)
            return new BookScrabbleHandler();
        else
            throw new IllegalArgumentException("Invalid ClientHandler");
    }

    /**    
     * The sendToOne function sends a message to one client.
     * 
     *
     * @param  msg Send a message to the client
     * @param  clientKey Identify the client that will receive the message
     *
     *
     * @author Omer Bartfeld
     */
    public void sendToOne(String msg, String clientKey) //send msg to a specific client
    {
        clients.get(clientKey).sendMsg(msg);
    }

    /**    
     * The sendToAllButOne function sends a message to all clients except for the one specified by clientKey.
     * 
     *
     * @param  msg Send a message to all clients
     * @param  clientKey Identify the client that is not to receive the message
     *
     * @author Omer Bartfeld
     */
    public void sendToAllButOne(String msg, String clientKey) //send msg to everyone cept a specific client
    {
        for (String key : clients.keySet())
            if (!key.equals(clientKey))
                clients.get(key).sendMsg(msg);


    }

    /**    
     * The close function closes the server.
     * It sets stop to true, shuts down the thread pool and waits for it to terminate.
     * If it doesn't terminate in 60 seconds, we shut it down now and interrupt its threads.
     * Finally, we close all of our client handlers by calling their close function.
     *
     * @author Omer Bartfeld
     */
    public void close() {
        setStop(true);
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.close();
        }
    }

    /**
     * The getPort function returns the port number of the server.
     *
     * @return The server port number
     *
     * @author Omer Bartfeld
     */
    public int getPort() {
        return port;
    }

    public boolean isStop() {
        return stop;
    }

    /**
     * The setStop function sets the stop variable to true, which will cause the
     * thread to terminate.

     *
     * @param  stop Stop the thread
     *
     * @author Omer Bartfeld
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }


    /**    
     * The getPlayerNames function returns a list of the keys of all players in the game.
     * this is used to allow us to synchronize the player keys across all clients.
     *
     * @return A list of player keys
     *
     * @author Omer Bartfeld
     */
    public List<String> getPlayerNames() {
        return playerNames;
    }
}
