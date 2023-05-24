package com.example.Scrabble.Game;

import com.example.Scrabble.Model.Player;
import com.example.Scrabble.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.*;

public class GameManager {

    //private LinkedList<Player> playerList;
    private ArrayList<Player> playerList;
    private LinkedHashMap <String, Integer> playerScores; //key: name+ID, value: score
    private LinkedHashMap <String,List<Tile>> playerTiles; //key: name+ID, value: tiles

    MyServer hostServer;
    MyServer IOserver;
    private Board gameBoard;
    private Tile.Bag bag;
    private int turn;


    private static GameManager single_instance = null;



    public static GameManager get(){
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    private GameManager(){
        Random r = new Random();
        playerList = new ArrayList<>();
        gameBoard = Board.getBoard();
        this.IOserver = new MyServer(6000 + r.nextInt(6000), new BookScrabbleHandler());
        bag = Tile.Bag.getBag();
        playerScores = new LinkedHashMap<>();
        playerTiles = new LinkedHashMap<>();

    }
    public void setHost(MyServer hostServer, Player hostplayer) {
        this.hostServer = hostServer;
        playerList.add(hostplayer);
        playerScores.put(hostplayer.getName(), 0);
        playerTiles.put(hostplayer.getName(), new ArrayList<>());
        //addPLayer("Host", 0);
    }

    public String addPlayer(Player player){

        if(playerList.contains(player) || playerList.size() > 3) {
           // System.out.println("from addplayer >3");
            return "Player already in game or game is full!";
        }
        else{
            playerList.add(player);
            playerScores.put(player.getName(), 0);
            playerTiles.put(player.getName(), new ArrayList<>());
           // System.out.println("added player: " + playerList.getLast().toString());
            return "Player added to game successfully";
        }
    }
    public String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }
    public void startGame() {
        System.out.println("starting game");
        System.out.println("num of players : " + playerList.size());
        //giving 7 tiles to each player
        for (int i = 0 ; i<7 ; i++) {
            for(Player p : playerList)
                playerTiles.get(p.getName()).add(bag.getRand());
        }
        turn = 0;
    }
    public String getTilefromBag(String playerName){
        Tile t = bag.getRand();
        //System.out.println(Arrays.toString(Tile.Bag.getBag().getQuantities()));
        if(t == null)
            return "Bag is empty!";
        else {
            playerTiles.get(playerName).add(t);
            return "Got: " + t.toString();
        }
    }

    public void stopGame() {
        hostServer.close();
        IOserver.close();
    }

    public String printPlayers() {
        return playerList.toString();
    }

    public String getScore(String playerName) {
        //System.out.println(playerName);
        return Integer.toString(10);
    }
    public int placeWord(String playername, int x, int y, String word, boolean isHorizontal){
        if(playerTiles.get(playername).size() < word.length())
            return -1;
        else {
            //String[] s = word.split("(?!^)");
            char[] carr = word.toCharArray();
            Tile tt;
            Tile[] wordTiles = new Tile[word.length()];
            for (char c : carr) {
                try {
                    tt = playerTiles.get(playername).stream().filter(t -> t.getLetter() == c).findFirst().get();
                    playerTiles.get(playername).remove(tt);
                    wordTiles[word.indexOf(c)] = tt;

                } catch (NoSuchElementException e) {
                    return -1;
                }
            }
            //Word wordT = new Word(wordTiles, x, y, isHorizontal);
            int score = gameBoard.tryPlaceWord(new Word(wordTiles, x, y, isHorizontal));
            if (!(score > 0)) {
                playerScores.put(playername, playerScores.get(playername) + score);
                for (Tile t : wordTiles) {
                    playerTiles.get(playername).add(t);

                   ;
                }
            }
            return score;
        }
       // return gameBoard.tryPlaceWord();
    }


//    public Player getPlayer(String name){
//        return playerMap.get(name);
//    }


//    public boolean addPlayer(Player player){
//        if(playerMap.containsKey(name))
//            return false;
//        else{
//            //Player player = new Player(name,playerID);
//            playerMap.put(name,player);
//            return true;
//        }
//    }





//    private PlayerManager(MyServer modelServer, Player hostPlayer){
//        playerMap = new LinkedHashMap<>();
//        localServer = modelServer;
//        //add host player as first in the map
//        playerMap.put(hostPlayer.getName(),hostPlayer);
//    }








}
