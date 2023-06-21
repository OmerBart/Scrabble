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

import static java.lang.Thread.sleep;

public class GameManager {
    private List<GuestPlayer> playersList;
    private LinkedHashMap<String, Integer> playerScores; // key: name+ID, value: score
    private LinkedHashMap<String, List<Tile>> playerTiles; // key: name+ID, value: tiles
    private MyServer hostServer;
    private MyServer IOserver;
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

    public void setHost(MyServer hostServer, GuestPlayer hostplayer) {
        this.hostServer = hostServer;
        playersList.add(hostplayer);
        playerScores.put(hostplayer.getName(), 0);
        playerTiles.put(hostplayer.getName(), new ArrayList<>());
    }

    public String getPlayerTiles(String playerName) {
        StringBuilder tiles = new StringBuilder();
        for (Tile tile : playerTiles.get(playerName)) {
            tiles.append(tile.getLetter() + " ");
        }
        return tiles.toString();
    }

    public String addPlayer(GuestPlayer player) {
        if (playersList.contains(player) || playersList.size() > 3) {
            return "Player already in the game or game is full!";
        } else {
            playersList.add(player);
            playerScores.put(player.getName(), 0);
            playerTiles.put(player.getName(), new ArrayList<>());
            return "Player added to the game successfully";
        }
    }

    public GuestPlayer getPlayer(String name) {
        for (GuestPlayer p : GameManager.get().playersList) {
            System.out.println("GM: " + p.getName());
            if (p.getName().split(":")[0].equals(name))
                return p;
        }
        System.out.println("GM");
        return null;
    }

    public String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }

    public String myTurn(String playerName) {
        // System.out.println(turn + " TURN player: " + playersList.get(turn %
        // playersList.size()).getName() );
        while (!playersList.get(turn % playersList.size()).getName().contains(playerName)) {
            try {
                System.out.println(playerName + " is waiting for their turn");
                sleep(1000);
            } catch (InterruptedException e) {
                return "false";
            }
        }
        return "true";
    }

    public String startGame(String playerName) {
        IOserver.start();
        System.out.println("IO server started successfully at: " + IOserver.getPort());
        System.out.println("Number of players: " + playersList.size());
        for (int i = 0; i < 7; i++) {
            for (Player p : playersList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }
        turn = 1;
        return "Game Started!";
    }

    public String getTilefromBag(String playerName) {
        Tile t = bag.getRand();
        if (t == null)
            return "Bag is empty!";
        else {
            playerTiles.get(playerName).add(t);
            return "Got: " + t.toString();
        }
    }

    public boolean endTurn() {
        turn++;
        return true;
    }

    public void stopGame() {
        hostServer.sendMsg("Game is over! from MEEE");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        hostServer.close();
        IOserver.close();
    }

    public String printPlayers() {
        return playersList.toString();
    }

    public String getScore(String playerName) {
        return Integer.toString(playerScores.getOrDefault(playerName, 0));
    }

    public String placeWord(String playerName, String word, int x, int y, boolean isHorizontal) {
        System.out.println("Placing word: " + word + " at: " + x + " " + y + " " + isHorizontal);
        // return "true";
        if (playerTiles.get(playerName).size() < word.length()) {
            System.out.println("You do not have the letters for the word in your hand");
            return Integer.toString(0);
        } else {
            System.out.println("you have the letters for the word in your hand");
            char[] carr = word.toUpperCase().toCharArray();
            Tile tmpTile;
            Tile[] wordTiles = new Tile[word.length()];
            System.out.println("player tiles: " + playerTiles.get(playerName).toString());
            for (char c : carr) {
                try {
                    System.out.println("char: " + c);
                    tmpTile = playerTiles.get(playerName).stream().filter(t -> t.getLetter() == c).findFirst().get();
                    playerTiles.get(playerName).remove(tmpTile);
                    System.out.println("tile: " + tmpTile.toString());
                    wordTiles[word.indexOf(c)] = tmpTile;
                    System.out.println("word tiles: " + Arrays.toString(wordTiles));
                } catch (NoSuchElementException e) {
                    System.out.println("You do not have the letters for the word in your hand");
                    return Integer.toString(0);
                }

            }
            System.out.println("you had all the letters");
            int score = gameBoard.tryPlaceWord(new Word(wordTiles, x, y, isHorizontal));
            System.out.println("score: " + score);
            if (score <= 0) {
                System.out.println("Word placement failed - returning tiles to player");
                for (Tile t : wordTiles)
                    playerTiles.get(playerName).add(t);
            } else {
                System.out.println("Word placement succeeded - updating player score");
                playerScores.put(playerName, playerScores.get(playerName) + score);
                return Integer.toString(score);
            }
        }
        System.out.println("Word placement failed");
        return Integer.toString(0);
    }

    public String queryIOserver(String Args) {
        try (Socket socket = new Socket("localhost", IOserver.getPort());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner in = new Scanner(socket.getInputStream())) {

            out.println(Args);
            out.flush();

            String res = in.nextLine();
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to server: " + e.getMessage(), e);
        }
    }

    public LinkedHashMap<String, List<Tile>> getPlayerTiles() {
        return playerTiles;
    }
}
