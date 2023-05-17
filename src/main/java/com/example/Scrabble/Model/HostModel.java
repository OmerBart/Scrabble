package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.ScrabbleServer.ClientHandler;
import com.example.Scrabble.ScrabbleServer.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class HostModel {
    MyServer gameServer;
    String Hostname;
    MyServer hostServer;
    
    ClientHandler ch;
    boolean stop;

    public HostModel(String name) {
        // set the name of the host
        this.Hostname = name;

        // create a server for the game
        Random r = new Random();
        gameServer = new MyServer(6000 + r.nextInt(1000), new BookScrabbleHandler());
        hostServer = new MyServer(6000 + r.nextInt(1000), new ModelHandler());
        // host new server for guests
        // this.stop = false;
        // try {
        //     ServerSocket hostSocket = new ServerSocket(6000 + r.nextInt(1000));
        //     hostSocket.setSoTimeout(1000);
        //     while (!stop) {
        //         try {
        //             Socket client = hostSocket.accept();
        //             ch.handleClient(client.getInputStream(), client.getOutputStream());
        //             ch.close();
        //             client.close();
        //         } catch (SocketTimeoutException e) {
        //             System.out.println(e.getMessage());
        //         }
        //     }
        //     hostSocket.close();
        // } catch (IOException e) {
        //     System.out.println("IOException in HostModel" + e.getMessage());
        // }
    }

    public void connectServer() {
        gameServer.start(); // runs in the background
        hostServer.start();
    }

    public void closeServer() {
        gameServer.close();
        hostServer.close();
    }

    public void handleClient(Socket client) {
        try {
            gameServer.getCh().handleClient(client.getInputStream(), client.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException in handleClient" + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("IOException in handleClient" + e.getMessage());
            }
        }

    }
    public void test(String query) {
        try {
            System.out.println("Connecting to port " + this.hostServer.getPort());
            Socket server = new Socket("localhost", this.hostServer.getPort());
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
            System.out.println("IOException in runGame");
        }
    }

    public void BookActions(String query) {
        try {
            System.out.println("Connecting to port " + this.gameServer.getPort());
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
            System.out.println("IOException in runGame");
        }
    }

}
