package com.example.Scrabble.VM;

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

public class ViewModel implements Observer {

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
        numberOfPlayersProperty = new SimpleStringProperty("0");
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

    public String tryPlaceWord(Character[] word, int x, int y, boolean isHorizontal) {
        if (guestPlayer.isMyTurn()) {

            // DONE: omer from here im passing the word as a char array, you need to change the
            // placeWord function to accept a char array instead of a string

            String result = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);

            //String result ="0";
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
