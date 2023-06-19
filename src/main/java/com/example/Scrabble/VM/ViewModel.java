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

    public ViewModel() {
    }

    @Override
    public void update(Observable o, Object arg) {
        // if(o instanceof HostPlayer) {
        // hostPlayerName.setValue(((HostPlayer) o).getName());
        // }
    }

    public static void startGame(String playerName) {
        System.out.println(HostPlayer.get(new GuestPlayer(playerName, 0)).startGame());
    }

    public static String joinGame(String playerName, String gameId, int playerID) {
        GuestPlayer guestPlayer = new GuestPlayer(playerName, playerID, gameId);
        return guestPlayer.joinGame();
    }

    public static Integer getScore(String playerName) {
        Player player = GameManager.getPlayer(playerName);
        if (player != null) {
            if (player instanceof GuestPlayer) {
                return ((GuestPlayer) player).getScore();
            }
        }
        return null;
    }

}
