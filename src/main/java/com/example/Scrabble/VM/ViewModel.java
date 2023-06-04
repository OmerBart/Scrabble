package com.example.Scrabble.VM;

import com.example.Scrabble.Model.Player.HostPlayer;
import com.example.Scrabble.Model.Player.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    Player player;
    public StringProperty hostPlayerName; // observable
    public DoubleProperty dp;

    public ViewModel(Player player, StringProperty hostPlayerName, DoubleProperty dp) {
        this.player = player;
        this.hostPlayerName = hostPlayerName;
        this.dp = dp;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof HostPlayer) {
            hostPlayerName.setValue(((HostPlayer) o).getName());
        }
    }
}
