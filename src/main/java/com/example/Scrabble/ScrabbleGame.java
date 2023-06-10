package com.example.Scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScrabbleGame extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-scene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Scrabble Game");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
