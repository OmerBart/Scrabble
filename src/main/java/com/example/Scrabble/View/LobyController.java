package com.example.Scrabble.View;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.Scrabble.VM.ViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LobyController implements Initializable {

    private Stage stage;
    private Scene scene;
    private ViewModel viewModel;

    @FXML
    Label player1Name;
    
    @FXML
    Label player2Name;

    @FXML
    Label player3Name;

    @FXML
    Label player4Name;

    @FXML
    Button player1Button;

    @FXML
    Button player2Button;

    @FXML
    Button player3Button;

    @FXML
    Button player4Button;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel = ViewModel.get();
        player1Name.textProperty().bind(viewModel.playerNameProperty);
        player1Button.getStyleClass().add("not-ready-button");
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
        try {
            player1Button.getStyleClass().remove("not-ready-button");
            player1Button.getStyleClass().add("ready-button");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onHostGameButtonClick(ActionEvent event) {
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