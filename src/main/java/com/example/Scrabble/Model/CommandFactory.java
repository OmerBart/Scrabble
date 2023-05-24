package com.example.Scrabble.Model;

import com.example.Scrabble.Game.GameManager;

public class CommandFactory {
    public Command createCommand(String request) {
        if (request.contains("getTile:")) {
            String playerName = request.split("getTile:")[1];
            return new GetTileCommand(playerName);
        } else if (request.contains("boardState")) {
            return new BoardStateCommand();
        } else if (request.contains("join")) {
            String[] args = request.split(":");
            if (args.length == 3) {
                String playerName = args[1];
                int playerID = Integer.parseInt(args[2]);
                return new JoinCommand(playerName, playerID);
            }
        }
        // Add more conditions to handle other commands
        return null;
    }

    private class GetTileCommand implements Command {
        private String playerName;

        public GetTileCommand(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getTilefromBag(playerName);
        }
    }

    private class BoardStateCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getGameBoard();
        }
    }

    private class JoinCommand implements Command {
        private String playerName;
        private int playerID;

        public JoinCommand(String playerName, int playerID) {
            this.playerName = playerName;
            this.playerID = playerID;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.addPlayer(new GuestPlayer(playerName, playerID));
        }
    }
}
