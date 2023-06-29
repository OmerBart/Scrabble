package com.example.Scrabble.VM;

import java.util.Observable;
import java.util.Observer;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewModel extends Observable implements Observer {

    public GuestPlayer guestPlayer;
    public StringProperty playerNameProperty;
    public StringProperty numberOfPlayersProperty;
    public StringProperty scoreProperty;
    public StringProperty boardProperty;
    public StringProperty playersProperty;
    private Stage stage;
    private Scene scene;

    public Boolean turn;
    public String board;
    public String players;

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
        numberOfPlayersProperty = new SimpleStringProperty("");
        turn = false;
        board = "";
        players = "";
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        System.out.println("VM: Game has been updated");
        // System.out.println(arg);
        if (arg instanceof String) {
            String argString = (String) arg;
            if (argString.startsWith("game started!")) {
                System.out.println("game started");
                setGameState(argString.substring(13));
                try {
                    Parent root = FXMLLoader
                            .load(getClass().getResource("/com/example/Scrabble/View/board-scene.fxml"));
                    scene = new Scene(root, 1000, 700);
                    scene.getStylesheets()
                            .add(getClass().getResource("/com/example/Scrabble/View/style.css").toExternalForm());
                    Platform.runLater(() -> {
                        stage.setScene(scene);
                    });
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (argString.startsWith("player added")) {
                Platform.runLater(() -> {
                    numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
                });
            } else {
                setGameState(argString);
                setChanged();
                notifyObservers(argString);
            }
        }
    }

    public void hostGame() {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue());
        guestPlayer = HostPlayer.get(guestPlayer);
        numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
        guestPlayer.addObserver(this);
    }

    public void startGame() {
        setGameState(guestPlayer.startGame().substring(13));
    }

    public String joinGame(String gameId) {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), gameId);
        guestPlayer.addObserver(this);
        String result = guestPlayer.joinGame();
        return result;
    }

    public Integer getScore() {
        return guestPlayer.getScore();
    }

    public String tryPlaceWord(Character[] word, int x, int y, boolean isHorizontal) {
        if (guestPlayer.isMyTurn()) {
            String newState = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);
            // int score = Integer.parseInt(result);
            // score += Integer.parseInt(scoreProperty.getValue());
            // scoreProperty.setValue(String.valueOf(score));
            setGameState(newState);
            scoreProperty.setValue(String.valueOf(guestPlayer.getScore()));
            return newState;
        } else {
            System.out.println("not my turn");
            return scoreProperty.getValue();
        }
    }

    public String[][] getBoard() {
        String[][] board = new String[15][15];
        String[] rows = this.board.split(",");
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split(" ");
            for (int j = 0; j < row.length; j++) {
                board[i][j] = row[j];
            }
        }
        return board;
    }

    public void printBoard() {
        String result = this.board;
        String[] rows = result.split(",");
        for (int i = 0; i < rows.length; i++) {
            System.out.println(rows[i]);
        }
    }

    public String getTile() {
        String result = guestPlayer.getTile();
        return result.split(" ")[1];
    }

    public String getPlayerTiles() {
        return guestPlayer.printTiles();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Boolean querryWord(String word) {
        // return true;
        return guestPlayer.queryIO(word);
    }

    public void setGameState(String gameState) {
        String turn = gameState.split(";")[0];
        String board = gameState.split(";")[1];
        String players = gameState.split(";")[2];

        this.board = board;
        this.players = players;
        this.turn = turn.equals(guestPlayer.getName()) ? true : false;
    }

}
