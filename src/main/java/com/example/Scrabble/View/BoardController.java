package com.example.Scrabble.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.example.Scrabble.VM.ViewModel;

public class BoardController implements Initializable {

    ArrayList<Tile> tilesList = new ArrayList<>();
    Tile selectedTile;
    ArrayList<BoardCell> wordToSet = new ArrayList<>();
    ViewModel viewModel;
    StringProperty wordToCheck;
    StringProperty boardString = new SimpleStringProperty("");

    @FXML
    StackPane wordPane;

    @FXML
    Label scoreText;

    @FXML
    Label nameText;

    @FXML
    Label wordText;

    @FXML
    private GridPane board;

    @FXML
    private HBox tiles;

    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get ViewModel instance
        viewModel = ViewModel.get();

        // Set welcome text and build board
        welcomeText.setText("Welcome to Scrabble!");
        welcomeText.getStyleClass().add("welcome-text");
        wordToCheck = new SimpleStringProperty("");
        wordText.textProperty().bind(wordToCheck);
        boardBuild();

        // Bindings
        nameText.textProperty().bind(viewModel.playerNameProperty);
        scoreText.textProperty().bind(viewModel.scoreProperty);

        // Set first 7 tiles
        String[] initialTiles = viewModel.getPlayerTiles().split(" ");
        for (String letter : initialTiles) {
            Tile tile = new Tile(letter);
            tilesList.add(tile);
            tiles.getChildren().add(tile);
            tile.setOnMouseClicked(event -> {
                handleTileClick(event, tile);
            });
        }
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
        System.out.println(letter);
        Tile tile = new Tile(letter);
        tilesList.add(tile);
        tiles.getChildren().add(tile);
        tile.setOnMouseClicked(event -> {
            handleTileClick(event, tile);
        });
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
            wordToSet.add(cell);
        }
        if (selectedTile != null && !cell.isOccupied) {
            BoardCell newCell = new BoardCell(selectedTile.getLetter(), cell.row, cell.col);
            newCell.getRect().getStyleClass().clear();
            newCell.getRect().getStyleClass().add("board-cell-tile");
            newCell.letter = selectedTile.getLetter();
            newCell.bonus = cell.bonus;
            newCell.isStar = cell.isStar;
            // newCell.isOccupied = true;
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
            String word = "";
            Character[] wordArr = new Character[wordToSet.size()];
            int i = 0;
            for (BoardCell cell : wordToSet) {
                System.out.println(cell.letter);
                if (cell.isOccupied) {
                    starOrOcupied = true;
                    wordArr[i++] = null;
                } else if (cell.isStar) {
                    starOrOcupied = true;
                    word += cell.letter;
                    wordArr[i++] = cell.letter.charAt(0);
                } else {
                    word += cell.letter;
                    wordArr[i++] = cell.letter.charAt(0);
                }
            }
            if (!starOrOcupied) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Word must be placed on star or use occupied cell");
                alert.showAndWait();
                return;
            }
            Boolean isHorizontal = wordToSet.get(0).row == wordToSet.get(1).row ? true : false;
            String res = viewModel.tryPlaceWord(wordArr, wordToSet.get(0).row, wordToSet.get(0).col, isHorizontal);
            System.out.println(res);
            boardBuild();
            wordToSet.clear();
            wordToCheck.setValue("");
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
        // System.out.println(viewModel.getBoard());
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
        for (BoardCell cell : wordToSet) {
            Tile tile = new Tile(cell.letter);
            cell.setDefaultStyle();
            tile.setOnMouseClicked(event -> {
                handleTileClick(event, tile);
            });
            tiles.getChildren().add(tile);
        }
        wordToSet.clear();
        wordToCheck.setValue("");
        wordPane.getChildren().get(0).setStyle("-fx-fill: white ;");

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
    }
}