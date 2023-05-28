package com.example.Scrabble;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

public class HostFacade {
    HostPlayer hostPLayer;

    public HostFacade() {
    this.hostPLayer = HostPlayer.get(new GuestPlayer("Host",0));
    }

    public String getHostPlayerName() {
        return hostPLayer.getName();
    }
    public void startGame() {
        hostPLayer.startGame();
    }
    public void stopGame() {
        hostPLayer.stopGame();
    }



}
