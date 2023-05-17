package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.ClientHandler;

import java.io.*;

public class ModelHandler implements ClientHandler {

    BufferedReader in;
    PrintWriter out;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String line = in.readLine();
            String newline = new String(line + " from server");
            out.println(newline);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        try{
            in.close();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
