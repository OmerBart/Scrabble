package com.example.Scrabble;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    ArrayList<StackPane> tilesList = new ArrayList<>();

    Tile selectedTile;

    @FXML
    private GridPane board;

    @FXML
    private HBox tiles;

    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeText.setText("Welcome to Scrabble!");
        welcomeText.getStyleClass().add("welcome-text");
        boardBuild();

        System.out.println("here");
    }

    public void boardBuild() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                newCellBuilder(i, j);
            }
        }
        boardBordersBuild();
        drawStar();
        drawBonus();
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

    private void drawStar() {
        BoardCell cell = new BoardCell("star", 8, 8);
        board.add(cell, 8, 8);
    }

    public void drawBonus() {
        newCellBuilder("3W", 1, 1);
        newCellBuilder("3W", 1, 8);
        newCellBuilder("3W", 1, 15);
        newCellBuilder("3W", 8, 1);
        newCellBuilder("3W", 8, 15);
        newCellBuilder("3W", 15, 1);
        newCellBuilder("3W", 15, 8);
        newCellBuilder("3W", 15, 15);

        newCellBuilder("2W", 2, 2);
        newCellBuilder("2W", 3, 3);
        newCellBuilder("2W", 4, 4);
        newCellBuilder("2W", 5, 5);
        newCellBuilder("2W", 11, 11);
        newCellBuilder("2W", 12, 12);
        newCellBuilder("2W", 13, 13);
        newCellBuilder("2W", 14, 14);
        newCellBuilder("2W", 2, 14);
        newCellBuilder("2W", 3, 13);
        newCellBuilder("2W", 4, 12);
        newCellBuilder("2W", 5, 11);
        newCellBuilder("2W", 11, 5);
        newCellBuilder("2W", 12, 4);
        newCellBuilder("2W", 13, 3);
        newCellBuilder("2W", 14, 2);

        newCellBuilder("2L", 1, 4);
        newCellBuilder("2L", 4, 1);
        newCellBuilder("2L", 1, 12);
        newCellBuilder("2L", 12, 1);
        newCellBuilder("2L", 3, 7);
        newCellBuilder("2L", 7, 3);
        newCellBuilder("2L", 4, 8);
        newCellBuilder("2L", 8, 4);
        newCellBuilder("2L", 9, 3);
        newCellBuilder("2L", 3, 9);
        newCellBuilder("2L", 15, 4);
        newCellBuilder("2L", 4, 15);
        newCellBuilder("2L", 7, 7);
        newCellBuilder("2L", 7, 9);
        newCellBuilder("2L", 9, 7);
        newCellBuilder("2L", 9, 9);
        newCellBuilder("2L", 12, 8);
        newCellBuilder("2L", 8, 12);
        newCellBuilder("2L", 7, 13);
        newCellBuilder("2L", 13, 7);
        newCellBuilder("2L", 9, 13);
        newCellBuilder("2L", 13, 9);
        newCellBuilder("2L", 4, 15);
        newCellBuilder("2L", 15, 4);
        newCellBuilder("2L", 12, 15);
        newCellBuilder("2L", 15, 12);

        newCellBuilder("3L", 2, 6);
        newCellBuilder("3L", 6, 2);
        newCellBuilder("3L", 2, 10);
        newCellBuilder("3L", 10, 2);
        newCellBuilder("3L", 6, 6);
        newCellBuilder("3L", 6, 10);
        newCellBuilder("3L", 10, 6);
        newCellBuilder("3L", 10, 10);
        newCellBuilder("3L", 6, 14);
        newCellBuilder("3L", 14, 6);
        newCellBuilder("3L", 10, 14);
        newCellBuilder("3L", 14, 10);
    }

    private void newCellBuilder(int r, int c) {
        BoardCell cell = new BoardCell(r, c);
        cell.setOnMouseClicked(event -> {
            handleBoardClick(event, cell);
        });
        board.add(cell, c, r);
    }

    private void newCellBuilder(String bonus, int r, int c) {
        BoardCell cell = new BoardCell(bonus, r, c);
        cell.setOnMouseClicked(event -> {
            handleBoardClick(event, cell);
        });
        board.add(cell, c, r);
    }

    public void getTile() {
        Tile tile = new Tile();
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
        if (selectedTile != null && !cell.isOccupied) {
            System.out.println("Cell is not occupied");
            BoardCell newCell = new BoardCell(selectedTile.getLetter(), cell.row, cell.col);
            Rectangle newRectangle = new Rectangle(40, 40);
            newRectangle.getStyleClass().add("board-cell-tile");
            newCell.setRect(newRectangle);
            board.add(newCell, cell.col, cell.row);

            BoardCell right = (BoardCell) getCell(cell.row, cell.col + 1);
            right.getRect().getStyleClass().clear();
            right.getRect().getStyleClass().add("board-cell-tile");


            
        } else {
            System.out.println("Cell is occupied");
        }
    }

    private Node getCell(int row, int col) {
        for (Node cell : board.getChildren()) {
            if (GridPane.getRowIndex(cell) == row && GridPane.getColumnIndex(cell) == col) {
                return cell;
            }
        }
        return null;
    }
}