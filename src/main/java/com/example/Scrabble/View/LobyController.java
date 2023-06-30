package com.example.Scrabble.View;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.Scrabble.Model.Player.HostPlayer;
import com.example.Scrabble.VM.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LobyController implements Initializable {

    public Label gameid;
    private Stage stage;
    private Scene scene;
    private ViewModel viewModel;

    @FXML
    Label nofcontainer;

    @FXML
    Label waitingText;

    @FXML
    Label numberOfPlayers;

    @FXML
    Button startGameButton;

    @FXML
    TextField numberOfRounds;

    @FXML
    Label numberOfRoundsText;

    @FXML
    ComboBox<String> bookSet;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel = ViewModel.get();
        numberOfPlayers.textProperty().bindBidirectional(viewModel.numberOfPlayersProperty);
        if (viewModel.guestPlayer instanceof HostPlayer) {
            gameid.setText("Game ID: " + viewModel.guestPlayer.getServerAddress());
            waitingText.setVisible(false);
            numberOfRounds.setVisible(true);
            numberOfRoundsText.setVisible(true);
            bookSet.setVisible(true);
            bookSet.setPromptText("Select a book");
            bookSet.getItems().addAll("Alice In Wonderland", "Dune", "Harry Potter", "Moby Dick", "Pg10",
                    "Shakespeare", "The Matrix");
        } else {
            waitingText.setVisible(true);
            startGameButton.setVisible(false);
            numberOfRounds.setVisible(false);
            numberOfPlayers.setVisible(false);
            nofcontainer.setVisible(false);
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
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

    @FXML
    protected void onReadyButtonClick(ActionEvent event) {
    }

    @FXML
    protected void onStartGameButtonClick(ActionEvent event) {
        try {
            HostPlayer hostPlayer = HostPlayer.get(viewModel.guestPlayer);
            Integer numOfRounds = Integer.parseInt(numberOfRounds.getText() != "" ? numberOfRounds.getText() : "30");
            hostPlayer.setNumOfTurns(numOfRounds);
            hostPlayer.setBooks(bookSet.getValue());
            viewModel.startGame();
            Parent root = FXMLLoader.load(getClass().getResource("board-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}