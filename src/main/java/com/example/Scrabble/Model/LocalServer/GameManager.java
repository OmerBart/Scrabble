package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.Game.Board;
import com.example.Scrabble.Model.Game.Tile;
import com.example.Scrabble.Model.Game.Word;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.Player;
import com.example.Scrabble.Model.ScrabbleDictionary.IOserver.BookScrabbleHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class GameManager {
    private List<GuestPlayer> playersList;
    private LinkedHashMap<String, Integer> playerScores; // key: name+ID, value: score
    private LinkedHashMap<String, List<Tile>> playerTiles; // key: name+ID, value: tiles
    private MyServer hostServer;
    private MyServer IOserver;
    private Board gameBoard;
    private Tile.Bag bag;
    private int turn;
    private boolean hasGameStarted;
    private String[] gameBooks;

    private static GameManager single_instance = null;

    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    private GameManager() {
        Random r = new Random();
        playersList = new ArrayList<>();
        gameBoard = Board.getBoard();
        this.IOserver = new MyServer(6000 + r.nextInt(6000), new BookScrabbleHandler());
        bag = Tile.Bag.getBag();
        playerScores = new LinkedHashMap<>();
        playerTiles = new LinkedHashMap<>();
        hasGameStarted = false;
        gameBooks = new String[]{"search_books/The Matrix.txt,search_books/test.txt"};
        turn = 0;
    }

    public synchronized void setHost(MyServer hostServer) {
        this.hostServer = hostServer;
    }

    public synchronized String getPlayerTiles(String playerName) {
        StringBuilder tiles = new StringBuilder();
        for (Tile tile : playerTiles.get(playerName)) {
            //System.out.println("Tile: " + tile + "Player: " + playerName);
            tiles.append(tile).append(" ");
        }
        return tiles.toString();
    }

    public synchronized String addPlayer(GuestPlayer player) {
        if (playersList.contains(player) || playersList.size() > 3) {
            return "Player already in the game or game is full!";
        } else {
            player.setID(generateUniqueID());
            playersList.add(player);
            playerScores.put(player.getName(), 0);
            playerTiles.put(player.getName(), new ArrayList<>());
            if (playersList.size() > 1)
                System.out.println("Player added to the game successfully with ID: " + player.getPlayerID());

            return "Player added to the game successfully with ID: " + player.getPlayerID();
        }
    }

    private synchronized int generateUniqueID() {
        Random r = new Random();
        return r.nextInt(1000 + playersList.size());
    }

    public synchronized GuestPlayer getPlayer(String name) {
        for (GuestPlayer p : GameManager.get().playersList) {
            if (p.getName().split(":")[0].equals(name))
                return p;
        }
        return null;
    }

    public synchronized String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    public synchronized void myTurn() {
        updatePlayer("T:true", turn % playersList.size());
    }

//    public synchronized String myTurn(String playerName) {
//        while (!playersList.get(turn % playersList.size()).getName().contains(playerName)) {
//            try {
//                System.out.println(playerName + " is waiting for their turn");
//                wait(1000);
//            } catch (InterruptedException e) {
//                return "T:false";
//            }
//        }
//        System.out.println(playerName + " is playing");
//        return "T:true";
//    }

    public synchronized String startGame(String playerName) {
        IOserver.start();
        System.out.println("IO server started successfully at: " + IOserver.getPort());
        System.out.println("Number of players: " + playersList.size());
        for (int i = 0; i < 7; i++) {
            for (Player p : playersList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }

        hasGameStarted = true;
        return "Game Started!";
    }

    public synchronized String getTilefromBag(String playerName) {
        Tile t = bag.getRand();
        if (t == null)
            return "Bag is empty!";
        else {
            playerTiles.get(playerName).add(t);
            updatePlayers(playerName + " got a new tile: " + t);
            return "Got: " + t;
        }
    }

    public void endTurn() {
        System.out.println(playersList.get((turn) % playersList.size()).getName() + "'s turn ended");
        turn++;
        //updatePlayers(playersList.get(turn % playersList.size()).getName() + "'s turn starts now!");
        myTurn();

    }

    public synchronized void stopGame() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        hostServer.close();
        IOserver.close();
    }

    public synchronized String printPlayers() {
        return playersList.toString();
    }

    public synchronized String getScore(String playerName) {
        return Integer.toString(playerScores.getOrDefault(playerName, 0));
    }

    public synchronized String placeWord(String playerName, String word, int x, int y, boolean isHorizontal) {
        System.out.println("Placing word: " + word + " at: " + x + " " + y + " " + isHorizontal);
        char[] carr = word.toUpperCase().toCharArray();
        Tile[] wordTiles = new Tile[word.length()];
        int index = 0;
        for (char c : carr) {
            //System.out.println("Looking for tile: " + c );
            try {
                wordTiles[index] = playerTiles.get(playerName)
                        .stream()
                        .filter(t -> t.getLetter() == c)
                        .findFirst()
                        .orElseThrow(NoSuchElementException::new);
            } catch (NoSuchElementException e) {
                return "You don't have these Tiles!";
            }
            System.out.println("Placing tile: " + wordTiles[index].toString());
            playerTiles.get(playerName).remove(wordTiles[index]);
            index++;
        }
        int score = gameBoard.tryPlaceWord(new Word(wordTiles, x, y, isHorizontal));
        if(score > 0){
            System.out.println("Score: " + score);
            updatePlayers("BU,"+getGameBoard());
            return Integer.toString(score);
        }
        // Word placement failed, returning tiles to player
        else {
            for (Tile t : wordTiles)
                playerTiles.get(playerName).add(t);
            return "Word placement failed!";
        }
    }




    public synchronized String queryIOserver(String qword) {
        try {
            Socket socket = new Socket("localhost", IOserver.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            if (qword.startsWith("Q")) {
                String args = "Q,";
                for (String book : gameBooks)
                    args += book + ",";
                System.out.println("wowowo " + args + qword.split(":")[1]);
                out.println(args + qword.split(":")[1]);
                out.flush();
            } else if (qword.startsWith("C")) {
                String args = "C,";
                for (String book : gameBooks)
                    args += book + ",";
                out.println(args + qword.split(":")[1]);
                out.flush();
            }

            String res = in.nextLine();
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    private void updatePlayer(String msg, int playerKeyIndex) {
        hostServer.sendToOne(msg, hostServer.getPlayerNames().get(playerKeyIndex));

    }

    private void updatePlayers(String msg) {
        hostServer.sendToAllButOne(msg, hostServer.getPlayerNames().get(turn % playersList.size()));
    }

    public synchronized LinkedHashMap<String, List<Tile>> getPlayerTiles() {
        return playerTiles;
    }
}