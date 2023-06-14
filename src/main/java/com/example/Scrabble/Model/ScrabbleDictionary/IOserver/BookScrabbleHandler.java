package com.example.Scrabble.Model.ScrabbleDictionary.IOserver;


import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;

//class that handles the client request
public class BookScrabbleHandler implements ClientHandler {
    BufferedReader in;
    PrintWriter out;
    DictionaryManager dm;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {

            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            dm = DictionaryManager.get(); // singleton

            String line = in.readLine();
            if(line.startsWith("Q")){
                String[] arg = line.split(",",2);
                out.println(dm.query(arg[1].split(",")));
            }
            else if(line.startsWith("C")){
                String[] arg = line.split(",",2);
                out.println(dm.query(arg[1].split(",")));
            }



        } catch (IOException e){
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

    @Override
    public void sendMsg(String msg) {

    }
}
