package com.example.Scrabble.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GuestModel {
    private String name;
    private String serverAddress;
    private int serverPort;
    private boolean stop;

    public GuestModel(String name, String serverAddress, int serverPort) {
        this.name = name;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.stop = false;
    }

    public void connectServer() {
        try {
            Socket connection;
            connection = new Socket(serverAddress, this.serverPort);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            Scanner in = new Scanner(connection.getInputStream());
            while (!stop) {
                System.out.println("Enter a line to send to the server: ");
                String line = in.nextLine();
                System.out.println(line);
                if (line.equals("stop")) {
                    stop = true;
                }
            }
            connection.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void sendRequestToHost(String query) {
        // myHost.BookActions(query);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
