package com.example.Scrabble.Model;

public class PlayerModel {

    private String playerName;
    private int playerID;



    public PlayerModel(String playerName, int playerID) {
        this.playerName = playerName;
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
