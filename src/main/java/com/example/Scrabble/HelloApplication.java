package com.example.Scrabble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // add css
        FXMLLoader fxmlHomePage = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene homePage = new Scene(fxmlHomePage.load(), 320, 240);
        stage.setTitle("Scrabble Game");
        stage.setScene(homePage);
        stage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}