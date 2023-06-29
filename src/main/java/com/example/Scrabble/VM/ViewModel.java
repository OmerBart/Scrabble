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

/**
 * The `ViewModel` class acts as the view model component in the MVVM (Model-View-ViewModel) architectural pattern.
 * It manages the state and behavior of the Scrabble game application.
 *
 * @author Eilon
 */
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

    /**
     * Returns the singleton instance of `ViewModel`. If no instance exists, it creates one and returns it.
     *
     * @return The singleton instance of `ViewModel`.
     */
    public static ViewModel get() {
        if (viewModelInstance == null) {
            viewModelInstance = new ViewModel();
        }
        return viewModelInstance;
    }

    /**
     * Constructs a new instance of the `ViewModel` class.
     * Initializes the various string properties used for binding in the UI.
     */
    public ViewModel() {
        playerNameProperty = new SimpleStringProperty("");
        scoreProperty = new SimpleStringProperty("0");
        boardProperty = new SimpleStringProperty("");
        playersProperty = new SimpleStringProperty("");
        numberOfPlayersProperty = new SimpleStringProperty("");
    }

    /**
     * Called when the observed object (i.e., the game model) has been updated.
     * Handles different types of updates and performs appropriate actions based on the update.
     *
     * @param o   The observed object.
     * @param arg The update argument.
     */
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
                Platform.runLater(() -> {
                    numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
                });
            } else if (argString.startsWith("Board")) {
                setChanged();
                notifyObservers(argString);
            }
        }
    }

    /**
     * Hosts a Scrabble game.
     * Creates a new guest player and registers it as a host player.
     * Updates the number of players property.
     */
    public void hostGame() {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue());
        guestPlayer = HostPlayer.get(guestPlayer);
        numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
        guestPlayer.addObserver(this);
    }

    /**
     * Starts the Scrabble game.
     * Prints the result of the game start operation.
     */
    public void startGame() {
        System.out.println(guestPlayer.startGame());
    }

    /**
     * Joins an existing Scrabble game.
     *
     * @param gameId The ID of the game to join.
     * @return The result of the join operation.
     */
    public String joinGame(String gameId) {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), gameId);
        guestPlayer.addObserver(this);
        String result = guestPlayer.joinGame();
        return result;
    }

    /**
     * Retrieves the current score of the player.
     *
     * @return The current score of the player.
     */
    public Integer getScore() {
        return guestPlayer.getScore();
    }

    /**
     * Tries to place a word on the Scrabble board.
     * If it's the player's turn, the word is placed and the score is updated.
     * Otherwise, it returns the current score.
     *
     * @param word        The word to place on the board.
     * @param x           The starting x-coordinate of the word.
     * @param y           The starting y-coordinate of the word.
     * @param isHorizontal True if the word is to be placed horizontally, false if vertically.
     * @return The result of the word placement operation or the current score if it's not the player's turn.
     */
    public String tryPlaceWord(Character[] word, int x, int y, boolean isHorizontal) {
        if (guestPlayer.isMyTurn()) {
            String result = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);
            int score = Integer.parseInt(result);
            score += Integer.parseInt(scoreProperty.getValue());
            scoreProperty.setValue(String.valueOf(score));
            guestPlayer.endTurn();
            return result;
        } else {
            System.out.println("not my turn");
            return scoreProperty.getValue();
        }
    }

    /**
     * Retrieves the current state of the Scrabble board.
     *
     * @return A 2D array representing the current state of the Scrabble board.
     */
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

    /**
     * Prints the current state of the Scrabble board.
     */
    public void printBoard() {
        String result = guestPlayer.getCurrentBoard();
        String[] rows = result.split(",");
        for (int i = 0; i < rows.length; i++) {
            System.out.println(rows[i]);
        }
    }

    /**
     * Retrieves a tile from the tile bag.
     *
     * @return The retrieved tile.
     */
    public String getTile() {
        String result = guestPlayer.getTile();
        return result.split(" ")[1];
    }

    /**
     * Retrieves the current tiles of the player.
     *
     * @return The current tiles of the player.
     */
    public String getPlayerTiles() {
        return guestPlayer.printTiles();
    }

    /**
     * Sets the stage for the application's UI.
     *
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Queries the dictionary server to check if a word is valid.
     *
     * @param word The word to query.
     * @return True if the word is valid, false otherwise.
     */
    public Boolean queryWord(String word) {
        return guestPlayer.queryDictionaryServer(word);
    }
}
