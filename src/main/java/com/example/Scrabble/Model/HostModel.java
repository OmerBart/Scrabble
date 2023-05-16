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

public class HostModel{
    MyServer gameServer;
    String Hostname;
    boolean stop;

    ClientHandler ch;


    public HostModel(String name){
        this.Hostname = name;
        this.stop = false;
        Random r=new Random();
        gameServer = new MyServer(6000+r.nextInt(1000), new BookScrabbleHandler());
//        try {
//            ServerSocket hostSocket = new ServerSocket(6000 + r.nextInt(1000));
//            hostSocket.setSoTimeout(1000);
//            while(!stop) {
//                try{
//                    Socket client = hostSocket.accept();
//                    ch.handleClient(client.getInputStream(), client.getOutputStream());
//                    ch.close();
//                    client.close();
//                }catch(SocketTimeoutException e) {}
//            }
//            hostSocket.close();
//        } catch (IOException e) {
//            System.out.println("IOException in HostModel" + e.getMessage());
//        }



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
    public  void BookActions(String query) {
        try {
            Socket server=new Socket("localhost", this.gameServer.getPort());
            PrintWriter out=new PrintWriter(server.getOutputStream());
            Scanner in=new Scanner(server.getInputStream());
            out.println(query);
            out.flush();
            String res=in.next();
            System.out.println(Hostname +" your results are: "+ res);
            in.close();
            out.close();
            server.close();
        } catch (IOException e) {
            System.out.println("IOException in runGame");
        }
    }









}
