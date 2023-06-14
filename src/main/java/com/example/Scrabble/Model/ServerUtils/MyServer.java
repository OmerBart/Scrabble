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

public class MyServer {

    int port;
    boolean stop;
    private Map<String,ClientHandler> clients;
    private ExecutorService threadPool;
    ClientHandler ch;
    //Socket client;

    public MyServer(int port, ClientHandler ch){
        this.port = port;
        this.ch = ch;
        this.clients = new ConcurrentHashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        //this.threadPool = Executors.newFixedThreadPool(4);
    }

    public void start(){
        stop = false;
        new Thread(()->startServer()).start();
    }

    private void startServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while(!stop) {
                try{
                    Socket client = server.accept();
                    String clientKey = client.getInetAddress().getHostAddress() + ":" + client.getPort();
                    if(!clients.containsKey(clientKey))
                        clients.put(clientKey, createHandler(ch, client));
                    //Socket cc = clients.get(clientKey);
                    ClientHandler cch = clients.get(clientKey);
                    //threadPool.submit(()->cch);
                    threadPool.submit(()-> {
                        try {
                            cch.handleClient(client.getInputStream(), client.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //cch.handleClient(client.getInputStream(), client.getOutputStream());
                    //cch.close();
                    //client.close();
                }catch(SocketTimeoutException e) {}
            }
            server.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private ClientHandler createHandler(ClientHandler ch, Socket client) {
        if(ch instanceof PlayerHandler)
            return new PlayerHandler(client);
        else if(ch instanceof BookScrabbleHandler)
            return new BookScrabbleHandler();
        else
            throw new IllegalArgumentException("Invalid ClientHandler");
    }

    public void sendMsg(String msg) {
        for (ClientHandler ch : clients.values()) {
            //System.out.println(clients.values().size());
            ch.sendMsg(msg);
        }
    }


    public void close(){
        stop = true;
        threadPool.shutdown();

    }
    public int getPort() {
        return port;
    }

    public ClientHandler getCh() {
        return ch;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }


}
