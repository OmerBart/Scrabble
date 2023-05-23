package com.example.Scrabble.Game;

import com.example.Scrabble.Model.Player;
import com.example.Scrabble.ScrabbleServer.BookScrabbleHandler;
import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class GameManager {

    private LinkedList<Player> playerList;
    MyServer hostServer;
    MyServer IOserver;
    private Board gameBoard;
    private Tile.Bag bag;


    private static GameManager single_instance = null;



    public static GameManager get(){
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    private GameManager(){
        Random r = new Random();
        playerList = new LinkedList<>();
        gameBoard = Board.getBoard();
        this.IOserver = new MyServer(6000 + r.nextInt(6000), new BookScrabbleHandler());
        bag = Tile.Bag.getBag();
    }
    public void setHost(MyServer hostServer, Player hostplayer) {
        this.hostServer = hostServer;
        playerList.add(hostplayer);
    }

    public String addPlayer(Player player){
        if(playerList.contains(player) || playerList.size() > 3) {
           // System.out.println("from addplayer >3");
            return "Player already in game or game is full";
        }
        else{
            playerList.add(player);
           // System.out.println("added player: " + playerList.getLast().toString());
            return "Player added" + playerList.getLast().toString();
        }
    }
    public String getGameBoard() {
        return gameBoard.getPrintableBoard();
    }
    public String getTilefromBag(){
        Tile t = bag.getRand();
        //System.out.println(Arrays.toString(Tile.Bag.getBag().getQuantities()));
        if(t == null)
            return "Bag is empty";
        else
            return t.toString();
    }

    public void stopGame() {
        hostServer.close();
        IOserver.close();
    }

    public String printPlayers() {
        return playerList.toString();
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
