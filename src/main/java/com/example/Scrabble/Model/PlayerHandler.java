package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.ClientHandler;

import java.io.*;

public class PlayerHandler implements ClientHandler {

    GameManager GM;
    BufferedReader in;
    PrintWriter out;


    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        GM = GameManager.get();
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String line = in.readLine();
            if(line.contains("GetTile")){
                out.println(GM.getTilefromBag());
                out.flush();
            }
            if(line.contains("boardState")){
                out.println(GM.getGameBoard());
            }
            if(line.contains("Join")){
                String[] arg = line.split(":");
                out.println(GM.addPlayer(new GuestPlayer(arg[1],Integer.parseInt(arg[2]))));
            }
            if(line.contains("printPlayers")){
                out.println(GM.printPlayers());
            }


            //logic for handling the model requests goes here (i.e. the logic for the model)
            //System.out.println("this came from the handleClient: "+ line);
            //out.println("from: ModelHandler");
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
