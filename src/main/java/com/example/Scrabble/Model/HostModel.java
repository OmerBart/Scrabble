package com.example.Scrabble.Model;

import com.example.Scrabble.Server.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.Server.ScrabbleServer.ClientHandler;
import com.example.Scrabble.Server.ScrabbleServer.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.Scanner;

public class HostModel {
    MyServer gameServer;
    String Hostname;
    
    ClientHandler ch;
    boolean stop;

    public HostModel(String name) {
        // set the name of the host
        this.Hostname = name;

        // create a server for the game
        Random r = new Random();
        gameServer = new MyServer(6000 + r.nextInt(1000), new BookScrabbleHandler());
    }

    public void connectServer() {
        gameServer.start(); // runs in the background
    }

    public void closeServer() {
        gameServer.close();
    }

    // public void handleClient(Socket client) {
    //     try {
    //         gameServer.getCh().handleClient(client.getInputStream(), client.getOutputStream());
    //     } catch (IOException e) {
    //         System.out.println("IOException in handleClient" + e.getMessage());
    //     } finally {
    //         try {
    //             client.close();
    //         } catch (IOException e) {
    //             System.out.println("IOException in handleClient" + e.getMessage());
    //         }
    //     }
    // }

    public void BookActions(String query) {
        try {
            System.out.println("Connecting to localhost on port " + this.gameServer.getPort());
            Socket server = new Socket("localhost", this.gameServer.getPort());
            PrintWriter out = new PrintWriter(server.getOutputStream());
            Scanner in = new Scanner(server.getInputStream());
            out.println(query);
            out.flush();
            String res = in.next();
            System.out.println(Hostname + " your results are: " + res);
            in.close();
            out.close();
            server.close();
        } catch (IOException e) {
            System.out.println("IOException in BookActions: "+e.getMessage());
        }
    }

}
