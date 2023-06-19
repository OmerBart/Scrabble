package com.example.Scrabble.VM;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;
import com.example.Scrabble.Model.Player.Player;
// import javafx.beans.InvalidationListener;
// import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    public static GuestPlayer guestPlayer;

    public ViewModel() {
    }

    @Override
    public void update(Observable o, Object arg) {
        // if(o instanceof HostPlayer) {
        // hostPlayerName.setValue(((HostPlayer) o).getName());
        // }
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
        return GameManager.get().getPlayer(playerName).placeWord(word, 0, 0, true);
    }

    public static String getTile() {
        return guestPlayer.getTile().split(" ")[1].split("|")[1];
    }

    public static String getPlayerTiles(){
        String tiles = "";
        for (String tile : guestPlayer.getPlayerTiles()) {
            tiles += tile;
        }
        return tiles;
    }
}
