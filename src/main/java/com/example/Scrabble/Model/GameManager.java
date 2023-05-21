package com.example.Scrabble.Model;

import com.example.Scrabble.ScrabbleServer.MyServer;

import java.util.LinkedHashMap;

class GameManager {

    private LinkedHashMap<String,Player> playerMap;
    MyServer localServer;

    private static GameManager single_instance = null;



    public static GameManager get(){
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    private GameManager(){
        playerMap = new LinkedHashMap<>();
    }
    public void setHostplayer(Player hostplayer){
        if(playerMap.isEmpty())
            playerMap.put(hostplayer.getName(),hostplayer);
        else
            System.out.println("Host player already exists!");
    }
    public void addPlayer(Player player){
        playerMap.put(player.getName(),player);
    }
    public Player getPlayer(String name){
        return playerMap.get(name);
    }


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
