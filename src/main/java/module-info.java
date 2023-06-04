module com.example.Scrabble {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.Scrabble to javafx.fxml;
    exports com.example.Scrabble;
    exports com.example.Scrabble.View;
    opens com.example.Scrabble.View to javafx.fxml;
}