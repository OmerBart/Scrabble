package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.ClientHandler;

import java.io.*;

public class PlayerHandler implements ClientHandler {

    GameManager pm;
    BufferedReader in;
    PrintWriter out;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        pm = GameManager.get();
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String line = in.readLine();


            //logic for handling the model requests goes here (i.e. the logic for the model)
            //System.out.println("this came from the handleClient: "+ line);
            out.println("from: ModelHandler");
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
