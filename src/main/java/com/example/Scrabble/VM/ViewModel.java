package com.example.Scrabble.VM;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel {

    public GuestPlayer guestPlayer;
    public StringProperty playerNameProperty;
    public StringProperty scoreProperty;

    private static ViewModel viewModelInstance = null;

    public static ViewModel get() {
        if (viewModelInstance == null) {
            viewModelInstance = new ViewModel();
        }
        return viewModelInstance;
    }

    public ViewModel() {
        playerNameProperty = new SimpleStringProperty("");
        scoreProperty = new SimpleStringProperty("");
    }

    public void startGame() {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), 0);
        guestPlayer = HostPlayer.get(guestPlayer);
        System.out.println(guestPlayer.startGame());
    }

    public String joinGame(String gameId, int playerID) {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), playerID, gameId);
        return guestPlayer.joinGame();
    }

    public Integer getScore() {
        return guestPlayer.getScore();
    }

    public String tryPlaceWord(String word) {
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, 7, 7, true);
            int score = Integer.parseInt(result);
            score += Integer.parseInt(scoreProperty.getValue());
            scoreProperty.setValue(String.valueOf(score));
            return result;
        } else {
            return "0";
        }
    }

    public String getBoard() {
        return guestPlayer.getCurrentBoard();
    }

    public String getTile() {
        return guestPlayer.getTile().split(" ")[1].split("|")[1];
    }

    public String getPlayerTiles() {
        return guestPlayer.printTiles();
    }
}
