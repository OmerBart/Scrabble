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
 * The ViewModel class serves as the bridge between the Model and View in the Scrabble game.
 * It provides the necessary properties and methods to manage the game state and handle user interactions.
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

    public Boolean turn;
    public String board;
    public String players;
    public String numberOfTurns;

    private static ViewModel viewModelInstance = null;

    /**
     * The get function is a static function that returns the singleton instance of ViewModel.
     * If no instance exists, it creates one and returns it.
     *
     */
    public static ViewModel get() {
        if (viewModelInstance == null) {
            viewModelInstance = new ViewModel();
        }
        return viewModelInstance;
    }
    /**
     * Constructs a new ViewModel object.
     * Initializes the properties and game state.
     */

    private ViewModel() {

        playerNameProperty = new SimpleStringProperty("");
        scoreProperty = new SimpleStringProperty("0");
        boardProperty = new SimpleStringProperty("");
        playersProperty = new SimpleStringProperty("");
        numberOfPlayersProperty = new SimpleStringProperty("");
        turn = false;
        board = "";
        players = "";
    }

    /**
     * Updates the ViewModel when the observed object changes.
     *
     * @param o   The Observable object.
     * @param arg The argument passed by the Observable object.
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        System.out.println("VM: Game has been updated");
        if (arg instanceof String) {
            String argString = (String) arg;
            if (argString.startsWith("game started!")) {
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

    /**
     * The hostGame function is called when the user clicks on the &quot;Host Game&quot; button.
     * It creates a new GuestPlayer object, and then calls HostPlayer.get(GuestPlayer) to create a new HostPlayer object.
     * The numberOfPlayersProperty is set to the value of guestPlayer's getNumberOfPlayers() function, which returns an int representing how many players are in this game (including this player).
     *
     */
    public void hostGame() {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue());
        guestPlayer = HostPlayer.get(guestPlayer);
        numberOfPlayersProperty.setValue(String.valueOf(guestPlayer.getNumberOfPlayers()));
        guestPlayer.addObserver(this);
    }

    /**
     * The startGame function is used to start the game.
     * It calls the startGame function in guestPlayer and sets the gameState variable to whatever it returns.
     *
     */

    public void startGame() {
        setGameState(guestPlayer.startGame().substring(13));
    }


    /**
     * The joinGame function is called when the user clicks on the &quot;Join Game&quot; button.
     * It creates a new GuestPlayer object, and calls its joinGame function to connect to
     * the server.  The result of this connection attempt is returned as a String, which can be displayed in an alert box.

     *
     * @param  gameId Join the game with that id
     *
     * @return A string containing the result of the connection attempt
     *
     */
    public String joinGame(String gameId) {
        guestPlayer = new GuestPlayer(playerNameProperty.getValue(), gameId);
        guestPlayer.addObserver(this);
        String result = guestPlayer.joinGame();
        return result;
    }

    /**
     * The getScore function returns the score of the guest player.
     *
     *
     * @return The score of the guest player
     *
     */
    public Integer getScore() {
        return guestPlayer.getScore();
    }

    /**
     * The tryPlaceWord function is used to place a word on the board. It calls the placeWord function in guestPlayer and sets the gameState variable to whatever it returns.
     * It also updates the scoreProperty to the new score of the guest player. If the word is not placed successfully, it returns a string containing the error message.
     *
     * @param  word Pass in the word that is being placed
     * @param  x Determine the x coordinate of the word

     * @param  y Determine the row of the board
     * @param  isHorizontal Determine whether the word is placed horizontally or vertically
     *
     * @return A string
     *
     *
     */
    public String tryPlaceWord(Character[] word, int x, int y, boolean isHorizontal) {
        if (guestPlayer.isMyTurn()) {
            String newState = guestPlayer.placeWord(word, x - 1, y - 1, isHorizontal);
            if(newState.split(";").length < 4) {
                return newState;
            }
            // int score = Integer.parseInt(result);
            // score += Integer.parseInt(scoreProperty.getValue());
            // scoreProperty.setValue(String.valueOf(score));
            setGameState(newState);
            scoreProperty.setValue(String.valueOf(guestPlayer.getScore()));
            return "";
        } else {
            System.out.println("not my turn");
            return scoreProperty.getValue();
        }
    }

    /**
     * The getBoard function takes the board string and converts it into a 2D array of strings.
     *
     *
     * @return A 2d array of strings representing the board
     *
     *
     */
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

    /**
     * The printBoard function prints the board to the console.

     *
     *
     *
     *
     *
     */
    public void printBoard() {
        String result = this.board;
        String[] rows = result.split(",");
        for (int i = 0; i < rows.length; i++) {
            System.out.println(rows[i]);
        }
    }

    /**
     * The getTile function returns the tile that the player is currently on.
     *
     *
     *
     * @return The tile that the player has chosen to play
     *
     *
     */
    public String getTile() {
        String result = guestPlayer.getTile();
        return result.split(" ")[1];
    }

    /**
     * The getPlayerTiles function returns the tiles of the guest player.
     *
     *
     * @return The tiles in the player's hand
     *
     */
    public String getPlayerTiles() {
        return guestPlayer.printTiles();
    }

    /**
     * The setStage function is used to set the stage of the application.
     *
     *
     * @param  stage Set the stage for the scene
     *
     *
     * @docauthor Trelent
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * The querryWord function takes a string as an argument and returns true if the word is in the dictionary, false otherwise.
     *
     *
     * @param  word Pass the word to be checked
     *
     * @return True if the word is in the dictionary, false otherwise
     *
     */
    public Boolean querryWord(String word) {
        // return true;
        return guestPlayer.queryDictionaryServer(word);
    }

    /**
     * The setGameState function takes in a string that represents the game state, and sets the board, players, turn and number of turns to their respective values.
     *
     *
     * @param  gameState Set the game state
     *
     */
    public void setGameState(String gameState) {
        String turn = gameState.split(";")[0];
        String board = gameState.split(";")[1];
        String players = gameState.split(";")[2];
        String numOfTurns = gameState.split(";")[3];

        this.board = board;
        this.players = players;
        this.turn = turn.equals(guestPlayer.getName()) ? true : false;
        this.numberOfTurns = numOfTurns;
    }

    /**
     * The challengeWord function is used to challenge a word that has been played by the opponent.
     * The function takes in a String parameter, which is the word being challenged.
     * It returns true if the word exists in the dictionary and false otherwise.

     *
     * @param  word Pass the word to be challenged
     *
     * @return A boolean value true for a valid word and false for an invalid word
     *
     */
    public boolean challengeWord(String word) {
        return guestPlayer.challengeIOserver(word);
    }
}
