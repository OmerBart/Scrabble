package com.example.Scrabble.VM;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel {

    public static GuestPlayer guestPlayer;
    public static StringProperty playerName = new SimpleStringProperty("");

    public ViewModel() {
    }

    public static void startGame() {
        guestPlayer = new GuestPlayer(playerName.getValue(),0);
        guestPlayer = HostPlayer.get(guestPlayer);
        System.out.println(guestPlayer.startGame());
    }

    public static String joinGame(String gameId, int playerID) {
        guestPlayer = new GuestPlayer(playerName.getValue(), playerID, gameId);
        return guestPlayer.joinGame();
    }

    public static Integer getScore() {
        return guestPlayer.getScore();
    }

    public static String tryPlaceWord(String word) {
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, 7, 7, true);
            return result;
        } else {
            return "0";
        }
    }

    public static String getBoard() {
        return guestPlayer.getCurrentBoard();
    }

    public static String getTile() {
        return guestPlayer.getTile().split(" ")[1].split("|")[1];
    }

    public static String getPlayerTiles() {
        return guestPlayer.printTiles();
    }
}
