package com.example.Scrabble.Model;

import com.example.Scrabble.Server.ScrabbleServer.ClientHandler;

import java.io.*;

public class GuestHandler implements ClientHandler {
    BufferedReader in;
    PrintWriter out;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        in = new BufferedReader(new InputStreamReader(inFromclient));
        out = new PrintWriter(outToClient, true);
        
    }

    @Override
    public void close() {

    }
}
