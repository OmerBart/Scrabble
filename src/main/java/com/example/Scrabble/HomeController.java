package com.example.Scrabble;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {

    private Stage stage;
    private Scene scene;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onJoinGameButtonClick(ActionEvent event) {
        // change scene to board-scene.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("join-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    protected void onHostGameButtonClick() {
        welcomeText.setText("Welcome to Scrabble! (Host)");
    }

}