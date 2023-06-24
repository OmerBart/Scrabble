package com.example.Scrabble.View;

import com.example.Scrabble.VM.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinGameController implements Initializable {

    private Stage stage;
    private Scene scene;
    private ViewModel viewModel;

    @FXML
    TextField gameId;

    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        viewModel = ViewModel.get();
        // viewModel.gameIdProperty.bind(gameId.textProperty());
        // gameId.textProperty().addListener((observable, oldValue, newValue) -> {
        // System.out.println("View model: " + viewModel.gameIdProperty.getValue());
        // });
    }

    @FXML
    protected void joinGame(ActionEvent event) {
        try {
            System.out.println(viewModel.joinGame("localhost:65432", 1));
            Parent root = FXMLLoader.load(getClass().getResource("board-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void backToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}