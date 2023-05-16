module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.Scrabble to javafx.fxml;
    exports com.example.Scrabble;
}