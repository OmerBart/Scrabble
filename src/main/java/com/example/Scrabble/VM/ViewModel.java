package com.example.Scrabble.VM;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel {

    public GuestPlayer guestPlayer;
    public StringProperty playerNameProperty;
    public StringProperty scoreProperty;
    public StringProperty boardProperty;
    public StringProperty playersProperty;

    private static ViewModel viewModelInstance = null;

    public static ViewModel get() {
        if (viewModelInstance == null) {
            viewModelInstance = new ViewModel();
        }
        return viewModelInstance;
    }

    public ViewModel() {
        playerNameProperty = new SimpleStringProperty("");
        scoreProperty = new SimpleStringProperty("0");
        boardProperty = new SimpleStringProperty("");
        playersProperty = new SimpleStringProperty("");
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

    public String tryPlaceWord(String word, int x, int y, boolean isHorizontal) {
        System.out.println("word: " + word + " x: " + x + " y: " + y + " isHorizontal: " + isHorizontal);
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, x-1, y-1, isHorizontal);
            System.out.println("new score: " + result);
            int score = Integer.parseInt(result);
            score += Integer.parseInt(scoreProperty.getValue());
            scoreProperty.setValue(String.valueOf(score));
            return result;
        } else {
            System.out.println("not my turn");
            return scoreProperty.getValue();
        }
    }

    public String getBoard() {
        return guestPlayer.getCurrentBoard();
    }

    public String getTile() {
        String result = guestPlayer.getTile();
        System.out.println(result);
        return result.split(" ")[1];
    }

    public String getPlayerTiles() {
        return guestPlayer.printTiles();
    }
}
