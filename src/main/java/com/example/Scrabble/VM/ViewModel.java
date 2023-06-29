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
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        System.out.println("VM: Game has been updated");
        System.out.println("VM: " + arg);
        if (arg instanceof String) {
            String argString = (String) arg;
            if (arg.equals("game started!")) {
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
                // int numberOfPlayers = Integer.parseInt(numberOfPlayersProperty.getValue());
                // ++numberOfPlayers;
                // String players = String.valueOf(numberOfPlayers);
                Platform.runLater(() -> {
                    numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
                    // numberOfPlayersProperty.setValue(players);
                });
            } else if (argString.startsWith("Board")) {
                setChanged();
                notifyObservers(argString);
            } else if (argString.startsWith("T:true")) {
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
        System.out.println(guestPlayer.startGame());
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
            String result = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);
            int score = Integer.parseInt(result);
            score += Integer.parseInt(scoreProperty.getValue());
            scoreProperty.setValue(String.valueOf(score));
            return result;
        } else {
            System.out.println("not my turn");
            return scoreProperty.getValue();
        }
    }

    public String[][] getBoard() {
        String[][] board = new String[15][15];
        String result = guestPlayer.getCurrentBoard();
        String[] rows = result.split(",");
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split(" ");
            for (int j = 0; j < row.length; j++) {
                board[i][j] = row[j];
            }
        }
        return board;
    }

    public void printBoard() {
        String result = guestPlayer.getCurrentBoard();
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

}
