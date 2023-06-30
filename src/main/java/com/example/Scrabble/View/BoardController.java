package com.example.Scrabble.View;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observer;
import java.util.ResourceBundle;

import com.example.Scrabble.Model.Player.HostPlayer;
import com.example.Scrabble.VM.ViewModel;

public class BoardController implements Initializable, Observer {

    ArrayList<Tile> tilesList = new ArrayList<>();
    Tile selectedTile;
    ArrayList<BoardCell> wordToSet = new ArrayList<>();
    ViewModel viewModel;
    StringProperty wordToCheck;
    StringProperty boardString = new SimpleStringProperty("");
    boolean turn = false;
    public StringProperty numberOfTurnsProperty;

    @FXML
    StackPane wordPane;

    @FXML
    Label scoreText;

    @FXML
    Label nameText;

    @FXML
    Label wordText;

    @FXML
    Label turnText;

    @FXML
    Label numOfTurns;

    @FXML
    VBox playersTable;

    @FXML
    VBox leftPane;

    @FXML
    private GridPane board;

    @FXML
    private HBox tiles;

    @FXML
    private Label welcomeText;

    @FXML
    Button placeWordButton;

    @FXML
    Button getTileButton;

    @FXML
    Button querryButton;

    @FXML
    Button challengeButton;
    @FXML

    private ScrollPane tilesScrollPane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get ViewModel instance
        viewModel = ViewModel.get();
        viewModel.addObserver(this);

        turn = viewModel.turn;

        // Set welcome text and build board
        welcomeText.setText("Welcome to Scrabble!");
        welcomeText.getStyleClass().add("welcome-text");
        wordToCheck = new SimpleStringProperty("");
        wordText.textProperty().bind(wordToCheck);
        numberOfTurnsProperty = new SimpleStringProperty("Turns left: " + viewModel.numberOfTurns);
        leftPane.getStyleClass().add("left-pane");

        // Set HBox as the content of the ScrollPane
        tilesScrollPane.setContent(tiles);

        // Set ScrollPane properties
        tilesScrollPane.setFitToWidth(true);
        tilesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tilesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        boardBuild();

        // Bindings
        nameText.textProperty().bind(viewModel.playerNameProperty);
        scoreText.textProperty().bind(viewModel.scoreProperty);
        numOfTurns.textProperty().bind(numberOfTurnsProperty);

        // Set first 7 tiles
        setTiles();

        // Set TableView
        setTableView();

        // Set buttons
        setButtons();

