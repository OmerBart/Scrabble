package com.example.Scrabble.Model.LocalServer;

import com.example.Scrabble.Model.Player.GuestPlayer;

/**
 * The `CommandFactory` class is responsible for creating command objects based on client requests.
 * It implements a synchronized factory method, `createCommand`, to create specific command objects
 * based on the provided request.
 */
public class CommandFactory {
    /**
     * The createCommand function takes in a string and returns a Command object.
     * The function parses the string to determine which command is being requested,
     * then creates an instance of that command and returns it. If the request is not recognized, null is returned.

     *
     * @param  request The client request.
     *
     * @return A command object that corresponds to the request, or null if no matching command is found.
     *
     * @author Omer Bartfeld
     */
    public synchronized Command createCommand(String request) {
        if (request.contains("getTile:")) {
            String playerName = request.split("getTile:")[1];
            return new GetTileCommand(playerName);
        } else if (request.contains("boardState")) {
            return new BoardStateCommand();
        } else if (request.contains("join")) {
            String[] args = request.split(",");
            if (args.length == 2) {
                String playerName = args[1];
                return new JoinCommand(playerName);
            } else
                return new ErrorCommand("Join command must be in the format: join,playerName:ID");
        } else if (request.contains("startGame")) {
            String playerName = request.split(",")[1];
            return new StartGameCommand(playerName);
        } else if (request.contains("getPlayerList")) {
            return new GetPlayerListCommand();
        } else if (request.contains("endGame")) {
            return new StopGameCommand();
        } else if (request.contains("getScore")) {
            String playerName = request.split("getScore:")[1];
            return new GetScoreCommand(playerName);
        } else if (request.contains("placeWord")) {
            //request comes in format of "placeWord:playerName:ID:word:x:y:isHorizontal"
            String[] arg = request.split(":");
            String playerName = arg[1] + ":" + arg[2];
            String word = arg[3];
            int x = Integer.parseInt(arg[4]);
            int y = Integer.parseInt(arg[5]);
            boolean isHorizontal = Boolean.parseBoolean(arg[6]);
            return new PlaceWordCommand(playerName, word, x, y, isHorizontal);
        } else if (request.contains("isMyTurn")) {
            String playerName = request.split(",")[1];
            return new IsTurnCommand(playerName);

        } else if (request.contains("Q")) {
            //String rq = request.split(":")[1];
            return new QueryCommand(request);

        } else if (request.contains("C")) {
            //String rq = request.split(":")[1];
            return new ChallengeCommand(request);

        }
//        else if (request.contains("getTurn")) {
//            return new GetTurnCommand();
//        }
        else if (request.contains("endTurn")) {
            return new EndTurnCommand();
        } else if (request.contains("printTiles")) {
            String playerName = request.split(",")[1];
            return new PrintTilesCommand(playerName);
        }

        // Add more conditions to handle other commands
        return null;
    }

    private static class GetTileCommand implements Command {
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

    private static class BoardStateCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getGameBoard();
        }
    }

    private static class JoinCommand implements Command {
        private String playerName;
        private int playerID;

        public JoinCommand(String playerNameID) {
            this.playerName = playerNameID.split(":")[0];
            this.playerID = Integer.parseInt(playerNameID.split(":")[1]);
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.addPlayer(new GuestPlayer(playerName, playerID));
        }
    }

    private static class StartGameCommand implements Command {
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

    private static class StopGameCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            GM.endGame();
            return "Game stopped";
        }
    }

    private static class GetScoreCommand implements Command {
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

    private static class PlaceWordCommand implements Command {
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
            System.out.println("PlaceWordCommand");
            GameManager GM = GameManager.get();
            return GM.placeWord(playerName, word, x, y, isHorizontal);
        }
    }

    private static class QueryCommand implements Command {
        private String query;

        public QueryCommand(String query) {
            this.query = query;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.queryIOserver(this.query);
        }
    }

    private static class ErrorCommand implements Command {
        private String Error;

        public ErrorCommand(String Error) {
            this.Error = Error;
        }

        @Override
        public String execute() {
            return this.Error;
        }
    }

    private static class IsTurnCommand implements Command {
        private String playerName;

        public IsTurnCommand(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return "I used to call to : GM.myTurn(playerName)";
        }
    }

    private static class EndTurnCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.endTurn();
        }
    }

    private static class PrintTilesCommand implements Command {
        private String playerName;

        public PrintTilesCommand(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getPlayerTiles(playerName);
        }
    }

    private static class ChallengeCommand implements Command {
        private String query;

        public ChallengeCommand(String query) {
            this.query = query;
        }

        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.queryIOserver(this.query);
        }
    }
    private class GetPlayerListCommand implements Command {
        @Override
        public String execute() {
            GameManager GM = GameManager.get();
            return GM.getPlayerList();
        }
    }

}
