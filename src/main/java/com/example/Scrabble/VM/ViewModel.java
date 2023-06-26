package com.example.Scrabble.VM;

import java.util.Observer;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;
import com.example.Scrabble.View.ScrabbleGame;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewModel implements Observer {

    public GuestPlayer guestPlayer;
    public StringProperty playerNameProperty;
    public StringProperty scoreProperty;
    public StringProperty boardProperty;
    public StringProperty playersProperty;
    private Stage stage;
    private Scene scene;

    @FXML

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

    @Override
    public void update(java.util.Observable o, Object arg) {
        System.out.println("VM: Player has been updated");
        System.out.println("VM: " + arg);
        if (arg instanceof String) {
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
            } else {
                System.out.println("VM: " + arg);
            }
        }
    }

    public void hostGame() {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue());
        guestPlayer = HostPlayer.get(guestPlayer);
        guestPlayer.addObserver(this);
    }

    public void startGame() {
        System.out.println(guestPlayer.startGame());
    }

    public String joinGame(String gameId) {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), gameId);
        guestPlayer.addObserver(this);
        return guestPlayer.joinGame();
    }

    public Integer getScore() {
        return guestPlayer.getScore();
    }

    public String tryPlaceWord(String word, int x, int y, boolean isHorizontal) {
        System.out.println("word: " + word + " x: " + x + " y: " + y + " isHorizontal: " + isHorizontal);
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);
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

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return this.stage;
    }
}
