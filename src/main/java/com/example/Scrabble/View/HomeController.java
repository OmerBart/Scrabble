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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    private Stage stage;
    private Scene scene;
    private ViewModel viewModel;

    @FXML
    private Label welcomeText;

    @FXML
    private TextField nameInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = ViewModel.get();
        viewModel.playerNameProperty.bind(nameInput.textProperty());
    }

    @FXML
    protected void onJoinGameButtonClick(ActionEvent event) {
        try {
            if (viewModel.playerNameProperty.getValue().equals("")) {
                welcomeText.setText("Please enter a name");
                return;
            }
            Parent root = FXMLLoader.load(getClass().getResource("join-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            viewModel.setStage(stage);
            scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            viewModel.setScene(scene);
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onHostGameButtonClick(ActionEvent event) {
        try {
            if (viewModel.playerNameProperty.getValue().equals("")) {
                welcomeText.setText("Please enter a name");
                return;
            }
            viewModel.hostGame();
            Parent root = FXMLLoader.load(getClass().getResource("loby-scene.fxml"));
            stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            viewModel.setScene(scene);
            viewModel.setStage(stage);
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}