        // Set turn text
        setTurnText();
    }

    public void setTurnText() {
        if (turn) {
            turnText.setText("It's your turn!");
            turnText.setStyle("-fx-text-fill: green;");
        } else {
            turnText.setText("wait for your turn!");
            turnText.setStyle("-fx-text-fill: red;");
        }
    }

    public void setTiles() {
        tilesList.clear();
        tiles.getChildren().clear();
        String[] initialTiles = viewModel.getPlayerTiles().split(" ");
        for (String letter : initialTiles) {
            Tile tile = new Tile(letter);
            tilesList.add(tile);
            tiles.getChildren().add(tile);
            HBox.setHgrow(tile, Priority.NEVER); // Prevent tiles from expanding horizontally
            VBox.setVgrow(tile, Priority.NEVER); // Prevent tiles from expanding vertically
            tile.setOnMouseClicked(event -> {
                handleTileClick(event, tile);
            });
        }
    }

    public void setTableView() {
        playersTable.getChildren().clear();
        String[] players = viewModel.players.split(",");
        for (String player : players) {
            String[] playerInfo = player.split(":");
            Label label = new Label(playerInfo[0] + "'s points: " + playerInfo[3]);
            playersTable.getChildren().add(label);
        }
    }

    public void setButtons() {
        if (turn) {
            placeWordButton.setDisable(false);
            getTileButton.setDisable(false);
            querryButton.setDisable(false);
            challengeButton.setDisable(false);
        } else {
            placeWordButton.setDisable(true);
            getTileButton.setDisable(true);
            querryButton.setDisable(true);
            challengeButton.setDisable(true);
        }
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        Platform.runLater(() -> {
            System.out.println("View: Game has been updated");
            turn = viewModel.turn;
            boardBuild();
            setTableView();
            setButtons();
            setTurnText();
            numberOfTurnsProperty.setValue("Turns left: " + viewModel.numberOfTurns);
        });
    }

    public void boardBuild() {
        String[][] boardArray = viewModel.getBoard();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                newCellBuilder(boardArray[i][j], i + 1, j + 1);
            }
        }
        boardBordersBuild();
    }

    public void boardBordersBuild() {
        board.getChildren().get(0).getStyleClass().clear();
        board.getChildren().get(0).getStyleClass().add("hide");
        for (int i = 1; i < 16; i++) {
            String letter = String.valueOf((char) (i + 64));
            Label label = new Label(letter);
            Rectangle rect = new Rectangle(40, 40);
            rect.getStyleClass().add("board-border");
            StackPane stack = new StackPane(rect, label);
            stack.setAlignment(Pos.CENTER);
            stack.getStyleClass().add("board-border");
            board.add(stack, i, 0);
            board.getChildren().get(i).getStyleClass().clear();
            board.getChildren().get(i).getStyleClass().add("board-border");
        }
        for (int i = 1; i < 16; i++) {
            Label label = new Label(String.valueOf(i));
            label.setRotate(270);
            Rectangle rect = new Rectangle(40, 40);
            rect.getStyleClass().add("board-border");
            StackPane stack = new StackPane(rect, label);
            stack.setAlignment(Pos.CENTER);
            board.add(stack, 0, i);
        }
    }

    private void newCellBuilder(String letter, int r, int c) {
        BoardCell cell = new BoardCell(letter, r, c);
        cell.setOnMouseClicked(event -> {
            handleBoardClick(event, cell);
        });
        board.add(cell, c, r);
    }

    public void getTile() {
        String letter = viewModel.getTile();
        if(letter.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Game tile bag is Empty!");
            alert.showAndWait();
        }
        else {
            Tile tile = new Tile(letter);
            tilesList.add(tile);
            tiles.getChildren().add(tile);
            tile.setOnMouseClicked(event -> {
                handleTileClick(event, tile);
            });
        }
        //setTiles();
        viewModel.guestPlayer.endTurn();
        turn = viewModel.turn;
        boardBuild();
        setTableView();
        setTurnText();
    }


    private void handleTileClick(Event e, Tile tile) {
        if (tile.selected) {
            tile.selected = false;
            tile.getChildren().get(0).getStyleClass().clear();
            tile.getChildren().get(0).getStyleClass().add("tile");
            selectedTile = null;
        } else {
            if (selectedTile != null) {
                selectedTile.selected = false;
                selectedTile.getChildren().get(0).getStyleClass().clear();
                selectedTile.getChildren().get(0).getStyleClass().add("tile");
            }
            tile.selected = true;
            tile.getChildren().get(0).getStyleClass().clear();
            tile.getChildren().get(0).getStyleClass().add("tile-selected");
            selectedTile = tile;
        }
    }

    private void handleBoardClick(Event e, BoardCell cell) {
        tryPlaceTile(cell);
    }

    private void tryPlaceTile(BoardCell cell) {
        if (cell.isOccupied) {
            System.out.println("Cell is occupied but you can use it to bulid a word");
            cell.getRect().getStyleClass().clear();
            cell.getRect().getStyleClass().add("board-cell-tile");
            wordToSet.add(cell);
            wordToCheck.setValue(wordToCheck.getValue() + cell.letter);
        }
        if (selectedTile != null && !cell.isOccupied) {
            BoardCell newCell = new BoardCell(selectedTile.getLetter(), cell.row, cell.col);
            newCell.getRect().getStyleClass().clear();
            newCell.getRect().getStyleClass().add("board-cell-tile");
            newCell.letter = selectedTile.getLetter();
            newCell.bonus = cell.bonus;
            newCell.isStar = cell.isStar;
            newCell.isOccupied = false;
            newCell.setOnMouseClicked(event -> {
                handleBoardClick(event, newCell);
            });
            board.add(newCell, cell.col, cell.row);
            wordToSet.add(newCell);
            wordToCheck.setValue(wordToCheck.getValue() + newCell.letter);

            selectedTile.selected = false;
            selectedTile.getChildren().get(0).getStyleClass().clear();
            selectedTile.getChildren().get(0).getStyleClass().add("tile");
            tiles.getChildren().remove(selectedTile);
            selectedTile = null;
        }
    }

    public void placeWord() {
        selectedTile = null;
        if (isSequenceWord()) {
            Boolean starOrOcupied = false;
            Character[] wordArr = new Character[wordToSet.size()];
            int i = 0;
            for (BoardCell cell : wordToSet) {
                if (cell.isOccupied) {
                    starOrOcupied = true;
                    wordArr[i++] = null;
                } else if (cell.isStar) {
                    starOrOcupied = true;
                    wordArr[i++] = cell.letter.charAt(0);
                } else {
                    wordArr[i++] = cell.letter.charAt(0);
                }
            }
            if (!starOrOcupied) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Word must be placed on star or use occupied cell");
                alert.showAndWait();
                return;
            }
            boolean isHorizontal = wordToSet.get(0).row == wordToSet.get(1).row;
            String err = viewModel.tryPlaceWord(wordArr, wordToSet.get(0).row, wordToSet.get(0).col, isHorizontal);
            if (err.contains("Error")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(err);
                alert.showAndWait();
                // clear();
            } else {
                setTiles();
                viewModel.guestPlayer.endTurn();
                turn = viewModel.turn;
                boardBuild();
                setTableView();
                setTurnText();

                wordToSet.clear();
                wordToCheck.setValue("");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Word is not a sequence");
            alert.showAndWait();
            for (BoardCell cell : wordToSet) {
                Tile tile = new Tile(cell.letter);
                cell.setDefaultStyle();
                tile.setOnMouseClicked(event -> {
                    handleTileClick(event, tile);
                });
                tiles.getChildren().add(tile);
            }
            wordToSet.clear();

        }
    }

    private boolean isSequenceWord() {
        wordToSet.sort(Comparator.comparing(BoardCell::getRow).thenComparing(BoardCell::getCol));
        boolean isSequence = false;
        for (int i = 0; i < wordToSet.size() - 1; i++) {
            if (wordToSet.get(i).row == wordToSet.get(i + 1).row
                    && wordToSet.get(i).col == wordToSet.get(i + 1).col - 1) {
                isSequence = true;
            } else {
                isSequence = false;
                break;
            }
        }
        if (!isSequence) {
            for (int i = 0; i < wordToSet.size() - 1; i++) {
                if (wordToSet.get(i).col == wordToSet.get(i + 1).col &&
                        wordToSet.get(i).row == wordToSet.get(i + 1).row - 1) {
                    isSequence = true;
                } else {
                    isSequence = false;
                    break;
                }
            }
        }
        return isSequence;
    }

    public void clear() {
        wordToSet.clear();
        wordToCheck.setValue("");
        wordPane.getChildren().get(0).setStyle("-fx-fill: white ;");
        setTiles();
        boardBuild();
    }

    public void onQuerryClick() {
        System.out.println("Querry");
        String word = wordToCheck.getValue();
        if (word.length() > 0) {
            if (viewModel.querryWord(word)) {
                wordPane.getChildren().get(0).setStyle("-fx-fill: green ;");
            } else {
                wordPane.getChildren().get(0).setStyle("-fx-fill: red;");
            }
        }
    }

    public void onChallengeClick() {
        System.out.println("Challenge");
        String word = wordToCheck.getValue();
        if (word.length() > 0) {
            if (viewModel.challengeWord(word)) {
                wordPane.getChildren().get(0).setStyle("-fx-fill: green ;");
            } else {
                wordPane.getChildren().get(0).setStyle("-fx-fill: red;");
            }
        }
    }
}