package com.example.Scrabble.VM;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;
import static java.lang.Thread.sleep;


import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    public static GuestPlayer guestPlayer;

    public ViewModel() {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void startGame(String playerName) {
        guestPlayer = new GuestPlayer(playerName, 0);
        guestPlayer = HostPlayer.get(guestPlayer);
        System.out.println(guestPlayer.startGame());
    }

    public static String joinGame(String playerName, String gameId, int playerID) {
        guestPlayer = new GuestPlayer(playerName, playerID, gameId);
        return guestPlayer.joinGame();
    }

    public static Integer getScore(String playerName) {
        return guestPlayer.getScore();
    }

    public static String tryPlaceWord(String playerName, String word) {
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, 7, 7, true);
            return result;
        } else {
            return "0";
        }
    }

    public static String getTile() {
        return guestPlayer.getTile().split(" ")[1].split("|")[1];
    }

    public static String getPlayerTiles() {
        String tiles = "";
        for (String tile : guestPlayer.getPlayerTiles()) {
            tiles += tile;
        }
        return tiles;
    }
}
