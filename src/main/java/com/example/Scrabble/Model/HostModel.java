package com.example.Scrabble.Model;

import com.example.Scrabble.Server.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.Server.ScrabbleServer.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class HostModel implements Model{
    MyServer s;
    public static void connectServer(){
        boolean ok=true;
        Random r=new Random();
        int port=6000+r.nextInt(1000);
        MyServer s=new MyServer(port, new BookScrabbleHandler());
        s.start(); // runs in the background
    }
    public static void runGame(int port,String query) {
        try {
            Socket server=new Socket("localhost",port);
            PrintWriter out=new PrintWriter(server.getOutputStream());
            Scanner in=new Scanner(server.getInputStream());
            out.println(query);
            out.flush();
            String res=in.next();
            in.close();
            out.close();
            server.close();
        } catch (IOException e) {
            System.out.println("IOException in runGame");
        }
    }





    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setID() {

    }
}
