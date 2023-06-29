package com.example.Scrabble.Model.ScrabbleDictionary.IOserver;

import com.example.Scrabble.Model.ServerUtils.ClientHandler;

import java.io.*;

/**
 * The `BookScrabbleHandler` class implements the `ClientHandler` interface and handles client requests for Scrabble dictionary operations.
 * It processes client input and sends corresponding responses.
 */
public class BookScrabbleHandler implements ClientHandler {
    private BufferedReader in;
    private PrintWriter out;
    private DictionaryManager dm;

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromClient));
            out = new PrintWriter(outToClient, true);
            dm = DictionaryManager.get(); // singleton

            String line = in.readLine();
            if (line.startsWith("Q")) {
                String[] arg = line.split(",", 2);
                out.println(dm.query(arg[1].split(",")));
            } else if (line.startsWith("C")) {
                String[] arg = line.split(",", 2);
                out.println(dm.query(arg[1].split(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMsg(String msg) {
        // Not implemented in this class
    }
}
