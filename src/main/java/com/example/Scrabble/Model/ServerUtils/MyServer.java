package com.example.Scrabble.Model.ServerUtils;

import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ScrabbleDictionary.IOserver.BookScrabbleHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyServer {
    private final int port;
    private volatile boolean stop;
    private final Map<String, ClientHandler> clients;
    private final ExecutorService threadPool;
    private final ClientHandler clientHandler;

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.clients = new ConcurrentHashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        stop = false;
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket server = new ServerSocket(port)) {
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    if (!clients.containsKey(clientKey))
                        clients.put(clientKey, createHandler(client));
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

    private String getClientKey(Socket client) {
        return client.getInetAddress().getHostAddress() + ":" + client.getPort();
    }

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

    public void sendMsg(String msg) {
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.sendMsg(msg);
        }
    }

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

    public int getPort() {
        return port;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
