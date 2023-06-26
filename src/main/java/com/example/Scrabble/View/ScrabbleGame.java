package com.example.Scrabble.View;

import com.example.Scrabble.VM.ViewModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScrabbleGame extends Application {

    ViewModel viewModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-scene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Scrabble Game");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        viewModel = ViewModel.get();
        viewModel.setStage(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // public static void setScene(String fxmlFile) {
    // try {
    // FXMLLoader loader = new FXMLLoader(ScrabbleGame.class.getResource(fxmlFile));
    // Parent root = loader.load();
    // Scene scene = new Scene(root, 1000, 700);
    // scene.getStylesheets().add(ScrabbleGame.class.getResource("style.css").toExternalForm());
    // Stage stage = (Stage) root.getScene().getWindow();
    // stage.setScene(scene);
    // } catch (Exception e) {
    // System.out.println(e);
    // }
    // }
}
