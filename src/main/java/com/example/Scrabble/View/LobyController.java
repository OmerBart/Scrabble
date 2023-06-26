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
import javafx.stage.Stage;

public class LobyController implements Initializable {

    private Stage stage;
    private Scene scene;
    private ViewModel viewModel;

    @FXML
    Label waitingText;

    @FXML
    Button startGameButton;

    @FXML
    ComboBox<String> numberOfRounds;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel = ViewModel.get();
        if (viewModel.guestPlayer instanceof HostPlayer) {
            waitingText.setVisible(false);
            numberOfRounds.setVisible(true);
            numberOfRounds.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        } else {
            waitingText.setVisible(true);
            startGameButton.setVisible(false);
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