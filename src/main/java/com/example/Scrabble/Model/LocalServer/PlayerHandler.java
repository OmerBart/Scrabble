package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlayerHandler implements ClientHandler {

    private BufferedReader in;
    private PrintWriter out;
    private CommandFactory commandFactory;
    private Socket clientSocket;
    private boolean alive;
    //private ExecutorService threadPool;

    public PlayerHandler() {
        // GameManager GM = GameManager.get();
        commandFactory = new CommandFactory();
    }

    public PlayerHandler(Socket client) {
        // GameManager GM = GameManager.get();
        commandFactory = new CommandFactory();
        this.clientSocket = client;
        this.alive = true;
        //this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String line;
            while (alive) {
                while ((line = in.readLine()) != null) {
                    Command command = commandFactory.createCommand(line);
                    if (command != null) {
                        //Future<String> future = threadPool.submit(()->command.execute());
                        String result = command.execute();
                        out.println(result);
                        //out.flush();
                    } else {
                        out.println("Invalid command");
                        //out.flush();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if (out != null) {
            out.println("update:" + msg);
            //out.flush();
        }
    }

    @Override
    public void close() {
        try {
            alive = false;
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return in == null && out == null;
    }

//    @Override
//    public void run() {
//        try {
//            handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}