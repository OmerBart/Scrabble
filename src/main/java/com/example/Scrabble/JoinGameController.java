package com.example.Scrabble;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JoinGameController {

    private Stage stage;
    private Scene scene;

    @FXML
    protected void joinGame(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("board-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    protected void backToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}