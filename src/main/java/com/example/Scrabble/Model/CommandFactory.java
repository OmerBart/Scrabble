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
        else if (request.contains("startGame")) {
            String playerName = request.split(":")[1];
            return new StartGameCommand(playerName);
        }
        else if (request.contains("stopGame")) {
            return new StopGameCommand();
        }
        else if (request.contains("getScore")) {
            String playerName = request.split("getScore:")[1];
            return new GetScoreCommand(playerName);
        }
        else if (request.contains("placeWord")) {
            //request comes in format of "placeWord:playerName:ID:word:x:y:isHorizontal"
            String[] arg = request.split(":");
            String playerName = arg[1] + ":" + arg[2];
            String word = arg[3];
            int x = Integer.parseInt(arg[4]);
            int y = Integer.parseInt(arg[5]);
            boolean isHorizontal = Boolean.parseBoolean(arg[6]);
            return new PlaceWordCommand(playerName, word, x, y, isHorizontal);
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

    private class StartGameCommand implements Command {
        private String playerName;

        public StartGameCommand(String playerName) {
            this.playerName = playerName;
        }
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.startGame(playerName);
        }
    }

    private class StopGameCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            GM.stopGame();
            return "Game stopped";
        }
    }
    private class GetScoreCommand implements Command {
        private String playerName;

        public GetScoreCommand(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getScore(playerName);
        }
    }
    private class PlaceWordCommand implements Command {
        private String playerName;
        private String word;
        private int x;
        private int y;
        private boolean isHorizontal;

        public PlaceWordCommand(String playerName, String word, int x, int y, boolean isHorizontal) {
            this.playerName = playerName;
            this.word = word;
            this.x = x;
            this.y = y;
            this.isHorizontal = isHorizontal;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.placeWord(playerName, word, x, y, isHorizontal);
        }
    }
}
