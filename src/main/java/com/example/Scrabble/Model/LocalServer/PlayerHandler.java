package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements ClientHandler {
    private BufferedReader in;
    private PrintWriter out;
    private CommandFactory commandFactory;
    private Socket clientSocket;
    private boolean alive;
    //private ExecutorService threadPool;

    public PlayerHandler() {
        commandFactory = new CommandFactory();
    }

    public PlayerHandler(Socket client) {
        this();
        this.clientSocket = client;
        this.alive = true;
        //this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromClient));
            out = new PrintWriter(outToClient, true);
            String line;
            while (alive && (line = in.readLine()) != null) {
                Command command = commandFactory.createCommand(line);
                if (command != null) {
                    String result = command.execute();
                    out.println(result);
                    out.flush();
                } else {
                    out.println("Invalid command");
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if (out != null) {
            out.println("update:" + msg);
            out.flush();
        }
    }

    @Override
    public void close() {
        alive = false;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (clientSocket != null)
                clientSocket.close();
//            if (threadPool != null)
//                threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return in == null && out == null && clientSocket == null ;
        //&& (threadPool == null || threadPool.isShutdown())
    }
}
