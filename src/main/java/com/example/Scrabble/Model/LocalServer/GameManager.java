package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.Game.Board;
import com.example.Scrabble.Model.Game.Tile;
import com.example.Scrabble.Model.Game.Word;
import com.example.Scrabble.Model.Player.Player;
import com.example.Scrabble.Model.ScrabbleDictionary.IOserver.BookScrabbleHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class GameManager {

    // private LinkedList<Player> playerList;
    private List<Player> playersList;
    private LinkedHashMap<String, Integer> playerScores; // key: name+ID, value: score
    private LinkedHashMap<String, List<Tile>> playerTiles; // key: name+ID, value: tiles

    MyServer hostServer;
    MyServer IOserver;
    private Board gameBoard;
    private Tile.Bag bag;
    private int turn;

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

    }

    public void setHost(MyServer hostServer, Player hostplayer) {
        this.hostServer = hostServer;
        playersList.add(hostplayer);
        playerScores.put(hostplayer.getName(), 0);
        playerTiles.put(hostplayer.getName(), new ArrayList<>());
        // addPLayer("Host", 0);
    }

    public String playerTiles(String playerName) {
        String tiles = "";
        for (Tile tile : playerTiles.get(playerName)) {
            tiles += tile.toString() + " ";
        }
        return tiles;
    }

    public String addPlayer(Player player) {
        if (playersList.contains(player) || playersList.size() > 3) {
            // System.out.println("from addplayer >3");
           // playerList.
            return "Player already in game or game is full!";
        } else {
            playersList.add(player);
            playerScores.put(player.getName(), 0);
            playerTiles.put(player.getName(), new ArrayList<>());
            // System.out.println("added player: " + playerList.getLast().toString());
            return "Player added to game successfully";
        }
    }

    public String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    public String myTurn(String playerName) {
        //System.out.println("hello from " + playerName);
        while ((playersList.get(turn % playersList.size()).getName().contains(playerName))) {
            // System.out.println("whello from " + playerName);
            try {
                System.out.println(playerName + " is waiting for their turn");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return "false";
            }
        }
        //turn++;
        return "true";

    }

    public String startGame(String playerName) {
        // StringBuilder players = new StringBuilder(System.out);
        System.out.println("Starting IO server...");
        IOserver.start();
        System.out.println("IO server started successfully at: " + IOserver.getPort());
        System.out.println("num of players : " + playersList.size());
        // giving 7 tiles to each player
        for (int i = 0; i < 7; i++) {
            for (Player p : playersList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }
        turn = 0;
        //hostServer.sendMsg("Game started!");
        return "Game Started!";
    }

    public String getTilefromBag(String playerName) {
        Tile t = bag.getRand();
        // System.out.println(Arrays.toString(Tile.Bag.getBag().getQuantities()));
        if (t == null)
            return "Bag is empty!";
        else {
            playerTiles.get(playerName).add(t);
            //hostServer.sendMsg("wakakakaka");
            return "Got: " + t.toString();
        }
    }


    public boolean endTurn() {
        turn++;
        hostServer.sendMsg("Turn ended");
        return true;
    }

    public void stopGame() {
        hostServer.sendMsg("Game is over! from MEEE");
        hostServer.close();
        IOserver.close();

    }

    public String printPlayers() {
        return playersList.toString();
    }

    public String getScore(String playerName) {
        // System.out.println(playerName);
        return Integer.toString(10);
    }

    public String placeWord(String playername, String word, int x, int y, boolean isHorizontal) {
        if (playerTiles.get(playername).size() < word.length())
            return Integer.toString(0);
        else {
            // String[] s = word.split("(?!^)");
            char[] carr = word.toUpperCase().toCharArray();
            Tile tmpTile;
            Tile[] wordTiles = new Tile[word.length()];
            for (char c : carr) {
                try {
                    tmpTile = playerTiles.get(playername).stream().filter(t -> t.getLetter() == c).findFirst().get();
                    playerTiles.get(playername).remove(tmpTile);
                    wordTiles[word.indexOf(c) + 1] = tmpTile;

                } catch (NoSuchElementException e) {
                    System.out.println("you do not have the letters for the word in your hand");
                    return Integer.toString(0);
                }

            }
            // Word wordT = new Word(wordTiles, x, y, isHorizontal);

            int score = gameBoard.tryPlaceWord(new Word(wordTiles, x, y, isHorizontal));
            if (!(score > 0)) {
                for (Tile t : wordTiles)
                    playerTiles.get(playername).add(t);
            } else {
                playerScores.put(playername, playerScores.get(playername) + score);
                return Integer.toString(score);
            }
        }
        return Integer.toString(0); // to remove
        // return gameBoard.tryPlaceWord()
    }

    // query + challenge dictionary
    public String queryIOserver(String Args) {
        try {
            Socket s = new Socket("localhost", IOserver.getPort());
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Scanner in = new Scanner(s.getInputStream());

            // Send the request to the server
            out.println(Args);
            out.flush();

            // Receive the response from the server
            String res = in.nextLine();
            in.close();
            out.close();
            s.close();
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }

    }

    public LinkedHashMap<String, List<Tile>> getPlayerTiles() {
        return playerTiles;
    }
}