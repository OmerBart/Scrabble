package com.example.Scrabble.Model.Player;

import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.LocalServer.PlayerHandler;
import com.example.Scrabble.Model.ServerUtils.MyServer;


public class HostPlayer extends GuestPlayer {

    private final MyServer hostGameServer;
    private GameManager gameManager;
    private static HostPlayer hostPlayerInstance = null;

    public static HostPlayer get(Player player) {
        if (hostPlayerInstance == null) {
            hostPlayerInstance = new HostPlayer(player);
        }
        return hostPlayerInstance;
    }

    private HostPlayer(Player player) {
        super(player);
        int port = 65432;
        hostGameServer = new MyServer(port, new PlayerHandler());
        setServerAddress("localhost", port);
        System.out.println("host cutr: "+this.getServerAddress());
        hostGameServer.start();
        gameManager = GameManager.get();
        gameManager.setHost(hostGameServer);
        System.out.println(joinGame());

    }

    public MyServer getHostGameServer() {
        return hostGameServer;
    }

    public void stopGame() {
        gameManager.stopGame();
        // hostGameServer.close();
    }
    
}
