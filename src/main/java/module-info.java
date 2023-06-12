module com.example.Scrabble {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.example.Scrabble to javafx.fxml;

    exports com.example.Scrabble;
}