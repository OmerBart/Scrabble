package com.example.Scrabble.View;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ScrabbleController implements Initializable {

    @FXML
    private Pane rootPane;

    @FXML
    private Canvas boardCanvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initializeCanvasSize);
    }

    private void initializeCanvasSize() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();
    }
}
