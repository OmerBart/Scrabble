package com.example.Scrabble.Model;

import com.example.Scrabble.Server.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.Server.ScrabbleServer.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class HostModel{
    MyServer gameServer;

    String Hostname;
    public HostModel(String name){
        this.Hostname = name;
        Random r=new Random();
        gameServer = new MyServer(6000+r.nextInt(1000), new BookScrabbleHandler());
        // ServerSocket hostSocket = new ServerSocket(6000+r.nextInt(1000));


    }

    public  void connectServer(){
        gameServer.start(); // runs in the background
    }

    public void closeServer(){
        gameServer.close();
    }
    public void handleClient(Socket client){
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
    public  void runGame(String query) {
        try {
            Socket server=new Socket("localhost", this.gameServer.getPort());
            PrintWriter out=new PrintWriter(server.getOutputStream());
            Scanner in=new Scanner(server.getInputStream());
            out.println(query);
            out.flush();
            String res=in.next();
            System.out.println(Hostname +" your query results are: "+ res);
            in.close();
            out.close();
            server.close();
        } catch (IOException e) {
            System.out.println("IOException in runGame");
        }
    }







}
